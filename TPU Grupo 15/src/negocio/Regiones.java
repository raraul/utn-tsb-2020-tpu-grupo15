package negocio;

import soporte.ArchivoDeDatos;

import java.util.Collection;

public class Regiones {
    private ArchivoDeDatos archivoRegiones;
    private Region pais;

    public Regiones(String path) {
        archivoRegiones = new ArchivoDeDatos(path + "\\descripcion_regiones.dsv");
        //archivoMesas = new ArchivoDeDatos(path + "\\mesas_totales_agrp_politica.dsv");
        pais = archivoRegiones.identificarRegiones();
        //archivoMesas.contarVotosPorAgrupacion(tablaHash);
    }

    public Collection getDistritos() {
        return pais.getSubregiones();
    }

    public Region getPais() {
        return pais;
    }

}
