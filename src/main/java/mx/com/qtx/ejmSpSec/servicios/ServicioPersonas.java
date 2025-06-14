package mx.com.qtx.ejmSpSec.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.qtx.ejmSpSec.defServicios.IServicioPersonas;
import mx.com.qtx.ejmSpSec.entidades.Persona;

@Service
public class ServicioPersonas implements IServicioPersonas {
	@Autowired
	IGestorDatos gestorDatos;
	
	public List<Persona> getPersonasTodas(){
		return new ArrayList<Persona> (this.gestorDatos.leerPersonas());
	}

	@Override
	public Persona getPersona(long id) {
		Persona persona = this.gestorDatos.leerPersonaXID(id);
		return persona;
	}

}
