package tabla_simbolos;

import tipo_dato.TipoDato;

public class EntradaSimbolo {
    private String nombre;
    private TipoDato tipo;
    private int direccion;
    private Object valor;
    private int tamaño;
    private String ambito;

    public EntradaSimbolo(String nombre, TipoDato tipo, int direccion, Object valor, String ambito) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
        this.valor = valor;
        this.ambito = ambito;
        this.tamaño = calcularTamaño(tipo);
    }

    /**
     * Calcula el tamaño en bytes según el tipo de dato
     */
    private int calcularTamaño(TipoDato tipo) {
        switch (tipo) {
            case INT:
                return 4; // 4 bytes para entero
            case FLOAT:
                return 4; // 4 bytes para float
            case STRING:
                return valor.toString().length() + 8; // longitud + overhead
            default:
                return 0;
        }
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public int getDireccion() {
        return direccion;
    }

    public Object getValor() {
        return valor;
    }

    public int getTamaño() {
        return tamaño;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setValor(Object valor) {
        this.valor = valor;
        this.tamaño = calcularTamaño(tipo); // Recalcular tamaño si es string
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-8s | %-10d | %-15s | %-6s | %s",
                nombre, tipo, direccion, valor, tamaño + "B", ambito);
    }
}
