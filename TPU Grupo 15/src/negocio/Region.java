package negocio;

import soporte.*;
import java.util.Collection;

public class Region {
    private String codigo;
    private String nombre;
    private TSB_OAHashtable subregiones;

    public Region(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        subregiones = new TSB_OAHashtable();
    }

    public void agregarSubregion(Region region) {
        subregiones.put(region.codigo, region);
    }

    public Collection getSubregiones() {
        return subregiones.values();
    }

    public Region getSubregion(String codigo) {
        return (Region) subregiones.get(codigo);
    }

    public Region getOrPutSubregion(String codigo) {
        Region subregion = (Region) subregiones.get(codigo);
        if (subregion == null) subregiones.put(codigo, new Region(codigo, ""));
        return (Region) subregiones.get(codigo);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "(" + codigo + ") "+ nombre;
    }


}
