package mx.com.qtx.ejmSpSec.seguridad.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class ProcesadorEvtAutorizacion {
	private static Logger bitacora =  LoggerFactory.getLogger(ProcesadorEvtAutorizacion.class);
	
	public ProcesadorEvtAutorizacion() {
		super();
		bitacora.info(" Bean ProcesadorEvtAutorizacion instanciado");
	}

	@EventListener
	public void alDenegarAutorizacion(AuthorizationDeniedEvent<?> autRechazadaEvt) {
		bitacora.trace("alDenegarAutorizacion( " + autRechazadaEvt.toString() + " )");
		String nombreClase = autRechazadaEvt.getClass().getName();
		String nombreT = autRechazadaEvt.getObject().getClass().getName();
		bitacora.debug("clase de evento de autorizacion denegada:[" + nombreClase + "<" + nombreT
				+ ">]");
		
		String principal = autRechazadaEvt.getAuthentication().get().getName();
		boolean accesoConcedido = autRechazadaEvt.getAuthorizationResult().isGranted();
//		String recurso = autRechazadaEvt.getResolvableType().toString();
		String recurso = autRechazadaEvt.getObject().toString();
		
		bitacora.warn("Autorizacion Denegada. " + "Usuario " + principal 
				+ " recurso:" + recurso + ", acceso concedido:" + accesoConcedido);
	}

}
