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
		if(authorizationHeader == null)
			return false;
		if(authorizationHeader.startsWith("Bearer ") == false)
			return false;

		String tokenJWT = authorizationHeader.substring(7);
		if(tokenJWT.isEmpty())
			return false;
		
		try {
			if(jwtUtil.tokenExpirado(tokenJWT))
				return false;
		}
		catch(Exception ex) {
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
