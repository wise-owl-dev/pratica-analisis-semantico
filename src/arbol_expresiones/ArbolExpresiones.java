package arbol_expresiones;

public class ArbolExpresiones {

    /**
     * Construye el árbol para la expresión (3 + 5) * (10 - 2)
     */
    public static NodoExpresion construirArbol() {
        // Crear nodos hoja (números)
        NodoExpresion tres = new NodoExpresion("3");
        NodoExpresion cinco = new NodoExpresion("5");
        NodoExpresion diez = new NodoExpresion("10");
        NodoExpresion dos = new NodoExpresion("2");

        // Crear subárboles para las operaciones
        NodoExpresion suma = new NodoExpresion("+", tres, cinco); // 3 + 5
        NodoExpresion resta = new NodoExpresion("-", diez, dos); // 10 - 2

        // Crear el nodo raíz para la multiplicación
        NodoExpresion multiplicacion = new NodoExpresion("*", suma, resta); // (3 + 5) * (10 - 2)

        return multiplicacion;
    }

    public static void main(String[] args) {
        System.out.println("=== PARTE 1: ÁRBOL DE EXPRESIONES ===");
        System.out.println("Expresión: (3 + 5) * (10 - 2)");
        System.out.println();

        // Construir el árbol
        NodoExpresion raiz = construirArbol();

        // Mostrar estructura del árbol
        System.out.println("Estructura del árbol:");
        raiz.mostrarArbol("", true);
        System.out.println();

        // Mostrar recorrido postorden
        System.out.print("Recorrido postorden: ");
        raiz.postorden();
        System.out.println();
        System.out.println();

        // Evaluar la expresión
        try {
            double resultado = raiz.evaluar();
            System.out.println("Evaluación paso a paso:");
            System.out.println("1. 3 + 5 = 8");
            System.out.println("2. 10 - 2 = 8");
            System.out.println("3. 8 * 8 = 64");
            System.out.println();
            System.out.println("Resultado final: " + resultado);
        } catch (Exception e) {
            System.err.println("Error al evaluar: " + e.getMessage());
        }
    }
}