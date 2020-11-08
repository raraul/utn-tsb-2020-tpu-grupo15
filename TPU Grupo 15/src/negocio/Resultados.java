package negocio;

import soporte.ArchivoDeDatos;
import soporte.BaseDeDatos;
import soporte.TSB_OAHashtable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Resultados {
    private TSB_OAHashtable tablaHash;

    public Resultados(String path, Region pais) {
        tablaHash = new TSB_OAHashtable();
        ArchivoDeDatos archivoMesas = new ArchivoDeDatos(path + "\\mesas_totales_agrp_politica.dsv");
        archivoMesas.contarVotosPorRegion(this, pais);
    }

    public Resultados(Region pais) throws SQLException, ClassNotFoundException {
        tablaHash = new TSB_OAHashtable();
        BaseDeDatos.contarVotosPorRegion(this, pais);
    }

    public void sumarVotos(String codRegion, String codAgrupacion, int votos) {
        if(tablaHash.get(codRegion)==null) tablaHash.put(codRegion,new Agrupaciones());

        Agrupaciones a = (Agrupaciones) tablaHash.get(codRegion);
        a.getAgrupacion(codAgrupacion).sumarVotos(votos);
    }

    public Collection getResultadosRegion(String codRegion) {
        try {
            Agrupaciones a = (Agrupaciones) tablaHash.get(codRegion);
            return a.getResultados();
        }
        catch (NullPointerException ex) {
            // esto hubo que arreglarlo para el caso de Malvinas
            System.out.println("NullPointerException");
            ex.printStackTrace();
            ArrayList<String> listaVacia = new ArrayList<>();
//            listaVacia.add("Las Malvinas son argentinas.");
            listaVacia.add("No se registraron votos.");
            return listaVacia;
        }
    }
}

