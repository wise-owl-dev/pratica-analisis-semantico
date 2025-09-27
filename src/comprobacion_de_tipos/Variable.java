package comprobacion_de_tipos;

import tipo_dato.TipoDato;

/**
 * Clase Variable actualizada
 * Representa una variable en el sistema de análisis semántico
 * Compatible con la enumeración TipoDato del paquete tipo_dato
 */
public class Variable {
    private String nombre;
    private TipoDato tipo;
    private Object valor;
    private String ambito;
    private boolean inicializada;

    /**
     * Constructor completo
     */
    public Variable(String nombre, TipoDato tipo, Object valor, String ambito) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.ambito = ambito != null ? ambito : "global";
        this.inicializada = valor != null;
    }

    /**
     * Constructor sin ámbito (usa "global" por defecto)
     */
    public Variable(String nombre, TipoDato tipo, Object valor) {
        this(nombre, tipo, valor, "global");
    }

    /**
     * Constructor para variable sin inicializar
     */
    public Variable(String nombre, TipoDato tipo) {
        this(nombre, tipo, null, "global");
        this.inicializada = false;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    public String getAmbito() {
        return ambito;
    }

    public boolean estaInicializada() {
        return inicializada;
    }

    // Setters
    public void setValor(Object valor) {
        // Validar que el valor sea compatible con el tipo usando TipoDato
        if (valor != null && !tipo.validarValor(valor)) {
            throw new IllegalArgumentException("Valor incompatible con el tipo " + tipo);
        }
        this.valor = valor;
        this.inicializada = valor != null;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito != null ? ambito : "global";
    }

    /**
     * Obtiene el valor como entero (con conversión si es necesario)
     */
    public int getValorEntero() {
        if (valor == null) {
            throw new IllegalStateException("Variable no inicializada: " + nombre);
        }

        if (valor instanceof Integer) {
            return (Integer) valor;
        } else if (valor instanceof Number) {
            return ((Number) valor).intValue();
        } else {
            throw new IllegalStateException("No se puede convertir " + valor + " a entero");
        }
    }

    /**
     * Obtiene el valor como decimal (con conversión si es necesario)
     */
    public float getValorFloat() {
        if (valor == null) {
            throw new IllegalStateException("Variable no inicializada: " + nombre);
        }

        if (valor instanceof Float) {
            return (Float) valor;
        } else if (valor instanceof Number) {
            return ((Number) valor).floatValue();
        } else {
            throw new IllegalStateException("No se puede convertir " + valor + " a float");
        }
    }

    /**
     * Obtiene el valor como cadena
     */
    public String getValorString() {
        if (valor == null) {
            return "null";
        }
        return valor.toString();
    }

    /**
     * Calcula el tamaño en memoria de la variable usando TipoDato
     */
    public int calcularTamaño() {
        return tipo.calcularTamaño(valor);
    }

    /**
     * Verifica si la variable es numérica usando TipoDato
     */
    public boolean esNumerica() {
        return tipo.esNumerico();
    }

    /**
     * Clona la variable
     */
    public Variable clonar() {
        return new Variable(nombre, tipo, valor, ambito);
    }

    /**
     * Compara dos variables por valor
     */
    public boolean tieneIgualValor(Variable otra) {
        if (otra == null || valor == null || otra.valor == null) {
            return false;
        }
        return valor.equals(otra.valor);
    }

    /**
     * Representación para debugging
     */
    public String toStringDetallado() {
        return String.format("Variable{nombre='%s', tipo=%s, valor=%s, ambito='%s', inicializada=%s}",
                nombre, tipo, valor, ambito, inicializada);
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ") = " + (valor != null ? valor : "sin inicializar");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Variable variable = (Variable) obj;
        return nombre.equals(variable.nombre) &&
                ambito.equals(variable.ambito);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode() * 31 + ambito.hashCode();
    }

    /**
     * Método de prueba para la clase Variable
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE LA CLASE VARIABLE ===");
        System.out.println();

        // Crear variables de prueba
        Variable entero = new Variable("x", TipoDato.INT, 42);
        Variable decimal = new Variable("y", TipoDato.FLOAT, 3.14f);
        Variable texto = new Variable("mensaje", TipoDato.STRING, "Hola mundo");
        Variable sinInicializar = new Variable("temp", TipoDato.INT);

        // Mostrar variables
        System.out.println("Variables creadas:");
        System.out.println("1. " + entero);
        System.out.println("2. " + decimal);
        System.out.println("3. " + texto);
        System.out.println("4. " + sinInicializar);
        System.out.println();

        // Probar métodos
        System.out.println("Información detallada:");
        System.out.println("- " + entero.getNombre() + " es numérica: " + entero.esNumerica());
        System.out.println("- " + entero.getNombre() + " tamaño: " + entero.calcularTamaño() + " bytes");
        System.out.println("- " + texto.getNombre() + " tamaño: " + texto.calcularTamaño() + " bytes");
        System.out.println("- " + sinInicializar.getNombre() + " inicializada: " + sinInicializar.estaInicializada());
        System.out.println();

        // Probar conversiones
        System.out.println("Conversiones:");
        System.out.println("- Entero como float: " + entero.getValorFloat());
        System.out.println("- Decimal como entero: " + decimal.getValorEntero());
        System.out.println("- Entero como string: " + entero.getValorString());
        System.out.println();

        // Probar modificación de valor
        System.out.println("Modificando valores:");
        System.out.println("Antes: " + entero);
        entero.setValor(100);
        System.out.println("Después: " + entero);

        // Inicializar variable sin valor
        System.out.println("Antes: " + sinInicializar);
        sinInicializar.setValor(55);
        System.out.println("Después: " + sinInicializar);
        System.out.println();

        // Probar clonación
        Variable copia = entero.clonar();
        System.out.println("Variable original: " + entero);
        System.out.println("Variable clonada: " + copia);
        System.out.println("¿Son iguales en valor? " + entero.tieneIgualValor(copia));
    }
}