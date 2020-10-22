package soporte;

import negocio.Agrupacion;
import negocio.TSB_OAHashtable;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ArchivoDeDatos {
    private File archivo;

    public ArchivoDeDatos(String path) {
        archivo = new File(path);
    }

    public String primeraLinea() {
        String primeraLinea = "";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(archivo));
            primeraLinea = bf.readLine();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return primeraLinea;
    }

    public TSB_OAHashtable procesarAgrupaciones() {
        TSB_OAHashtable tablaHash = new TSB_OAHashtable();

        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                String[] campos = sc.nextLine().split("\\|");
                if (campos[0].equals("000100000000000")) {
                    Agrupacion agrupacion = new Agrupacion(campos[2], campos[3]);
                    tablaHash.put(agrupacion.getCodigo(), agrupacion);
                }
            }
            return tablaHash;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
