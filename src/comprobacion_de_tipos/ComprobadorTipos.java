package comprobacion_de_tipos;

import java.util.*;
import tipo_dato.TipoDato;

/**
 * Parte 2: Comprobación de Tipos
 * Sistema para detectar errores de tipos en expresiones
 * Actualizado para usar TipoDato del paquete tipo_dato
 */

public class ComprobadorTipos {
    private Map<String, Variable> variables;

    public ComprobadorTipos() {
        this.variables = new HashMap<>();
    }

    /**
     * Declara una variable con su tipo y valor
     */
    public void declararVariable(String nombre, TipoDato tipo, Object valor) throws ErrorSemantico {
        // Verificar que el valor sea compatible con el tipo usando TipoDato
        if (!tipo.validarValor(valor)) {
            throw new ErrorSemantico("Error: el valor " + valor + " no es compatible con el tipo " + tipo);
        }

        variables.put(nombre, new Variable(nombre, tipo, valor));
        System.out.println("Variable declarada: " + variables.get(nombre));
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

        // Usar el método esCompatibleCon de TipoDato
        if (!tipo1.esCompatibleCon(tipo2, operador)) {
            throw new ErrorSemantico("Error: no se puede realizar la operación '" + operador +
                    "' entre " + tipo1 + " y " + tipo2);
        }

        // Usar el método tipoResultado de TipoDato
        TipoDato resultado = tipo1.tipoResultado(tipo2, operador);
        if (resultado == TipoDato.ERROR) {
            throw new ErrorSemantico("Error: operación '" + operador + "' inválida entre " +
                    tipo1 + " y " + tipo2);
        }

        return resultado;
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

    /**
     * Evalúa una expresión con valores literales
     */
    public void evaluarExpresionConLiterales(TipoDato tipo1, Object valor1, String operador,
            TipoDato tipo2, Object valor2) {
        try {
            System.out.println("\n--- Evaluando: " + valor1 + "(" + tipo1 + ") " + operador +
                    " " + valor2 + "(" + tipo2 + ") ---");

            TipoDato tipoResultado = verificarOperacion(operador, tipo1, tipo2);
            System.out.println("✓ Expresión válida. Tipo resultado: " + tipoResultado);

        } catch (ErrorSemantico e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las variables declaradas
     */
    public Map<String, Variable> obtenerVariables() {
        return new HashMap<>(variables);
    }

    /**
     * Verifica si una variable existe
     */
    public boolean existeVariable(String nombre) {
        return variables.containsKey(nombre);
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
            checker.declararVariable("c", TipoDato.FLOAT, 3.14f);
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

            // Ejemplos con literales
            System.out.println("\n=== EJEMPLOS CON LITERALES ===");
            checker.evaluarExpresionConLiterales(TipoDato.INT, 5, "+", TipoDato.STRING, "hola");
            checker.evaluarExpresionConLiterales(TipoDato.FLOAT, 3.14f, "*", TipoDato.INT, 2);
            checker.evaluarExpresionConLiterales(TipoDato.STRING, "Hello", "+", TipoDato.STRING, "World");

        } catch (ErrorSemantico e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}