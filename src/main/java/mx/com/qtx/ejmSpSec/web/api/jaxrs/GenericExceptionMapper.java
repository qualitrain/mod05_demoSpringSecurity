package mx.com.qtx.ejmSpSec.web.api.jaxrs;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

	private static Logger bitacora = LoggerFactory.getLogger(GenericExceptionMapper.class);	

	@Override
	public Response toResponse(Throwable exception) {
		bitacora.trace("toResponse");
		bitacora.debug(exception.getClass().getName() + ":[" + exception.getMessage() + "]");
		
		// Construir respuesta Rest para cliente
        RespuestaError respuestaError = new RespuestaError(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Servicio no disponible temporalmente"
            );
            
        Response respuesta = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                     .entity(respuestaError)
                                     .type(MediaType.APPLICATION_JSON)
                                     .build();
		
		// Armar descripcion para la Bitacora
		return respuesta;
	}

}
