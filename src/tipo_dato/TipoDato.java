package tipo_dato;

/**
 * Enumeración TipoDato
 * Define los tipos de datos soportados por el analizador semántico
 */

public enum TipoDato {
    INT("int", 4),
    FLOAT("float", 4),
    STRING("string", -1), // Tamaño variable
    BOOLEAN("boolean", 1),
    ERROR("error", 0); // Tipo especial para errores

    private final String nombre;
    private final int tamañoBase; // Tamaño en bytes (-1 para tamaño variable)

    /**
     * Constructor de la enumeración
     * 
     * @param nombre     Nombre del tipo en código fuente
     * @param tamañoBase Tamaño base en bytes
     */
    TipoDato(String nombre, int tamañoBase) {
        this.nombre = nombre;
        this.tamañoBase = tamañoBase;
    }

    /**
     * Obtiene el nombre del tipo de dato
     * 
     * @return Nombre del tipo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el tamaño base del tipo
     * 
     * @return Tamaño en bytes, -1 si es variable
     */
    public int getTamañoBase() {
        return tamañoBase;
    }

    /**
     * Verifica si el tipo es numérico
     * 
     * @return true si es INT o FLOAT
     */
    public boolean esNumerico() {
        return this == INT || this == FLOAT;
    }

    /**
     * Verifica si el tipo tiene tamaño variable
     * 
     * @return true si el tamaño depende del contenido
     */
    public boolean esTamañoVariable() {
        return tamañoBase == -1;
    }

    /**
     * Calcula el tamaño real para un valor específico
     * 
     * @param valor Valor para calcular el tamaño
     * @return Tamaño en bytes
     */
    public int calcularTamaño(Object valor) {
        switch (this) {
            case STRING:
                return valor != null ? valor.toString().length() + 8 : 8; // +8 por overhead
            case INT:
            case FLOAT:
            case BOOLEAN:
                return tamañoBase;
            case ERROR:
            default:
                return 0;
        }
    }

    /**
     * Verifica si este tipo es compatible con otro para operaciones
     * 
     * @param otro      Otro tipo a comparar
     * @param operacion Tipo de operación ("+", "-", "*", "/", "==", etc.)
     * @return true si son compatibles
     */
    public boolean esCompatibleCon(TipoDato otro, String operacion) {
        if (this == ERROR || otro == ERROR) {
            return false;
        }

        switch (operacion) {
            case "+":
                // Suma: números entre sí, strings entre sí
                return (this.esNumerico() && otro.esNumerico()) ||
                        (this == STRING && otro == STRING);

            case "-":
            case "*":
            case "/":
            case "%":
                // Operaciones aritméticas: solo números
                return this.esNumerico() && otro.esNumerico();

            case "==":
            case "!=":
                // Comparación: tipos iguales o números entre sí
                return this == otro || (this.esNumerico() && otro.esNumerico());

            case "<":
            case ">":
            case "<=":
            case ">=":
                // Comparación ordinal: solo números
                return this.esNumerico() && otro.esNumerico();

            case "&&":
            case "||":
                // Operaciones lógicas: solo booleanos
                return this == BOOLEAN && otro == BOOLEAN;

            case "=":
                // Asignación: tipos iguales o promoción numérica
                return this == otro ||
                        (this == FLOAT && otro == INT) ||
                        (this == otro);

            default:
                return false;
        }
    }

    /**
     * Determina el tipo resultado de una operación
     * 
     * @param otro      Segundo operando
     * @param operacion Operación a realizar
     * @return Tipo resultado o ERROR si incompatible
     */
    public TipoDato tipoResultado(TipoDato otro, String operacion) {
        if (!esCompatibleCon(otro, operacion)) {
            return ERROR;
        }

        switch (operacion) {
            case "+":
            case "-":
            case "*":
            case "/":
                if (this == STRING && otro == STRING) {
                    return STRING; // Concatenación
                }
                // Para operaciones numéricas, promover a FLOAT si hay uno
                return (this == FLOAT || otro == FLOAT) ? FLOAT : INT;

            case "%":
                return INT; // Módulo siempre retorna entero

            case "==":
            case "!=":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "&&":
            case "||":
                return BOOLEAN; // Comparaciones retornan boolean

            case "=":
                return this; // Asignación retorna el tipo de la variable

            default:
                return ERROR;
        }
    }

    /**
     * Convierte un string a TipoDato
     * 
     * @param tipoStr String con el nombre del tipo
     * @return TipoDato correspondiente o ERROR si no se encuentra
     */
    public static TipoDato fromString(String tipoStr) {
        for (TipoDato tipo : values()) {
            if (tipo.nombre.equalsIgnoreCase(tipoStr)) {
                return tipo;
            }
        }
        return ERROR;
    }

    /**
     * Verifica si un valor Java es compatible con este tipo
     * 
     * @param valor Valor a verificar
     * @return true si el valor es compatible
     */
    public boolean validarValor(Object valor) {
        if (valor == null) {
            return false;
        }

        switch (this) {
            case INT:
                return valor instanceof Integer;
            case FLOAT:
                return valor instanceof Float || valor instanceof Double;
            case STRING:
                return valor instanceof String;
            case BOOLEAN:
                return valor instanceof Boolean;
            case ERROR:
            default:
                return false;
        }
    }

    /**
     * Convierte un valor a este tipo si es posible
     * 
     * @param valor Valor a convertir
     * @return Valor convertido o null si no es posible
     */
    public Object convertir(Object valor) {
        if (valor == null) {
            return null;
        }

        try {
            switch (this) {
                case INT:
                    if (valor instanceof Integer)
                        return valor;
                    if (valor instanceof Number)
                        return ((Number) valor).intValue();
                    if (valor instanceof String)
                        return Integer.parseInt((String) valor);
                    break;

                case FLOAT:
                    if (valor instanceof Float)
                        return valor;
                    if (valor instanceof Double)
                        return ((Double) valor).floatValue();
                    if (valor instanceof Number)
                        return ((Number) valor).floatValue();
                    if (valor instanceof String)
                        return Float.parseFloat((String) valor);
                    break;

                case STRING:
                    return valor.toString();

                case BOOLEAN:
                    if (valor instanceof Boolean)
                        return valor;
                    if (valor instanceof String)
                        return Boolean.parseBoolean((String) valor);
                    break;
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Ejemplo de uso y testing de la enumeración
     */
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE TipoDato ===");
        System.out.println();

        // Mostrar todos los tipos
        System.out.println("Tipos soportados:");
        for (TipoDato tipo : TipoDato.values()) {
            System.out.printf("%-8s - Tamaño: %-2d bytes - Numérico: %s%n",
                    tipo.getNombre(),
                    tipo.getTamañoBase(),
                    tipo.esNumerico() ? "Sí" : "No");
        }
        System.out.println();

        // Probar compatibilidades
        System.out.println("Compatibilidades de operaciones:");
        System.out.println("INT + INT: " + INT.esCompatibleCon(INT, "+"));
        System.out.println("INT + FLOAT: " + INT.esCompatibleCon(FLOAT, "+"));
        System.out.println("INT + STRING: " + INT.esCompatibleCon(STRING, "+"));
        System.out.println("STRING + STRING: " + STRING.esCompatibleCon(STRING, "+"));
        System.out.println();

        // Probar tipos resultado
        System.out.println("Tipos resultado:");
        System.out.println("INT + INT = " + INT.tipoResultado(INT, "+"));
        System.out.println("INT + FLOAT = " + INT.tipoResultado(FLOAT, "+"));
        System.out.println("STRING + STRING = " + STRING.tipoResultado(STRING, "+"));
        System.out.println("INT < FLOAT = " + INT.tipoResultado(FLOAT, "<"));
        System.out.println();

        // Probar validación de valores
        System.out.println("Validación de valores:");
        System.out.println("INT.validarValor(42): " + INT.validarValor(42));
        System.out.println("INT.validarValor(\"hola\"): " + INT.validarValor("hola"));
        System.out.println("STRING.validarValor(\"hola\"): " + STRING.validarValor("hola"));
        System.out.println("FLOAT.validarValor(3.14): " + FLOAT.validarValor(3.14));
        System.out.println();

        // Probar conversiones
        System.out.println("Conversiones:");
        System.out.println("FLOAT.convertir(42): " + FLOAT.convertir(42));
        System.out.println("STRING.convertir(123): " + STRING.convertir(123));
        System.out.println("INT.convertir(3.14): " + INT.convertir(3.14));
    }
}
