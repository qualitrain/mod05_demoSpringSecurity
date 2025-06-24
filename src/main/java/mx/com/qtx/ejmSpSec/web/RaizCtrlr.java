package mx.com.qtx.ejmSpSec.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RaizCtrlr {

	private static Logger bitacora = LoggerFactory.getLogger(RaizCtrlr.class);
	
	public RaizCtrlr() {
		bitacora.debug("instancia creada:RaizCtrlr()");
	}
	
	@GetMapping("/comenzar")
	public String getWelcomeFile(Model modelo, HttpServletRequest req) {
		bitacora.trace("getWelcomeFile()");
		Principal usuario = req.getUserPrincipal();
		if(usuario == null) {
			bitacora.warn("Principal es nulo");
		}
		else {
			bitacora.debug("usuario = " + usuario.toString());
		}
		bitacora.debug("modelo contiene " +  modelo.asMap().size() + " propiedades");
//		bitacora.debug("Principal:" + this.getPrincipal());
		return "vistaRaiz";
	}
	@GetMapping("/admin")
	public String getVistaAdmin(Model modelo) {
		bitacora.trace("getVistaAdmin()");
		bitacora.debug("Principal:" + this.getPrincipal());
		return "vistaAdmin";
	}
	
	@GetMapping("/logistica")
	public String getVistaLogistica(Model modelo) {
		bitacora.trace("getVistaLogistica()");
		bitacora.debug("Principal:" + this.getPrincipal());
		return "vistaLogistica";
	}
	
	@GetMapping("/info")
	public String getVistaInfo(Model modelo) {
		bitacora.trace("getVistaInfo()");
		bitacora.debug("Principal:" + this.getPrincipal());
		return "vistaInfo";
	}
	
	private String getPrincipal() {
		bitacora.trace("getPrincipal()");
		Authentication autenticacion = SecurityContextHolder.getContext()
				                                            .getAuthentication();
		
		if(autenticacion == null) {
			bitacora.warn("Autenticacion nula");
			return "nulo";
		}
		String nomPrincipal = autenticacion.getName();
		return nomPrincipal;
	}
	
}
