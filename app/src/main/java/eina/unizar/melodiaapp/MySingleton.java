package eina.unizar.melodiaapp;

public class MySingleton {
    private static MySingleton instance;
    private String IPdir = "ec2-3-83-121-162.compute-1.amazonaws.com";

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
