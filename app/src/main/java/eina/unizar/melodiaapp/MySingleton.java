package eina.unizar.melodiaapp;

/**
 * Clase que codifica la IP del servidor
 */
public class MySingleton {
    private static MySingleton instance;
    private String IPdir = "ec2-44-204-175-208.compute-1.amazonaws.com";

    /**
     * Constructor privado para evitar que se creen instancias de la clase
     */
    private MySingleton() {
        // Constructor privado para evitar que se creen instancias de la clase
    }

    /**
     * Método que devuelve la instancia de la clase
     * @return instancia de la clase
     */
    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    /**
     * Método que devuelve la IP del servidor
     * @return IP del servidor
     */
    public String getMyGlobalVariable() {
        return IPdir;
    }

    /**
     * Método que establece la IP del servidor
     * @param myGlobalVariable IP del servidor
     */
    public void setMyGlobalVariable(String myGlobalVariable) {
        this.IPdir = myGlobalVariable;
    }
}
