package manejo_errores_semantico;

import java.util.*;

import tabla_simbolos.EntradaSimbolo;
import tipo_dato.TipoDato;

/**
 * Parte 5: Manejo de Errores Semánticos
 * Sistema completo para detectar y reportar errores semánticos comunes
 */

enum TipoError {
    VARIABLE_NO_DECLARADA,
    VARIABLE_YA_DECLARADA,
    TIPOS_INCOMPATIBLES,
    OPERACION_INVALIDA,
    DIVISION_POR_CERO,
    ASIGNACION_INVALIDA,
    FUNCION_NO_DECLARADA,
    ARGUMENTOS_INCORRECTOS
}

public class DetectorErroresSemanticos {
    private Map<String, EntradaSimbolo> tablaSimbolos;
    private Map<String, Funcion> tablaFunciones;
    private List<ErrorSemanticoDetalle> errores;
    private int contadorLineas;

    public DetectorErroresSemanticos() {
        this.tablaSimbolos = new HashMap<>();
        this.tablaFunciones = new HashMap<>();
        this.errores = new ArrayList<>();
        this.contadorLineas = 1;

        // Inicializar funciones predefinidas
        inicializarFuncionesPredefinidas();
    }

    /**
     * Inicializa funciones del sistema como print
     */
    private void inicializarFuncionesPredefinidas() {
        // Función print que acepta cualquier tipo
        tablaFunciones.put("print", new Funcion("print", TipoDato.INT, Arrays.asList(TipoDato.INT)));
        tablaFunciones.put("println", new Funcion("println", TipoDato.INT, Arrays.asList(TipoDato.STRING)));
    }

    /**
     * Declara una variable con validaciones semánticas
     */
    public boolean declararVariable(String nombre, TipoDato tipo, Object valor) {
        contadorLineas++;

        // Error: Variable ya declarada
        if (tablaSimbolos.containsKey(nombre)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.VARIABLE_YA_DECLARADA,
                    "La variable '" + nombre + "' ya ha sido declarada anteriormente",
                    contadorLineas, 1,
                    tipo + " " + nombre + " = " + valor + ";");
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        // Error: Tipo incompatible
        if (!validarTipo(tipo, valor)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.TIPOS_INCOMPATIBLES,
                    "El valor '" + valor + "' no es compatible con el tipo " + tipo,
                    contadorLineas, 1,
                    tipo + " " + nombre + " = " + valor + ";");
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        // Declaración exitosa
        EntradaSimbolo entrada = new EntradaSimbolo(nombre, tipo, 1000 + tablaSimbolos.size() * 4, valor, "global");
        tablaSimbolos.put(nombre, entrada);
        System.out.println("✓ Variable declarada: " + nombre + " (" + tipo + ") = " + valor);
        return true;
    }

    /**
     * Evalúa una expresión binaria con detección de errores
     */
    public boolean evaluarExpresionBinaria(String var1, String operador, String var2) {
        contadorLineas++;
        String contexto = var1 + " " + operador + " " + var2;

        // Verificar que las variables existan
        if (!verificarVariableDeclarada(var1, contexto)) {
            return false;
        }

        if (!verificarVariableDeclarada(var2, contexto)) {
            return false;
        }

        EntradaSimbolo entrada1 = tablaSimbolos.get(var1);
        EntradaSimbolo entrada2 = tablaSimbolos.get(var2);

        // Verificar compatibilidad de tipos para la operación
        if (!sonTiposCompatibles(entrada1.getTipo(), entrada2.getTipo(), operador)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.TIPOS_INCOMPATIBLES,
                    "No se puede realizar la operación '" + operador + "' entre " +
                            entrada1.getTipo() + " y " + entrada2.getTipo(),
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        // Verificar división por cero
        if (operador.equals("/") && entrada2.getValor().equals(0)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.DIVISION_POR_CERO,
                    "División por cero detectada",
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        System.out.println("✓ Expresión válida: " + contexto);
        return true;
    }

    /**
     * Simula una llamada a función con validaciones
     */
    public boolean llamarFuncion(String nombreFuncion, List<String> argumentos) {
        contadorLineas++;
        String contexto = nombreFuncion + "(" + String.join(", ", argumentos) + ")";

        // Verificar que la función existe
        if (!tablaFunciones.containsKey(nombreFuncion)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.FUNCION_NO_DECLARADA,
                    "La función '" + nombreFuncion + "' no ha sido declarada",
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        Funcion funcion = tablaFunciones.get(nombreFuncion);

        // Verificar número de argumentos (para print permitimos flexibilidad)
        if (!nombreFuncion.equals("print") && argumentos.size() != funcion.getNumParametros()) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.ARGUMENTOS_INCORRECTOS,
                    "La función '" + nombreFuncion + "' esperaba " + funcion.getNumParametros() +
                            " argumentos, pero recibió " + argumentos.size(),
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        // Verificar que los argumentos existen
        for (String argumento : argumentos) {
            if (!verificarVariableDeclarada(argumento, contexto)) {
                return false;
            }
        }

        System.out.println("✓ Llamada a función válida: " + contexto);
        return true;
    }

    /**
     * Simula asignación con validaciones
     */
    public boolean asignarVariable(String variable, Object valor) {
        contadorLineas++;
        String contexto = variable + " = " + valor;

        // Verificar que la variable existe
        if (!verificarVariableDeclarada(variable, contexto)) {
            return false;
        }

        EntradaSimbolo entrada = tablaSimbolos.get(variable);

        // Verificar compatibilidad de tipo
        if (!validarTipo(entrada.getTipo(), valor)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.ASIGNACION_INVALIDA,
                    "No se puede asignar un valor de tipo " + obtenerTipo(valor) +
                            " a una variable de tipo " + entrada.getTipo(),
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }

        entrada.setValor(valor);
        System.out.println("✓ Asignación válida: " + contexto);
        return true;
    }

    /**
     * Verifica que una variable esté declarada
     */
    private boolean verificarVariableDeclarada(String variable, String contexto) {
        if (!tablaSimbolos.containsKey(variable)) {
            ErrorSemanticoDetalle error = new ErrorSemanticoDetalle(
                    TipoError.VARIABLE_NO_DECLARADA,
                    "La variable '" + variable + "' no ha sido declarada",
                    contadorLineas, 1, contexto);
            errores.add(error);
            System.err.println("✗ " + error);
            return false;
        }
        return true;
    }

    /**
     * Verifica si dos tipos son compatibles para una operación
     */
    private boolean sonTiposCompatibles(TipoDato tipo1, TipoDato tipo2, String operador) {
        switch (operador) {
            case "+":
                // Suma: números entre sí, strings entre sí
                return (esNumerico(tipo1) && esNumerico(tipo2)) ||
                        (tipo1 == TipoDato.STRING && tipo2 == TipoDato.STRING);
            case "-":
            case "*":
            case "/":
                // Operaciones aritméticas: solo números
                return esNumerico(tipo1) && esNumerico(tipo2);
            case "==":
            case "!=":
                // Comparación: tipos compatibles
                return tipo1 == tipo2;
            case "<":
            case ">":
            case "<=":
            case ">=":
                // Comparación ordinal: solo números
                return esNumerico(tipo1) && esNumerico(tipo2);
            default:
                return false;
        }
    }

    /**
     * Verifica si un tipo es numérico
     */
    private boolean esNumerico(TipoDato tipo) {
        return tipo == TipoDato.INT || tipo == TipoDato.FLOAT;
    }

    /**
     * Valida compatibilidad entre tipo y valor
     */
    private boolean validarTipo(TipoDato tipo, Object valor) {
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
     * Obtiene el tipo de un valor
     */
    private String obtenerTipo(Object valor) {
        if (valor instanceof Integer)
            return "INT";
        if (valor instanceof Float || valor instanceof Double)
            return "FLOAT";
        if (valor instanceof String)
            return "STRING";
        return "DESCONOCIDO";
    }

    /**
     * Muestra un resumen de todos los errores encontrados
     */
    public void mostrarResumenErrores() {
        System.out.println("\n=== RESUMEN DE ERRORES SEMÁNTICOS ===");

        if (errores.isEmpty()) {
            System.out.println("✓ No se encontraron errores semánticos");
            return;
        }

        System.out.println("Total de errores encontrados: " + errores.size());
        System.out.println();

        // Agrupar errores por tipo
        Map<TipoError, Integer> conteoTipos = new HashMap<>();
        for (ErrorSemanticoDetalle error : errores) {
            conteoTipos.put(error.getTipo(), conteoTipos.getOrDefault(error.getTipo(), 0) + 1);
        }

        System.out.println("Distribución por tipo de error:");
        for (Map.Entry<TipoError, Integer> entrada : conteoTipos.entrySet()) {
            System.out.println("  " + entrada.getKey() + ": " + entrada.getValue() + " ocurrencias");
        }

        System.out.println("\nDetalles de todos los errores:");
        for (int i = 0; i < errores.size(); i++) {
            System.out.println((i + 1) + ". " + errores.get(i));
        }
    }

    /**
     * Reinicia el detector para un nuevo análisis
     */
    public void reiniciar() {
        tablaSimbolos.clear();
        errores.clear();
        contadorLineas = 1;
        inicializarFuncionesPredefinidas();
        System.out.println("Detector reiniciado para nuevo análisis");
    }

    public static void main(String[] args) {
        System.out.println("=== PARTE 5: MANEJO DE ERRORES SEMÁNTICOS ===");
        System.out.println();

        DetectorErroresSemanticos detector = new DetectorErroresSemanticos();

        System.out.println("=== SIMULACIÓN DE CÓDIGO CON ERRORES ===");
        System.out.println();

        // Ejemplo 1: Declaraciones válidas
        System.out.println("--- Declaraciones iniciales ---");
        detector.declararVariable("x", TipoDato.INT, 10);
        detector.declararVariable("y", TipoDato.FLOAT, 3.14f);
        detector.declararVariable("mensaje", TipoDato.STRING, "hola");

        System.out.println("\n--- Casos de error ---");

        // Ejemplo 2: Variable ya declarada
        detector.declararVariable("x", TipoDato.FLOAT, 5.5f);

        // Ejemplo 3: Tipo incompatible en declaración
        detector.declararVariable("numero", TipoDato.INT, "texto");

        // Ejemplo 4: Variable no declarada en expresión
        detector.evaluarExpresionBinaria("w", "+", "x");

        // Ejemplo 5: Tipos incompatibles en operación (clásico int + string)
        detector.evaluarExpresionBinaria("x", "+", "mensaje");

        // Ejemplo 6: División por cero
        detector.declararVariable("cero", TipoDato.INT, 0);
        detector.evaluarExpresionBinaria("x", "/", "cero");

        // Ejemplo 7: Operación válida
        detector.evaluarExpresionBinaria("x", "*", "y");

        // Ejemplo 8: Función no declarada
        detector.llamarFuncion("calcular", Arrays.asList("x", "y"));

        // Ejemplo 9: Variable no declarada en función
        detector.llamarFuncion("print", Arrays.asList("w"));

        // Ejemplo 10: Llamada válida a print
        detector.llamarFuncion("print", Arrays.asList("x"));

        // Ejemplo 11: Asignación inválida
        detector.asignarVariable("x", "texto");

        // Ejemplo 12: Asignación válida
        detector.asignarVariable("x", 25);

        // Mostrar resumen final
        detector.mostrarResumenErrores();

        // Demostración adicional: análisis de código limpio
        System.out.println("\n" + "=".repeat(50));
        System.out.println("=== ANÁLISIS DE CÓDIGO SIN ERRORES ===");
        detector.reiniciar();

        detector.declararVariable("a", TipoDato.INT, 5);
        detector.declararVariable("b", TipoDato.INT, 3);
        detector.declararVariable("resultado", TipoDato.INT, 0);
        detector.evaluarExpresionBinaria("a", "+", "b");
        detector.asignarVariable("resultado", 8);
        detector.llamarFuncion("print", Arrays.asList("resultado"));

        detector.mostrarResumenErrores();
    }
}
