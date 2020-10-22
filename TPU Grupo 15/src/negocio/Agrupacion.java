package negocio;

public class Agrupacion {
    private String codigo;
    private String nombre;
    private int votos;

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

    public void sumarVotos(int votosASumar) {
        this.votos += votosASumar;
    }

    @Override
    public String toString() {
        return "Agrupacion{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", votos=" + votos +
                '}';
    }
}
