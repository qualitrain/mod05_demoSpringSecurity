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
		bitacora.info("instancia creada:RaizCtrlr()");
	}
	
	@GetMapping("/comenzar")
	public String getWelcomeFile(Model modelo) {
		return "raizHtml";
	}
	
}
