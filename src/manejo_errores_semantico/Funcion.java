package manejo_errores_semantico;

import java.util.ArrayList;
import java.util.List;

import tipo_dato.TipoDato;

public class Funcion {
    private String nombre;
    private TipoDato tipoRetorno;
    private List<TipoDato> tiposParametros;

    public Funcion(String nombre, TipoDato tipoRetorno, List<TipoDato> tiposParametros) {
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.tiposParametros = tiposParametros != null ? tiposParametros : new ArrayList<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipoRetorno() {
        return tipoRetorno;
    }

    public List<TipoDato> getTiposParametros() {
        return tiposParametros;
    }

    public int getNumParametros() {
        return tiposParametros.size();
    }
}