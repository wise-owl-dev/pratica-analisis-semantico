package manejo_errores_semantico;

public class ErrorSemanticoDetalle {
    private TipoError tipo;
    private String mensaje;
    private int linea;
    private int columna;
    private String contexto;

    public ErrorSemanticoDetalle(TipoError tipo, String mensaje, int linea, int columna, String contexto) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.linea = linea;
        this.columna = columna;
        this.contexto = contexto;
    }

    public ErrorSemanticoDetalle(TipoError tipo, String mensaje, String contexto) {
        this(tipo, mensaje, -1, -1, contexto);
    }

    // Getters
    public TipoError getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getContexto() {
        return contexto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ERROR SEMÁNTICO [").append(tipo).append("]");
        if (linea != -1) {
            sb.append(" Línea ").append(linea);
            if (columna != -1) {
                sb.append(", Columna ").append(columna);
            }
        }
        sb.append(": ").append(mensaje);
        if (contexto != null && !contexto.isEmpty()) {
            sb.append("\n  Contexto: ").append(contexto);
        }
        return sb.toString();
    }
}
