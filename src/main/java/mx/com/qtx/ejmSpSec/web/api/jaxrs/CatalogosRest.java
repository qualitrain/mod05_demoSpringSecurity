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
import jakarta.ws.rs.core.Response;
import mx.com.qtx.ejmSpSec.defServicios.IServicioCatalogos;
import static mx.com.qtx.ejmSpSec.web.api.jaxrs.ErrorRest.getError;

@Path("catalogos")
public class CatalogosRest {
	
	@Autowired
	private IServicioCatalogos servicioCatalogos;
	private static Logger bitacora = LoggerFactory.getLogger(CatalogosRest.class);
	
	public CatalogosRest() {
		super();
		bitacora.info("CatalogosRest()");
	}
	@Path("apellidos")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<String> getApellidos() {
		
		bitacora.info("getApellidos()");
		List<String> apellidos = this.servicioCatalogos.getApellidosSugeridos("");
		if(apellidos == null) {
			throw getError("Falló el sistema subyacente", 
					        ErrorRest.ERR_FALLA_SERVICIO, 
					        Response.Status.BAD_REQUEST);
		}
		return apellidos;
		
	}
	
	@Path("apellidos/{inicio}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<String> getApellidosQueInicienCon(
			@PathParam("inicio")
			String inicio) {
		
		bitacora.info("getApellidosQueInicienCon(" + inicio + ")");
		List<String> apellidos = this.servicioCatalogos.getApellidosSugeridos(inicio);
		if(apellidos == null) {
			throw getError("Falló el sistema subyacente", 
					        ErrorRest.ERR_FALLA_SERVICIO, 
					        Response.Status.BAD_REQUEST);
		}
		return apellidos;
		
	}
	@Path("nombres/{inicio}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<String> getNombresQueInicienCon(
			@PathParam("inicio")
			String inicio){
		bitacora.info("getNombresQueInicienCon(" + inicio + ")");
		List<String> nombres = this.servicioCatalogos.getNombresSugeridos(inicio);
		if(nombres == null) {
			throw getError("Falló el sistema subyacente", 
					        ErrorRest.ERR_FALLA_SERVICIO, 
					        Response.Status.BAD_REQUEST);
		}
		return nombres;
	}
	@Path("nombres")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<String> getNombres(){
		bitacora.info("getNombres()");
		List<String> nombres = this.servicioCatalogos.getNombresSugeridos("");
		if(nombres == null) {
			throw getError("Falló el sistema subyacente", 
					        ErrorRest.ERR_FALLA_SERVICIO, 
					        Response.Status.BAD_REQUEST);
		}
		return nombres;
	}

}
