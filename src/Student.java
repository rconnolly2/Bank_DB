public class Student extends Clients {
    public Student(String nom, String apell, Integer edad_cliente, String email, boolean desemp, Integer telefono, String sexo, Double efectivo) {
        super(nom, apell, edad_cliente, email, desemp, telefono, sexo, efectivo);

        if (desemp==false) {
            throw new IllegalArgumentException("El estudiante debe estar desempleado");
        }
    }

    public void QueSoy() {
        System.out.println("Soy un Estudiante :)");
    }
}
