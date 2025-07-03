package mx.com.qtx.ejmSpSec.seguridad.web;

import jakarta.servlet.http.HttpServletRequest;

public interface IExtractorTokenJwtPeticionHttp {
	boolean peticionTieneTokenValido(HttpServletRequest request);
	String getNombreUsuario(HttpServletRequest request);
}
