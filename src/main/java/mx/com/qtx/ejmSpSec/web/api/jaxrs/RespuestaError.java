package mx.com.qtx.ejmSpSec.web.api.jaxrs;
import java.util.*;

// 1. Clase para respuestas de error estandarizadas
public class RespuestaError {
    private int status;
    private String message;
    private List<String> errors;

    public RespuestaError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RespuestaError(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }

    // Getters (necesarios para la serialización JSON)
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<String> getErrors() { return errors; }
}