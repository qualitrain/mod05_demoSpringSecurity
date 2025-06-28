package mx.com.qtx.ejmSpSec.seguridad.servicios;

import java.util.Date;
import java.util.Map;

public interface IGeneradorTokensJWT {
	void setPeriodoExpiracionHoras(int horas);
	void setPeriodoExpiracionMinutos(int min);
	String generarToken(String nombreUsuario);
	String generarToken(String nombreUsuario, Map<String,Object> mapClaims);
	String generarToken(String nombreUsuario, Map<String, Object> mapClaims, long milisDuracion);
	String extraerUsuario(String token);
	Date extraerExpiracion(String token);
	boolean tokenExpirado(String token);
	boolean tokenValido(String tokenFirmado, String nombreUsuario);
	String extraerContenidoJwtTokenSinFirmarStr(String tokenSinFirma);
	String extraerContenidoTokenFirmadoStr(String tokenFirmado);
	<R> R extraerCampo(String tokenFirmado, Class<R> tipoJavaCampo, String campo);
	String getLlaveBase64();
}
