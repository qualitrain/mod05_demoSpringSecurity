package mx.com.qtx.ejmSpSec.servicios;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import mx.com.qtx.ejmSpSec.defServicios.IServicioCatalogos;

@Service
@Primary
public class ServicioCatalogos implements IServicioCatalogos{
	@Autowired
	IGestorDatos gestorDatos;

	public ServicioCatalogos() {
		super();
	}
	public List<String> getNombresSugeridos(String inicioNombre) {
		return Arrays.asList(this.getDatoSugerido(inicioNombre, "nombre"));
	}
	public List<String> getApellidosSugeridos(String inicioNombre) {
		return  Arrays.asList(this.getDatoSugerido(inicioNombre, "apellido"));
	}
	private String[] getDatoSugerido(String inicio, String tipoCampo) {
		return this.gestorDatos.leerValoresCatSimpleConInicio(tipoCampo, inicio)
        		.stream()
        		.map(vcs -> vcs.getValorAlfa())
        		.toArray(String[]::new);		
	}

}
