package comprobacion_de_tipos;

import java.util.*;

/**
 * Parte 2: Comprobación de Tipos
 * Sistema para detectar errores de tipos en expresiones
 */

enum TipoDato {
    INT, FLOAT, STRING, ERROR
}

public class ComprobadorTipos {
    private Map<String, Variable> variables;

    public ComprobadorTipos() {
        this.variables = new HashMap<>();
    }

    /**
     * Declara una variable con su tipo y valor
     */
    public void declararVariable(String nombre, TipoDato tipo, Object valor) throws ErrorSemantico {
        // Verificar que el valor sea compatible con el tipo
        if (!esCompatible(tipo, valor)) {
            throw new ErrorSemantico("Error: el valor " + valor + " no es compatible con el tipo " + tipo);
        }

        variables.put(nombre, new Variable(nombre, tipo, valor));
        System.out.println("Variable declarada: " + variables.get(nombre));
    }

    /**
     * Verifica si un valor es compatible con un tipo
     */
    private boolean esCompatible(TipoDato tipo, Object valor) {
        switch (tipo) {
            case INT:
                return valor instanceof Integer;
            case FLOAT:
                return valor instanceof Float || valor instanceof Double;
            case STRING:
                return valor instanceof String;
            default:
                return false;
        }
    }

    /**
     * Obtiene el tipo de una variable
     */
    public TipoDato obtenerTipo(String nombre) throws ErrorSemantico {
        if (!variables.containsKey(nombre)) {
            throw new ErrorSemantico("Error: variable '" + nombre + "' no declarada");
        }
        return variables.get(nombre).getTipo();
    }

    /**
     * Verifica si una operación entre dos tipos es válida
     */
    public TipoDato verificarOperacion(String operador, TipoDato tipo1, TipoDato tipo2) throws ErrorSemantico {
        System.out.println("Verificando operación: " + tipo1 + " " + operador + " " + tipo2);

        switch (operador) {
            case "+":
                return verificarSuma(tipo1, tipo2);
            case "-":
            case "*":
            case "/":
                return verificarAritmetica(tipo1, tipo2);
            case "==":
            case "!=":
                return TipoDato.INT; // boolean representado como int
            case "<":
            case ">":
            case "<=":
            case ">=":
                return verificarComparacion(tipo1, tipo2);
            default:
                throw new ErrorSemantico("Error: operador '" + operador + "' no reconocido");
        }
    }

    /**
     * Verifica operaciones de suma (permite concatenación de strings)
     */
    private TipoDato verificarSuma(TipoDato tipo1, TipoDato tipo2) throws ErrorSemantico {
        if (tipo1 == TipoDato.STRING || tipo2 == TipoDato.STRING) {
            if (tipo1 == TipoDato.STRING && tipo2 == TipoDato.STRING) {
                return TipoDato.STRING; // Concatenación válida
            } else {
                throw new ErrorSemantico("Error: no se puede sumar " + tipo1 + " con " + tipo2);
            }
        }

        // Operaciones numéricas
        if ((tipo1 == TipoDato.INT || tipo1 == TipoDato.FLOAT) &&
                (tipo2 == TipoDato.INT || tipo2 == TipoDato.FLOAT)) {
            if (tipo1 == TipoDato.FLOAT || tipo2 == TipoDato.FLOAT) {
                return TipoDato.FLOAT;
            } else {
                return TipoDato.INT;
            }
        }

        throw new ErrorSemantico("Error: no se puede sumar " + tipo1 + " con " + tipo2);
    }

    /**
     * Verifica operaciones aritméticas básicas
     */
    private TipoDato verificarAritmetica(TipoDato tipo1, TipoDato tipo2) throws ErrorSemantico {
        if ((tipo1 == TipoDato.INT || tipo1 == TipoDato.FLOAT) &&
                (tipo2 == TipoDato.INT || tipo2 == TipoDato.FLOAT)) {
            if (tipo1 == TipoDato.FLOAT || tipo2 == TipoDato.FLOAT) {
                return TipoDato.FLOAT;
            } else {
                return TipoDato.INT;
            }
        }

        throw new ErrorSemantico("Error: operación aritmética inválida entre " + tipo1 + " y " + tipo2);
    }

    /**
     * Verifica operaciones de comparación
     */
    private TipoDato verificarComparacion(TipoDato tipo1, TipoDato tipo2) throws ErrorSemantico {
        if ((tipo1 == TipoDato.INT || tipo1 == TipoDato.FLOAT) &&
                (tipo2 == TipoDato.INT || tipo2 == TipoDato.FLOAT)) {
            return TipoDato.INT; // boolean como int
        }

        throw new ErrorSemantico("Error: no se pueden comparar " + tipo1 + " y " + tipo2);
    }

    /**
     * Evalúa una expresión simple
     */
    public void evaluarExpresion(String var1, String operador, String var2) {
        try {
            System.out.println("\n--- Evaluando expresión: " + var1 + " " + operador + " " + var2 + " ---");

            TipoDato tipo1 = obtenerTipo(var1);
            TipoDato tipo2 = obtenerTipo(var2);

            TipoDato tipoResultado = verificarOperacion(operador, tipo1, tipo2);

            System.out.println("✓ Expresión válida. Tipo resultado: " + tipoResultado);

        } catch (ErrorSemantico e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    /**
     * Simula el uso de una variable en una expresión print
     */
    public void simularPrint(String variable) {
        try {
            System.out.println("\n--- Simulando: print(" + variable + ") ---");
            TipoDato tipo = obtenerTipo(variable);
            Variable var = variables.get(variable);
            System.out.println("✓ Salida: " + var.getValor() + " (tipo: " + tipo + ")");
        } catch (ErrorSemantico e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PARTE 2: COMPROBACIÓN DE TIPOS ===");
        System.out.println();

        ComprobadorTipos checker = new ComprobadorTipos();

        try {
            // Declarar variables de ejemplo
            System.out.println("Declarando variables:");
            checker.declararVariable("a", TipoDato.INT, 5);
            checker.declararVariable("b", TipoDato.STRING, "hola");
            checker.declararVariable("c", TipoDato.FLOAT, 3.14);
            checker.declararVariable("d", TipoDato.INT, 10);

            System.out.println("\n=== EJEMPLOS DE VERIFICACIÓN ===");

            // Ejemplo 1: Error semántico - int + string
            checker.evaluarExpresion("a", "+", "b");

            // Ejemplo 2: Operación válida - int + int
            checker.evaluarExpresion("a", "+", "d");

            // Ejemplo 3: Operación válida - int + float
            checker.evaluarExpresion("a", "*", "c");

            // Ejemplo 4: Concatenación válida - string + string
            checker.declararVariable("e", TipoDato.STRING, "mundo");
            checker.evaluarExpresion("b", "+", "e");

            // Ejemplo 5: Variable no declarada
            System.out.println("\n--- Evaluando expresión: w + a ---");
            checker.evaluarExpresion("w", "+", "a");

            // Ejemplos de print
            checker.simularPrint("a");
            checker.simularPrint("b");
            checker.simularPrint("w"); // Error: variable no declarada

        } catch (ErrorSemantico e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}