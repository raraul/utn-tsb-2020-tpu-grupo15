package negocio;

import soporte.*;
import java.util.Collection;

public class Agrupaciones {
    private static TSBHashtable tablaHashInicial;
    private TSBHashtable tablaHashConteo;

    public Agrupaciones() {
        tablaHashConteo = new TSBHashtable();
        for (Object o: tablaHashInicial.values()) {
            Agrupacion a = (Agrupacion) o;
            tablaHashConteo.put(a.getCodigo(), new Agrupacion(a.getCodigo(),a.getNombre()));
        }
    }

    public static void leerAgrupaciones(String path)
    {
        ArchivoDeDatos archivoAgrupaciones = new ArchivoDeDatos(path + "\\descripcion_postulaciones.dsv");
        tablaHashInicial = archivoAgrupaciones.identificarAgrupacion();
    }

    public Agrupacion getAgrupacion(String codAgrupacion)
    {
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
