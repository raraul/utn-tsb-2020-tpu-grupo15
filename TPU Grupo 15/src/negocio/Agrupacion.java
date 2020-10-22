package negocio;

public class Agrupacion {
    private String codigo;
    private String nombre;

    public Agrupacion(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}
