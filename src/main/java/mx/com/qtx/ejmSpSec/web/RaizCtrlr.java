package mx.com.qtx.ejmSpSec.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RaizCtrlr {

	private static Logger bitacora = LoggerFactory.getLogger(RaizCtrlr.class);
	
	public RaizCtrlr() {
		bitacora.debug("instancia creada:RaizCtrlr()");
	}
	
	@GetMapping("/comenzar")
	public String getWelcomeFile(Model modelo) {
		bitacora.trace("getWelcomeFile()");
		
		bitacora.debug("modelo contiene " +  modelo.asMap().size() + " propiedades");
		return "vistaRaiz";
	}
	@GetMapping("/admin")
	public String getVistaAdmin(Model modelo) {
		return "vistaAdmin";
	}
	
	@GetMapping("/logistica")
	public String getVistaLogistica(Model modelo) {
		return "vistaLogistica";
	}
	
	@GetMapping("/info")
	public String getVistaInfo(Model modelo) {
		return "vistaInfo";
	}
	
}
