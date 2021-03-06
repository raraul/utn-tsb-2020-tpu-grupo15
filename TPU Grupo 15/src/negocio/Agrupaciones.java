package negocio;

import soporte.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;

public class Agrupaciones {
    private static TSB_OAHashtable tablaHashInicial;
    private TSB_OAHashtable tablaHashConteo;

    public Agrupaciones() {
        tablaHashConteo = new TSB_OAHashtable();
        for (Object o : tablaHashInicial.values()) {
            Agrupacion a = (Agrupacion) o;
            tablaHashConteo.put(a.getCodigo(), new Agrupacion(a.getCodigo(), a.getNombre()));
        }
    }

    public static void leerAgrupaciones(String path) throws FileNotFoundException {
        ArchivoDeDatos archivoAgrupaciones = new ArchivoDeDatos(path + "\\descripcion_postulaciones.dsv");
        tablaHashInicial = archivoAgrupaciones.identificarAgrupacion();
    }

    public static void leerAgrupaciones() {
        try {
            tablaHashInicial = BaseDeDatos.identificarAgrupacion();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al intentar hacer conexión con la base de datos.");
            e.printStackTrace();
        }
    }

    public Agrupacion getAgrupacion(String codAgrupacion) {
        return (Agrupacion) tablaHashConteo.get(codAgrupacion);
    }

    public Collection getResultados() {
        return tablaHashConteo.values();
    }

    //@Override
    //public String toString() {
    //    StringBuilder sb = new StringBuilder();
    //    for (Object o : tablaHash.values()) {
    //        sb.append("\n").append(o);
    //    }
    //    return sb.toString();
    //}

    //@Override
    //public String toString() {
    //    return String.valueOf(tablaHash);
    //}


}
