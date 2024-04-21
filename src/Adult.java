public class Adult extends Clients {
    public Adult(String nom, String apell, Integer edad_cliente, String email, boolean desemp, Integer telefono, String sexo, Double efectivo) {
        super(nom, apell, edad_cliente, email, desemp, telefono, sexo, efectivo);

        if (edad_cliente<18) {
            throw new IllegalArgumentException("El adulto debe ser mayor de edad");
        }
    }

    public void QueSoy() {
        System.out.println("Soy un adulto y soy mayor de edad...");
    }
}
