

public class App {

    // usuario y contraseña de MySQL
    private static final String USUARIO_DB = "root";
    private static final String CONTRASENA_DB = "root";
    private static final String NOMBRE_DB = "bank_db";

    public static void main(String[] args) {
        Model obj_modelo = Model.CrearInstancia(USUARIO_DB, CONTRASENA_DB, NOMBRE_DB);
        Model obj_modelo2 = Model.CrearInstancia(USUARIO_DB, CONTRASENA_DB, "bank_db2");

        System.out.println(obj_modelo2.getNOMBRE_DB());
        System.out.println(obj_modelo.getNOMBRE_DB());

        Bank b1 = new Bank(obj_modelo, "BBVA");
        Bank b2 = new Bank(obj_modelo2, "Santander");

        Adult a1 = new Adult("Esteban", "Rodriguez", 23, "esteban@gmail.com", true, 734732732, "M", 23.65);
        Adult a2 = new Adult("Robert", "Connolly", 20, "rob@gmail.com", true, 651654347, "M", 2.65);
        Adult a3 = new Adult("Marty", "Ramis", 24, "marty@gmail.com", true, 7653462, "M", 28.65);

        b1.AñadirCliente(a1);
        b2.AñadirCliente(a2);
        b2.AñadirCliente(a3);

        b1.InsertarClientes();
        b2.InsertarClientes();

        b1.EnviarBizum(734732732, 7653462, b2, 10.12);

    }


}