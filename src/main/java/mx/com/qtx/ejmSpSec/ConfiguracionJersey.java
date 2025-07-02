package mx.com.qtx.ejmSpSec;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import mx.com.qtx.ejmSpSec.seguridad.web.AutenticacionRest;
import mx.com.qtx.ejmSpSec.web.api.jaxrs.CatalogosRest;
import mx.com.qtx.ejmSpSec.web.api.jaxrs.CorsFilter;
import mx.com.qtx.ejmSpSec.web.api.jaxrs.GenericExceptionMapper;
import mx.com.qtx.ejmSpSec.web.api.jaxrs.PersonasRest;

@Configuration
public class ConfiguracionJersey extends ResourceConfig {
	public ConfiguracionJersey() {
		this.register(CatalogosRest.class);
		this.register(PersonasRest.class);
		this.register(AutenticacionRest.class);
		this.register(CorsFilter.class);
		this.register(GenericExceptionMapper.class);
	}
}
