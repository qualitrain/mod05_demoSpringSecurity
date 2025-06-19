package mx.com.qtx.ejmSpSec.test;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import mx.com.qtx.ejmSpSec.entidades.CatValorSimple;
import mx.com.qtx.ejmSpSec.entidades.Persona;
import mx.com.qtx.ejmSpSec.servicios.IGestorDatos;

//@Primary
@Repository
public class GestorDatosErroneo implements IGestorDatos{
	private static Logger bitacora = LoggerFactory.getLogger(GestorDatosErroneo.class);	
	
	public GestorDatosErroneo() {
		super();
		bitacora.trace("GestorDatosErroneo()");
	}

	@Override
	public Set<CatValorSimple> leerValoresCatSimple(String tipoValor) {
		bitacora.trace("leerValoresCatSimple()");
		return null;
	}

	@Override
	public Set<CatValorSimple> leerValoresCatSimpleConInicio(String tipoValor, String inicioValAlfanumerico) {
		bitacora.trace("leerValoresCatSimpleConInicio()");
		return null;
	}

	@Override
	public Set<Persona> leerPersonas() {
		bitacora.trace("leerPersonas()");
		return null;
	}

	@Override
	public Persona leerPersonaXID(long id) {
		bitacora.trace("leerPersonaXID()");
		return null;
	}

}
