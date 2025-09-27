package pila_semantica;

import tipo_dato.TipoDato;

public class ElementoPila {
    private String valor;
    private TipoDato tipo;
    private boolean esOperador;

    // Constructor para operandos
    public ElementoPila(String valor, TipoDato tipo) {
        this.valor = valor;
        this.tipo = tipo;
        this.esOperador = false;
    }

    // Constructor para operadores
    public ElementoPila(String valor) {
        this.valor = valor;
        this.tipo = null;
        this.esOperador = true;
    }

    // Getters
    public String getValor() {
        return valor;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public boolean esOperador() {
        return esOperador;
    }

    @Override
    public String toString() {
        if (esOperador) {
            return valor + "(op)";
        } else {
            return valor + "(" + tipo + ")";
        }
    }
}
