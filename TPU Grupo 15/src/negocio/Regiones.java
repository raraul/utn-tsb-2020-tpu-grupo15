package negocio;

import soporte.ArchivoDeDatos;
import soporte.BaseDeDatos;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;

public class Regiones {
    private ArchivoDeDatos archivoRegiones;
    private Region pais;

    public Regiones(String path) throws FileNotFoundException
    {
        archivoRegiones = new ArchivoDeDatos(path + "\\descripcion_regiones.dsv");
        //archivoMesas = new ArchivoDeDatos(path + "\\mesas_totales_agrp_politica.dsv");
        pais = archivoRegiones.identificarRegiones();
        //archivoMesas.contarVotosPorAgrupacion(tablaHash);
    }

    public Regiones() throws SQLException, ClassNotFoundException {
        archivoRegiones = null;
        // todo
        pais = BaseDeDatos.identificarRegiones();
    }

    public Collection getDistritos() {
        return pais.getSubregiones();
    }

    public Region getPais() {
        return pais;
    }

}
