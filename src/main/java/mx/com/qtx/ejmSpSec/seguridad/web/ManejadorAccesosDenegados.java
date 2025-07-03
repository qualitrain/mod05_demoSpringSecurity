package mx.com.qtx.ejmSpSec.seguridad.web;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManejadorAccesosDenegados implements AccessDeniedHandler{
	private static Logger bitacora = LoggerFactory.getLogger(ManejadorAccesosDenegados.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().append("{\"error:\"" + accessDeniedException.getMessage()
				+ "}");
		
		Principal principal = request.getUserPrincipal();		
		bitacora.warn("Acceso denegado a " + request.getMethod() + " " + request.getRequestURI() 
		 			+ ". Peticion hecha por usuario [" + principal.getName() + "] , "
		 			+ " con roles " + SecurityContextHolder.getContext()
		 			                                       .getAuthentication()
		 			                                       .getAuthorities());
	}

}
