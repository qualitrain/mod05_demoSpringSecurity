package mx.com.qtx.ejmSpSec.defServicios;

import java.util.List;

public interface IServicioCatalogos {
	List<String> getNombresSugeridos(String inicioNombre);
	List<String> getApellidosSugeridos(String inicioNombre);
}
