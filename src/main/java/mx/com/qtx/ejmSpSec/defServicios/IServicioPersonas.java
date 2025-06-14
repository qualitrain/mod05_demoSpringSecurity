package mx.com.qtx.ejmSpSec.defServicios;

import java.util.List;

import mx.com.qtx.ejmSpSec.entidades.Persona;

public interface IServicioPersonas {
	List<Persona> getPersonasTodas();
	Persona getPersona(long id);
}
