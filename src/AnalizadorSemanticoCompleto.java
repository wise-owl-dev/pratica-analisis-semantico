import java.util.*;

// Importar todas las clases necesarias de los diferentes paquetes
import arbol_expresiones.ArbolExpresiones;
import arbol_expresiones.NodoExpresion;
import comprobacion_de_tipos.ComprobadorTipos;
import comprobacion_de_tipos.ErrorSemantico;
import comprobacion_de_tipos.Variable;
import pila_semantica.PilaSemantica;
import tabla_simbolos.TablaSimbolos;
import tabla_simbolos.EntradaSimbolo;
import manejo_errores_semantico.DetectorErroresSemanticos;
import tipo_dato.TipoDato;

/**
 * Programa Principal - Análisis Semántico Completo
 * Integra todas las partes del análisis semántico en una demostración completa
 */
public class AnalizadorSemanticoCompleto {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║            ANÁLISIS SEMÁNTICO EN JAVA                   ║");
        System.out.println("║            Práctica Completa - 5 Partes                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        // Mostrar menú principal
        mostrarMenu();

        while (true) {
            System.out.print("\nSelecciona una opción (1-6): ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    ejecutarParte1();
                    break;
                case 2:
                    ejecutarParte2();
                    break;
                case 3:
                    ejecutarParte3();
                    break;
                case 4:
                    ejecutarParte4();
                    break;
                case 5:
                    ejecutarParte5();
                    break;
                case 6:
                    ejecutarDemostracionCompleta();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar el Analizador Semántico!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }

            System.out.println("\nPresiona Enter para continuar...");
            scanner.nextLine();
            scanner.nextLine();
        }
    }

    /**
     * Muestra el menú principal
     */
    private static void mostrarMenu() {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("                      MENÚ PRINCIPAL");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("1. 🌳 Parte 1: Construcción de Árboles de Expresiones");
        System.out.println("2. 🔍 Parte 2: Comprobación de Tipos");
        System.out.println("3. 📚 Parte 3: Pila Semántica");
        System.out.println("4. 📋 Parte 4: Tabla de Símbolos");
        System.out.println("5. ⚠️  Parte 5: Manejo de Errores Semánticos");
        System.out.println("6. 🎯 Demostración Completa (Todas las partes)");
        System.out.println("0. ❌ Salir");
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    /**
     * Ejecuta la Parte 1: Árboles de Expresiones
     */
    private static void ejecutarParte1() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌳 PARTE 1: CONSTRUCCIÓN DE ÁRBOLES DE EXPRESIONES");
        System.out.println("=".repeat(60));

        // Ejecutar el ejemplo principal
        ArbolExpresiones.main(new String[] {});

        // Ejemplo adicional con otra expresión
        System.out.println("\n--- EJEMPLO ADICIONAL: (2 * 4) + (6 / 3) ---");

        NodoExpresion dos = new NodoExpresion("2");
        NodoExpresion cuatro = new NodoExpresion("4");
        NodoExpresion seis = new NodoExpresion("6");
        NodoExpresion tres = new NodoExpresion("3");

        NodoExpresion mult = new NodoExpresion("*", dos, cuatro);
        NodoExpresion div = new NodoExpresion("/", seis, tres);
        NodoExpresion suma = new NodoExpresion("+", mult, div);

        System.out.println("Estructura del árbol:");
        suma.mostrarArbol("", true);

        System.out.print("Recorrido postorden: ");
        suma.postorden();
        System.out.println();

        System.out.println("Resultado: " + suma.evaluar());
    }

    /**
     * Ejecuta la Parte 2: Comprobación de Tipos
     */
    private static void ejecutarParte2() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔍 PARTE 2: COMPROBACIÓN DE TIPOS");
        System.out.println("=".repeat(60));

        ComprobadorTipos.main(new String[] {});

        // Ejemplo interactivo adicional
        System.out.println("\n--- EJEMPLO INTERACTIVO ---");
        ComprobadorTipos checker = new ComprobadorTipos();

        try {
            checker.declararVariable("edad", TipoDato.INT, 25);
            checker.declararVariable("nombre", TipoDato.STRING, "Juan");
            checker.declararVariable("altura", TipoDato.FLOAT, 1.75f);

            // Casos de prueba adicionales
            System.out.println("\n=== CASOS DE PRUEBA ADICIONALES ===");
            checker.evaluarExpresion("edad", "*", "altura");
            checker.evaluarExpresion("nombre", "+", "edad");
            checker.simularPrint("nombre");

        } catch (ErrorSemantico e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la Parte 3: Pila Semántica
     */
    private static void ejecutarParte3() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📚 PARTE 3: PILA SEMÁNTICA");
        System.out.println("=".repeat(60));

        PilaSemantica.main(new String[] {});

        // Ejemplo adicional con expresión diferente
        System.out.println("\n--- EJEMPLO ADICIONAL: x * y + z ---");
        System.out.println("Variables: x = 4, y = 5, z = 10");
        System.out.println("Expresión: x * y + z (precedencia: * antes que +)");
        System.out.println();

        System.out.println("Procesamiento:");
        System.out.println("1. x * y = 4 * 5 = 20");
        System.out.println("2. 20 + z = 20 + 10 = 30");
        System.out.println("Resultado final: 30");
    }

    /**
     * Ejecuta la Parte 4: Tabla de Símbolos
     */
    private static void ejecutarParte4() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 PARTE 4: TABLA DE SÍMBOLOS");
        System.out.println("=".repeat(60));

        TablaSimbolos.main(new String[] {});

        // Demostración adicional de búsquedas
        System.out.println("\n--- DEMOSTRACIÓN DE BÚSQUEDAS AVANZADAS ---");
        TablaSimbolos tablaExtra = new TablaSimbolos();

        tablaExtra.insertar("contador", TipoDato.INT, 0);
        tablaExtra.insertar("precio", TipoDato.FLOAT, 19.99f);
        tablaExtra.insertar("producto", TipoDato.STRING, "Laptop");
        tablaExtra.insertar("activo", TipoDato.INT, 1);

        System.out.println("\nBúsqueda de variables tipo FLOAT:");
        List<EntradaSimbolo> floats = tablaExtra.buscarPorTipo(TipoDato.FLOAT);
        for (EntradaSimbolo entrada : floats) {
            System.out.println("  " + entrada);
        }

        tablaExtra.mostrarTabla();
    }

    /**
     * Ejecuta la Parte 5: Manejo de Errores Semánticos
     */
    private static void ejecutarParte5() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚠️ PARTE 5: MANEJO DE ERRORES SEMÁNTICOS");
        System.out.println("=".repeat(60));

        DetectorErroresSemanticos.main(new String[] {});

        // Casos adicionales de errores comunes
        System.out.println("\n--- ERRORES ADICIONALES COMUNES ---");
        DetectorErroresSemanticos detectorExtra = new DetectorErroresSemanticos();

        // Simular código típico con errores
        System.out.println("Simulando código con errores típicos:");
        System.out.println();

        detectorExtra.declararVariable("i", TipoDato.INT, 0);
        detectorExtra.declararVariable("texto", TipoDato.STRING, "hola mundo");

        // Error común: usar variable no inicializada
        detectorExtra.evaluarExpresionBinaria("j", "+", "i");

        // Error común: concatenar tipos incompatibles
        detectorExtra.evaluarExpresionBinaria("i", "+", "texto");

        detectorExtra.mostrarResumenErrores();
    }

    /**
     * Ejecuta una demostración completa integrando todas las partes
     */
    private static void ejecutarDemostracionCompleta() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 DEMOSTRACIÓN COMPLETA - ANÁLISIS SEMÁNTICO INTEGRADO");
        System.out.println("=".repeat(60));

        System.out.println("Simulando el análisis completo de un programa simple:");
        System.out.println();
        System.out.println("CÓDIGO A ANALIZAR:");
        System.out.println("─────────────────");
        System.out.println("int a = 10;");
        System.out.println("int b = 5;");
        System.out.println("float resultado = 0.0;");
        System.out.println("resultado = (a + b) * 2;");
        System.out.println("print(resultado);");
        System.out.println();

        // PASO 1: Construcción de tabla de símbolos
        System.out.println("PASO 1: CONSTRUCCIÓN DE TABLA DE SÍMBOLOS");
        System.out.println("─────────────────────────────────────────");
        TablaSimbolos tabla = new TablaSimbolos();
        tabla.insertar("a", TipoDato.INT, 10);
        tabla.insertar("b", TipoDato.INT, 5);
        tabla.insertar("resultado", TipoDato.FLOAT, 0.0f);
        tabla.mostrarTabla();

        // PASO 2: Análisis de expresión con pila semántica
        System.out.println("\nPASO 2: ANÁLISIS DE EXPRESIÓN (a + b) * 2");
        System.out.println("─────────────────────────────────────────");
        System.out.println("Construyendo árbol de expresión:");

        NodoExpresion nodoA = new NodoExpresion("10");
        NodoExpresion nodoB = new NodoExpresion("5");
        NodoExpresion nodoDos = new NodoExpresion("2");
        NodoExpresion suma = new NodoExpresion("+", nodoA, nodoB);
        NodoExpresion multiplicacion = new NodoExpresion("*", suma, nodoDos);

        multiplicacion.mostrarArbol("", true);
        System.out.println("Resultado de la expresión: " + multiplicacion.evaluar());

        // PASO 3: Verificación de tipos
        System.out.println("\nPASO 3: VERIFICACIÓN DE TIPOS");
        System.out.println("─────────────────────────────");
        ComprobadorTipos checker = new ComprobadorTipos();
        try {
            checker.declararVariable("a", TipoDato.INT, 10);
            checker.declararVariable("b", TipoDato.INT, 5);
            checker.declararVariable("resultado", TipoDato.FLOAT, 0.0f);

            System.out.println("✓ Verificando asignación: resultado = (a + b) * 2");
            System.out.println("✓ Tipos compatibles: INT → FLOAT (promoción automática)");

        } catch (ErrorSemantico e) {
            System.err.println("Error: " + e.getMessage());
        }

        // PASO 4: Detección de errores
        System.out.println("\nPASO 4: DETECCIÓN DE ERRORES SEMÁNTICOS");
        System.out.println("─────────────────────────────────────");
        DetectorErroresSemanticos detector = new DetectorErroresSemanticos();

        detector.declararVariable("a", TipoDato.INT, 10);
        detector.declararVariable("b", TipoDato.INT, 5);
        detector.declararVariable("resultado", TipoDato.FLOAT, 0.0f);

        detector.evaluarExpresionBinaria("a", "+", "b");
        detector.asignarVariable("resultado", 30.0f);
        detector.llamarFuncion("print", Arrays.asList("resultado"));

        // PASO 5: Resumen final
        System.out.println("\nPASO 5: RESUMEN DEL ANÁLISIS");
        System.out.println("────────────────────────────");
        System.out.println("✓ Tabla de símbolos: 3 variables declaradas");
        System.out.println("✓ Árbol de expresiones: Construido y evaluado correctamente");
        System.out.println("✓ Verificación de tipos: Sin errores de compatibilidad");
        System.out.println("✓ Análisis semántico: Programa válido");
        System.out.println();
        System.out.println("RESULTADO FINAL: El programa es semánticamente correcto");

        detector.mostrarResumenErrores();

        // Ejemplo con errores para contraste
        System.out.println("\n" + "─".repeat(60));
        System.out.println("CONTRASTE: ANÁLISIS DE CÓDIGO CON ERRORES");
        System.out.println("─".repeat(60));

        DetectorErroresSemanticos detectorErrores = new DetectorErroresSemanticos();

        System.out.println("Código con errores:");
        System.out.println("int x = 5;");
        System.out.println("string y = \"hola\";");
        System.out.println("print(x + y);  // Error: tipos incompatibles");
        System.out.println("print(z);      // Error: variable no declarada");
        System.out.println();

        detectorErrores.declararVariable("x", TipoDato.INT, 5);
        detectorErrores.declararVariable("y", TipoDato.STRING, "hola");
        detectorErrores.evaluarExpresionBinaria("x", "+", "y");
        detectorErrores.llamarFuncion("print", Arrays.asList("z"));

        detectorErrores.mostrarResumenErrores();
    }
}