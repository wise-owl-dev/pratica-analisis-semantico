package arbol_expresiones;

/**
 * Parte 1: Construcción de Árboles de Expresiones
 * Implementación de un árbol de expresiones para (3 + 5) * (10 - 2)
 */

public class NodoExpresion {
    private String valor;
    private NodoExpresion izquierdo;
    private NodoExpresion derecho;

    // Constructor para operadores
    public NodoExpresion(String valor, NodoExpresion izquierdo, NodoExpresion derecho) {
        this.valor = valor;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
    }

    // Constructor para operandos (números)
    public NodoExpresion(String valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
    }

    // Getters
    public String getValor() {
        return valor;
    }

    public NodoExpresion getIzquierdo() {
        return izquierdo;
    }

    public NodoExpresion getDerecho() {
        return derecho;
    }

    // Método para verificar si es un operador
    public boolean esOperador() {
        return valor.equals("+") || valor.equals("-") || valor.equals("*") || valor.equals("/");
    }

    // Método para evaluar la expresión en postorden
    public double evaluar() {
        // Si es una hoja (número), retornar su valor
        if (!esOperador()) {
            return Double.parseDouble(valor);
        }

        // Si es un operador, evaluar recursivamente
        double valorIzquierdo = izquierdo.evaluar();
        double valorDerecho = derecho.evaluar();

        switch (valor) {
            case "+":
                return valorIzquierdo + valorDerecho;
            case "-":
                return valorIzquierdo - valorDerecho;
            case "*":
                return valorIzquierdo * valorDerecho;
            case "/":
                if (valorDerecho == 0) {
                    throw new ArithmeticException("División por cero");
                }
                return valorIzquierdo / valorDerecho;
            default:
                throw new IllegalArgumentException("Operador no válido: " + valor);
        }
    }

    // Método para mostrar el recorrido postorden
    public void postorden() {
        if (izquierdo != null) {
            izquierdo.postorden();
        }
        if (derecho != null) {
            derecho.postorden();
        }
        System.out.print(valor + " ");
    }

    // Método para mostrar el árbol de forma visual
    public void mostrarArbol(String prefijo, boolean esUltimo) {
        System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + valor);

        if (izquierdo != null || derecho != null) {
            if (derecho != null) {
                derecho.mostrarArbol(prefijo + (esUltimo ? "    " : "│   "), izquierdo == null);
            }
            if (izquierdo != null) {
                izquierdo.mostrarArbol(prefijo + (esUltimo ? "    " : "│   "), true);
            }
        }
    }
}
