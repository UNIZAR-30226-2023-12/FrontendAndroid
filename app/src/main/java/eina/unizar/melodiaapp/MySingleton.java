package eina.unizar.melodiaapp;

public class MySingleton {
    private static MySingleton instance;
    private String IPdir = "10.0.2.2";

    private MySingleton() {
        // Constructor privado para evitar que se creen instancias de la clase
    }

    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    public String getMyGlobalVariable() {
        return IPdir;
    }

    public void setMyGlobalVariable(String myGlobalVariable) {
        this.IPdir = myGlobalVariable;
    }
}
