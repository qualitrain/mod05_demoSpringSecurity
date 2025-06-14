package mx.com.qtx.ejmSpSec.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import mx.com.qtx.ejmSpSec.defServicios.IServicioCatalogos;

@Service
@Primary
public class ServicioCatalogosErroneo implements IServicioCatalogos {

	private static Logger bitacora = LoggerFactory.getLogger(ServicioCatalogosErroneo.class);	
	
	public ServicioCatalogosErroneo() {
		super();
		bitacora.trace("ServicioCatalogosErroneo()");
	}

	@Override
	public List<String> getNombresSugeridos(String inicioNombre) {
		bitacora.trace("getNombresSugeridos()");
		bitacora.debug("inicioNombre = [" +inicioNombre + "]");
		return null;
	}

	@Override
	public List<String> getApellidosSugeridos(String inicioNombre) {
		bitacora.trace("getApellidosSugeridos()");
		bitacora.debug("inicioNombre = [" +inicioNombre + "]");
		return null;
	}

}
