package mx.com.qtx.ejmSpSec.seguridad.servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import mx.com.qtx.ejmSpSec.seguridad.web.IExtractorTokenJwtPeticionHttp;

@Component
public class ExtractorTokenJwtPeticiones implements IExtractorTokenJwtPeticionHttp{
	@Autowired
	IGeneradorTokensJWT jwtUtil;
	private static Logger bitacora = LoggerFactory.getLogger(ExtractorTokenJwtPeticiones.class);
	
	public ExtractorTokenJwtPeticiones() {
		super();
		bitacora.info("Se ha instanciado IExtractorTokenJwtPeticionHttp [" 
		              + this.getClass().getSimpleName() + "]");
	}
		
	public boolean peticionTieneTokenValido(HttpServletRequest request) {
		bitacora.trace("peticionTieneTokenValido(" 
	                   + request.getMethod() + " " + request.getRequestURI() + ")");
		String authorizationHeader = request.getHeader("Authorization");
		bitacora.debug("Authorization:" + authorizationHeader);
		if(authorizationHeader == null) {
			bitacora.warn("authorizationHeader es nulo");
			return false;
		}
		if(authorizationHeader.startsWith("Bearer ") == false) {
			bitacora.warn("authorizationHeader no inicia con 'Bearer '");			
			return false;
		}

		String tokenJWT = authorizationHeader.substring(7);
		if(tokenJWT.isEmpty()) {
			bitacora.warn("tokenJWT está vacío");
			return false;
		}
		
		try {
			if(jwtUtil.tokenExpirado(tokenJWT)) {
				bitacora.warn("tokenJWT esta expirado");				
				return false;
			}
		}
		catch(Exception ex) {
			bitacora.warn("tokenJWT invalido por ex [" + ex.getClass().getName() + ": " + ex.getMessage() + "]");				
			return false;
		}
		return true;
	}

	public String getNombreUsuario(HttpServletRequest request) {
		if(this.peticionTieneTokenValido(request) == false)
			return null;
		String token = request.getHeader("Authorization")
							  .substring(7);
		try {
			String nombreUsuario = this.jwtUtil.extraerUsuario(token);
			return nombreUsuario;
		}
		catch(Exception ex) {
			return null;		
		}
	}
	
}
