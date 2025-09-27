package tabla_simbolos;

import java.util.*;

import tipo_dato.TipoDato;

/**
 * Parte 4: Tabla de Símbolos
 * Implementación de una tabla de símbolos con gestión de memoria simulada
 */

public class TablaSimbolos {
    private Map<String, EntradaSimbolo> tabla;
    private int siguienteDireccion;
    private String ambitoActual;

    public TablaSimbolos() {
        this.tabla = new HashMap<>();
        this.siguienteDireccion = 1000; // Dirección base
        this.ambitoActual = "global";
    }

    /**
     * Inserta una nueva variable en la tabla de símbolos
     */
    public boolean insertar(String nombre, TipoDato tipo, Object valor) {
        if (tabla.containsKey(nombre)) {
            System.err.println("Error: La variable '" + nombre + "' ya está declarada");
            return false;
        }

        // Validar compatibilidad de tipo y valor
        if (!validarTipo(tipo, valor)) {
            System.err.println("Error: El valor no es compatible con el tipo " + tipo);
            return false;
        }

        EntradaSimbolo entrada = new EntradaSimbolo(nombre, tipo, siguienteDireccion, valor, ambitoActual);
        tabla.put(nombre, entrada);

        // Actualizar la siguiente dirección disponible
        siguienteDireccion += entrada.getTamaño();

        System.out.println("✓ Variable insertada: " + entrada);
        return true;
    }

    /**
     * Busca una variable en la tabla de símbolos
     */
    public EntradaSimbolo buscar(String nombre) {
        return tabla.get(nombre);
    }

    /**
     * Verifica si una variable existe
     */
    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    /**
     * Actualiza el valor de una variable existente
     */
    public boolean actualizar(String nombre, Object nuevoValor) {
        EntradaSimbolo entrada = tabla.get(nombre);
        if (entrada == null) {
            System.err.println("Error: La variable '" + nombre + "' no está declarada");
            return false;
        }

        if (!validarTipo(entrada.getTipo(), nuevoValor)) {
            System.err.println("Error: El nuevo valor no es compatible con el tipo " + entrada.getTipo());
            return false;
        }

        Object valorAnterior = entrada.getValor();
        entrada.setValor(nuevoValor);

        System.out.println("✓ Variable actualizada: " + nombre + " = " + valorAnterior + " → " + nuevoValor);
        return true;
    }

    /**
     * Valida que un valor sea compatible con un tipo
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
     * Obtiene información de uso de memoria
     */
    public void mostrarEstadisticasMemoria() {
        int totalVariables = tabla.size();
        int memoriaUsada = siguienteDireccion - 1000;
        int memoriaLibre = 10000 - memoriaUsada; // Supongamos 10KB disponibles

        System.out.println("\n=== ESTADÍSTICAS DE MEMORIA ===");
        System.out.println("Total de variables: " + totalVariables);
        System.out.println("Memoria usada: " + memoriaUsada + " bytes");
        System.out.println("Memoria libre: " + memoriaLibre + " bytes");
        System.out.println("Dirección siguiente: " + siguienteDireccion);
        System.out.println("Fragmentación: " + calcularFragmentacion() + "%");
    }

    /**
     * Calcula un porcentaje simple de fragmentación
     */
    private double calcularFragmentacion() {
        if (tabla.isEmpty())
            return 0.0;

        // Simulación simple: fragmentación basada en diferencias de tamaño
        int totalEspacios = 0;
        for (EntradaSimbolo entrada : tabla.values()) {
            totalEspacios += entrada.getTamaño();
        }

        int memoriaUsada = siguienteDireccion - 1000;
        return ((double) (memoriaUsada - totalEspacios) / memoriaUsada) * 100;
    }

    /**
     * Muestra la tabla de símbolos en formato tabular
     */
    public void mostrarTabla() {
        System.out.println("\n=== TABLA DE SÍMBOLOS ===");
        System.out.println("Nombre     | Tipo     | Dirección  | Valor           | Tamaño | Ámbito");
        System.out.println("-----------|----------|------------|-----------------|--------|--------");

        if (tabla.isEmpty()) {
            System.out.println("(Tabla vacía)");
            return;
        }

        // Ordenar por dirección de memoria
        List<EntradaSimbolo> entradas = new ArrayList<>(tabla.values());
        entradas.sort(Comparator.comparingInt(EntradaSimbolo::getDireccion));

        for (EntradaSimbolo entrada : entradas) {
            System.out.println(entrada);
        }

        mostrarEstadisticasMemoria();
    }

    /**
     * Busca variables por tipo
     */
    public List<EntradaSimbolo> buscarPorTipo(TipoDato tipo) {
        List<EntradaSimbolo> resultado = new ArrayList<>();
        for (EntradaSimbolo entrada : tabla.values()) {
            if (entrada.getTipo() == tipo) {
                resultado.add(entrada);
            }
        }
        return resultado;
    }

    /**
     * Elimina una variable de la tabla (simulación de liberación de memoria)
     */
    public boolean eliminar(String nombre) {
        EntradaSimbolo entrada = tabla.get(nombre);
        if (entrada == null) {
            System.err.println("Error: La variable '" + nombre + "' no existe");
            return false;
        }

        tabla.remove(nombre);
        System.out.println("✓ Variable eliminada: " + nombre + " (liberados " + entrada.getTamaño() + " bytes)");
        return true;
    }

    /**
     * Cambia el ámbito actual
     */
    public void cambiarAmbito(String nuevoAmbito) {
        this.ambitoActual = nuevoAmbito;
        System.out.println("Ámbito cambiado a: " + nuevoAmbito);
    }

    /**
     * Obtiene todas las variables del ámbito actual
     */
    public List<EntradaSimbolo> obtenerVariablesAmbito(String ambito) {
        List<EntradaSimbolo> resultado = new ArrayList<>();
        for (EntradaSimbolo entrada : tabla.values()) {
            if (entrada.getAmbito().equals(ambito)) {
                resultado.add(entrada);
            }
        }
        return resultado;
    }

    public static void main(String[] args) {
        System.out.println("=== PARTE 4: TABLA DE SÍMBOLOS ===");
        System.out.println();

        TablaSimbolos tabla = new TablaSimbolos();

        System.out.println("=== INSERTANDO VARIABLES ===");

        // Insertar las variables del ejercicio
        tabla.insertar("x", TipoDato.INT, 10);
        tabla.insertar("y", TipoDato.FLOAT, 3.5f);
        tabla.insertar("z", TipoDato.STRING, "hola");

        // Insertar variables adicionales para demostrar funcionalidad
        tabla.insertar("contador", TipoDato.INT, 0);
        tabla.insertar("pi", TipoDato.FLOAT, 3.14159f);
        tabla.insertar("mensaje", TipoDato.STRING, "Bienvenido al compilador");

        // Mostrar tabla completa
        tabla.mostrarTabla();

        System.out.println("\n=== OPERACIONES ADICIONALES ===");

        // Intentar insertar variable duplicada
        tabla.insertar("x", TipoDato.FLOAT, 5.5f);

        // Actualizar valor de variable existente
        tabla.actualizar("contador", 25);
        tabla.actualizar("mensaje", "¡Hola mundo!");

        // Buscar variable específica
        System.out.println("\n--- Búsqueda de variable 'pi' ---");
        EntradaSimbolo pi = tabla.buscar("pi");
        if (pi != null) {
            System.out.println("✓ Encontrada: " + pi);
        }

        // Buscar variables por tipo
        System.out.println("\n--- Variables de tipo STRING ---");
        List<EntradaSimbolo> strings = tabla.buscarPorTipo(TipoDato.STRING);
        for (EntradaSimbolo entrada : strings) {
            System.out.println("  " + entrada);
        }

        // Demostrar manejo de ámbitos
        System.out.println("\n=== MANEJO DE ÁMBITOS ===");
        tabla.cambiarAmbito("funcion1");
        tabla.insertar("temp", TipoDato.INT, 42);
        tabla.insertar("local", TipoDato.STRING, "variable local");

        tabla.cambiarAmbito("funcion2");
        tabla.insertar("temp", TipoDato.FLOAT, 99.9f); // Mismo nombre, diferente ámbito

        // Mostrar tabla final
        tabla.mostrarTabla();

        // Mostrar variables por ámbito
        System.out.println("\n--- Variables del ámbito 'global' ---");
        List<EntradaSimbolo> globales = tabla.obtenerVariablesAmbito("global");
        for (EntradaSimbolo entrada : globales) {
            System.out.println("  " + entrada);
        }

        // Eliminar una variable
        System.out.println("\n=== LIBERACIÓN DE MEMORIA ===");
        tabla.eliminar("temp");
        tabla.mostrarTabla();
    }
}
