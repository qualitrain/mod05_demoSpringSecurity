package mx.com.qtx.ejmSpSec.web.api.jaxrs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mx.com.qtx.ejmSpSec.defServicios.IServicioPersonas;
import mx.com.qtx.ejmSpSec.entidades.Persona;

@Path("personas")
public class PersonasRest {

	@Autowired
	private IServicioPersonas servicioPersonas;
	private static Logger bitacora = LoggerFactory.getLogger(PersonasRest.class);

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Persona> getPersonas() {
		bitacora.info("getPersonas()");
		List<Persona> personas = this.servicioPersonas.getPersonasTodas();
		return personas;
	}
	
	@Path("{id}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Persona getPersona(
			@PathParam("id")
			Long id) {
		bitacora.info("getPersona("+ id + ")");
		
		Persona persona = this.servicioPersonas.getPersona(id);
		return persona;
	}

}
