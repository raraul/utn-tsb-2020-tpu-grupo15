package negocio;

import soporte.ArchivoDeDatos;

public class Agrupaciones {
    private ArchivoDeDatos archivoAgrupaciones;
    private TSB_OAHashtable tablaHash;
    private ArchivoDeDatos archivoMesas;

    public Agrupaciones(String path) {
        archivoAgrupaciones = new ArchivoDeDatos(path + "\\descripcion_postulaciones.dsv");
        archivoMesas = new ArchivoDeDatos(path + "\\mesas_totales_agrp_politica.dsv");
        tablaHash = archivoAgrupaciones.procesarAgrupaciones();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : tablaHash.values()) {
            sb.append("\n").append(o);
        }
        return sb.toString();
    }
}
