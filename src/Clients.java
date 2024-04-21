public abstract class Clients {
    private String nombre = "";
    private String apellido = "";
    private Integer edad;
    private String correo = "";
    private boolean desempleado;
    private Integer tlf;
    private String genero;
    private String banco = null; // no tiene cuenta bancaria
    private Double cant_eur = 0.0;

    public Clients(String nom, String apell, Integer edad_cliente, String email, boolean desemp, Integer telefono, String sexo, Double efectivo) {
        this.nombre = nom;
        this.apellido = apell;
        this.edad = edad_cliente;
        this.correo = email;
        this.desempleado = desemp;
        this.tlf = telefono;
        this.genero = sexo;
        this.cant_eur = efectivo;
    }

    public void IngresarDineroCartera(Double cant) {
        this.cant_eur+=cant;
    }

    public void SacarDineroCartera(Double cant) {
        this.cant_eur-=cant;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", correo=" + correo + ", desempleado=" + desempleado + ", tlf=" + tlf + ", genero=" + genero + ", banco=" + banco + ", cant_eur=" + cant_eur + "]";
    }

    public abstract void QueSoy();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isDesempleado() {
        return desempleado;
    }

    public void setDesempleado(boolean desempleado) {
        this.desempleado = desempleado;
    }

    public Integer getTlf() {
        return tlf;
    }

    public void setTlf(Integer tlf) {
        this.tlf = tlf;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Double getCant_eur() {
        return cant_eur;
    }

    public void setCant_eur(Double cant_eur) {
        this.cant_eur = cant_eur;
    }

    
    
}
