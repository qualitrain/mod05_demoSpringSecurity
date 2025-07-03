package mx.com.qtx.ejmSpSec.seguridad.web;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroTokensJwt_SS extends OncePerRequestFilter {
	
	public static int PETICION_AUTENTICADA_OK                           =  0;
	public static int ERROR_TOKEN_INVALIDO                              = -1;
	public static int ERROR_NO_HAY_NOMBRE_USUARIO_EN_TOKEN              = -2;
	public static int ERROR_YA_HAY_TOKEN_AUTENTICACION_EN_CTX_SEGURIDAD = -3;
	
	
    private IExtractorTokenJwtPeticionHttp servTokens;
    private UserDetailsService gestorUsuariosSS;
	
	private static Logger bitacora = LoggerFactory.getLogger(FiltroTokensJwt_SS.class);

	public FiltroTokensJwt_SS(IExtractorTokenJwtPeticionHttp servTokens, UserDetailsService gestorUsuariosSS) {
		super();
		this.servTokens = servTokens;
		this.gestorUsuariosSS = gestorUsuariosSS;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		bitacora.trace("doFilterInternal(servletPath:" + request.getServletPath() 
		 								+ ", URI:" + request.getRequestURI()
		 								+ ", PathInfo:" + request.getPathInfo()
				+ ")");
		int statusAutenticacionPeticion = autenticarPeticionConTokenJWT(request);
		if(statusAutenticacionPeticion == PETICION_AUTENTICADA_OK) {
			bitacora.debug("Peticion autenticada. Prosigue cadena de filtrado");
			filterChain.doFilter(request, response);
		}
		else {
			String respuestaJSon = generarRespuestaJsonDeRechazo(response, statusAutenticacionPeticion);
			bitacora.warn("Peticion rechazada de " + request.getRemoteAddr()
					+ ". Respuesta devuelta:" + respuestaJSon);
		}
	}

	private String generarRespuestaJsonDeRechazo(HttpServletResponse response, int statusAutenticacionPeticion)
			throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		String respuesta = "{\"error\":" + statusAutenticacionPeticion + "}";
		writer.append(respuesta);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return respuesta;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String servletPath = request.getServletPath();
		String pathAutenticaion = servletPath + request.getPathInfo();
		if(pathAutenticaion.equals("/api/autenticacion"))
			return true;
		if(servletPath.startsWith("/api") == false)
			return true;
			
		return false;
	}
	
	private int autenticarPeticionConTokenJWT(HttpServletRequest request) {
		bitacora.trace("procesarToken(" + request.getMethod() + " " + request.getRequestURI() +")");
		
		if (servTokens.peticionTieneTokenValido(request) == false) {
			bitacora.warn("Peticion " + request.getMethod() + " " + request.getRequestURI() 
							+ " tiene Token Invalido");
			return ERROR_TOKEN_INVALIDO;
		}
		
		String nombreUsuario = servTokens.getNombreUsuario(request);
		bitacora.debug("nombreUsuario:" + nombreUsuario);
		
		if(nombreUsuario == null) {
			bitacora.warn("Peticion " + request.getMethod() + " " + request.getRequestURI() 
							+ " no contiene nombreUsuario");
			return ERROR_NO_HAY_NOMBRE_USUARIO_EN_TOKEN;
		}
		return publicarTokenAutenticacionEnCtxSeguridad(nombreUsuario, request);
		
	}

	private int publicarTokenAutenticacionEnCtxSeguridad(String nombreUsuario, HttpServletRequest request) {
		
		 SecurityContext ctxSeguridad = SecurityContextHolder.getContext();
		 if(ctxSeguridad.getAuthentication() != null) {
			 bitacora.info("YA HAY un principal en la petición (no debería ser así): "
				 		+ "nombrePrincipal:" + ctxSeguridad.getAuthentication().getName());
				 return ERROR_YA_HAY_TOKEN_AUTENTICACION_EN_CTX_SEGURIDAD; // Ya hay un token de Autenticación en el contexto de seguridad
		 }
		 
		 WebAuthenticationDetailsSource wads = new WebAuthenticationDetailsSource();
		 WebAuthenticationDetails webDetails = wads.buildDetails(request);
		 
		 UserDetails usuarioUd = this.gestorUsuariosSS.loadUserByUsername(nombreUsuario);
		 UsernamePasswordAuthenticationToken tknAutenticacion = null;
		 tknAutenticacion = new UsernamePasswordAuthenticationToken(usuarioUd, usuarioUd.getPassword(), 
				                                                              usuarioUd.getAuthorities());
		 tknAutenticacion.setDetails(webDetails); // Se agregan datos relacionados con la petición
		 ctxSeguridad.setAuthentication(tknAutenticacion); //Se agrega token de autenticación de este usuario al contexto
		 
		 bitacora.debug("Se ha agregado token de autenticación a ctx seguridad:" + tknAutenticacion);
		 return PETICION_AUTENTICADA_OK;
	}


}
