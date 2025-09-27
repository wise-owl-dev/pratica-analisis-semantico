package pila_semantica;

import java.util.*;

import comprobacion_de_tipos.Variable;
import tipo_dato.TipoDato;

/**
 * Parte 3: Pila Semántica
 * Simulación del procesamiento de la expresión a + b * c usando una pila
 */

public class PilaSemantica {
    private Stack<ElementoPila> pila;
    private Map<String, Variable> tablaSimbolos;
    private int paso;

    public PilaSemantica() {
        this.pila = new Stack<>();
        this.tablaSimbolos = new HashMap<>();
        this.paso = 1;
    }

    /**
     * Inicializa las variables para el ejemplo
     */
    public void inicializarVariables() {
        System.out.println("=== INICIALIZANDO VARIABLES ===");
        tablaSimbolos.put("a", new Variable("a", TipoDato.INT, 5));
        tablaSimbolos.put("b", new Variable("b", TipoDato.INT, 3));
        tablaSimbolos.put("c", new Variable("c", TipoDato.INT, 2));

        System.out.println("a = 5 (INT)");
        System.out.println("b = 3 (INT)");
        System.out.println("c = 2 (INT)");
        System.out.println();
    }

    /**
     * Procesa la expresión a + b * c considerando precedencia
     */
    public void procesarExpresion() {
        System.out.println("=== PROCESANDO EXPRESIÓN: a + b * c ===");
        System.out.println("(Considerando precedencia: * tiene mayor precedencia que +)");
        System.out.println();

        // Primero procesamos b * c (mayor precedencia)
        apilar("b");
        apilar("c");
        aplicarOperador("*");

        // Luego procesamos a + (resultado anterior)
        // Necesitamos apilar 'a' y luego el resultado de b*c
        Stack<ElementoPila> temp = new Stack<>();
        temp.push(pila.pop()); // Guardar resultado de b*c

        apilar("a");
        pila.push(temp.pop()); // Restaurar resultado de b*c
        aplicarOperador("+");

        System.out.println("=== RESULTADO FINAL ===");
        if (!pila.isEmpty()) {
            ElementoPila resultado = pila.peek();
            System.out.println("Resultado: " + resultado.getValor() + " (tipo: " + resultado.getTipo() + ")");
        }
    }

    /**
     * Simula el procesamiento lineal paso a paso para mostrar el concepto
     */
    public void procesarExpresionLineal() {
        System.out.println("\n=== SIMULACIÓN LINEAL (para mostrar conceptos) ===");
        System.out.println("Procesando token por token de izquierda a derecha:");
        paso = 1;
        pila.clear();

        // Apilar operandos y operadores en orden
        apilarToken("a");
        apilarToken("+");
        apilarToken("b");
        apilarToken("*");
        apilarToken("c");

        System.out.println("\n--- Resolviendo por precedencia ---");

        // Resolver primero la multiplicación (mayor precedencia)
        resolverOperacionEnPila("*");

        // Luego resolver la suma
        resolverOperacionEnPila("+");

        System.out.println("\n=== RESULTADO FINAL ===");
        mostrarEstadoPila();
    }

    /**
     * Apila un operando (variable)
     */
    private void apilar(String variable) {
        if (tablaSimbolos.containsKey(variable)) {
            Variable var = tablaSimbolos.get(variable);
            ElementoPila elemento = new ElementoPila(var.getValor().toString(), var.getTipo());
            pila.push(elemento);

            System.out
                    .println("Paso " + paso++ + ": Apilar operando '" + variable + "' (valor: " + var.getValor() + ")");
            mostrarEstadoPila();
        } else {
            System.err.println("Error: Variable '" + variable + "' no encontrada");
        }
    }

    /**
     * Apila un token (para simulación lineal)
     */
    private void apilarToken(String token) {
        if (esOperador(token)) {
            pila.push(new ElementoPila(token));
            System.out.println("Paso " + paso++ + ": Apilar operador '" + token + "'");
        } else if (tablaSimbolos.containsKey(token)) {
            Variable var = tablaSimbolos.get(token);
            ElementoPila elemento = new ElementoPila(var.getValor().toString(), var.getTipo());
            pila.push(elemento);
            System.out.println("Paso " + paso++ + ": Apilar operando '" + token + "' (valor: " + var.getValor() + ")");
        }
        mostrarEstadoPila();
    }

    /**
     * Aplica un operador desapilando operandos
     */
    private void aplicarOperador(String operador) {
        if (pila.size() < 2) {
            System.err.println("Error: No hay suficientes operandos para " + operador);
            return;
        }

        ElementoPila der = pila.pop();
        ElementoPila izq = pila.pop();

        System.out.println("Paso " + paso++ + ": Aplicar operador '" + operador + "'");
        System.out.println("  Desapilar: " + der.getValor() + " y " + izq.getValor());

        // Calcular resultado
        int valorIzq = Integer.parseInt(izq.getValor());
        int valorDer = Integer.parseInt(der.getValor());
        int resultado = 0;

        switch (operador) {
            case "+":
                resultado = valorIzq + valorDer;
                System.out.println("  Calcular: " + valorIzq + " + " + valorDer + " = " + resultado);
                break;
            case "*":
                resultado = valorIzq * valorDer;
                System.out.println("  Calcular: " + valorIzq + " * " + valorDer + " = " + resultado);
                break;
            case "-":
                resultado = valorIzq - valorDer;
                System.out.println("  Calcular: " + valorIzq + " - " + valorDer + " = " + resultado);
                break;
            case "/":
                if (valorDer != 0) {
                    resultado = valorIzq / valorDer;
                    System.out.println("  Calcular: " + valorIzq + " / " + valorDer + " = " + resultado);
                } else {
                    System.err.println("Error: División por cero");
                    return;
                }
                break;
        }

        // Apilar resultado
        ElementoPila elementoResultado = new ElementoPila(String.valueOf(resultado), TipoDato.INT);
        pila.push(elementoResultado);

        System.out.println("  Apilar resultado: " + resultado);
        mostrarEstadoPila();
    }

    /**
     * Resuelve una operación específica en la pila
     */
    private void resolverOperacionEnPila(String operador) {
        Stack<ElementoPila> temp = new Stack<>();
        boolean operacionRealizada = false;

        // Buscar el operador en la pila
        while (!pila.isEmpty()) {
            ElementoPila elemento = pila.pop();

            if (elemento.esOperador() && elemento.getValor().equals(operador)) {
                // Encontramos el operador, ahora necesitamos los operandos
                if (!temp.isEmpty() && !pila.isEmpty()) {
                    ElementoPila der = temp.pop(); // Operando derecho
                    ElementoPila izq = pila.pop(); // Operando izquierdo

                    System.out.println("Paso " + paso++ + ": Resolver '" + operador + "'");
                    System.out.println("  Operandos: " + izq.getValor() + " " + operador + " " + der.getValor());

                    // Calcular resultado
                    int valorIzq = Integer.parseInt(izq.getValor());
                    int valorDer = Integer.parseInt(der.getValor());
                    int resultado = operador.equals("*") ? valorIzq * valorDer : valorIzq + valorDer;

                    System.out.println("  Resultado: " + resultado);

                    // Apilar resultado
                    pila.push(new ElementoPila(String.valueOf(resultado), TipoDato.INT));

                    // Restaurar elementos temporales
                    while (!temp.isEmpty()) {
                        pila.push(temp.pop());
                    }

                    operacionRealizada = true;
                    break;
                }
            } else {
                temp.push(elemento);
            }
        }

        if (operacionRealizada) {
            mostrarEstadoPila();
        }
    }

    /**
     * Verifica si un string es un operador
     */
    private boolean esOperador(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    /**
     * Muestra el estado actual de la pila
     */
    private void mostrarEstadoPila() {
        System.out.print("  Estado de la pila: [");
        if (pila.isEmpty()) {
            System.out.println("vacía]");
        } else {
            for (int i = 0; i < pila.size(); i++) {
                System.out.print(pila.get(i));
                if (i < pila.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== PARTE 3: PILA SEMÁNTICA ===");
        System.out.println();

        PilaSemantica pila = new PilaSemantica();
        pila.inicializarVariables();
        pila.procesarExpresion();
        pila.procesarExpresionLineal();
    }
}
