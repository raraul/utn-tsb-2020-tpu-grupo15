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

    @Deprecated
    public String primeraLinea() {
        String primeraLinea = "";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(archivo));
            primeraLinea = bf.readLine();
        }
        catch (IOException ex) {
            System.out.println("No se pudo leer el archivo");
            ex.printStackTrace();
        }
        return primeraLinea;
    }


    /**
     *Procesa la
     * @return tablaHash devuelve la tabla con
     */
    public TSB_OAHashtable procesarAgrupaciones() {
        TSB_OAHashtable tablaHash = new TSB_OAHashtable();
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                String[] campos = sc.nextLine().split("\\|");
                // Preguntamos si el campo 4 que es el de Cod de categoria corresponde  a Presidente
                if (campos[4].equals("000100000000000")) {
                    //todo Programar el get
                    Agrupacion agrupacion = (Agrupacion) tablaHash.get(campos[5]);
                    int votos = Integer.parseInt(campos[6]);
                    agrupacion.sumarVotos(votos);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return tablaHash;
    }



    //A esto la profe le llama Identificar Agrupacion

    public void contarVotosPorAgrupacion(TSB_OAHashtable tablaHash) {
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                String[] campos = sc.nextLine().split("\\|");
                if (campos[0].equals("000100000000000")) {
                    Agrupacion agrupacion = new Agrupacion(campos[2], campos[3]);
                    tablaHash.put(agrupacion.getCodigo(), agrupacion);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }




    // todo Revisar nombres de metodos

    /**
     *Procesa un archivo de Descripcion de Postulaciones.dsv , linea a linea buscando que el primer campo sea
     * el codigo correspondiente a "Presidente", y crea una Instancia de la clase Agrupacion por cada una que encuentre
     * guardando en ella los campos 2 y 3, correspondientes a codigo de la agrupacion y nombre respectivamente
     * Guarda estas Agrupaciones en una tabla HASH con Key:'Codigo de Agrupacion', Value:'unaAgrupacion'
     * @param tablaHash  es recibida como parametro y utilizada para cargar las agrupaciones.
     */

    //mismo proceso que contar votos por agrupacion , con nombre corregido
    public void IdentificarAgrupacion(TSB_OAHashtable tablaHash) {
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                String[] campos = sc.nextLine().split("\\|");
                if (campos[0].equals("000100000000000")) {
                    Agrupacion agrupacion = new Agrupacion(campos[2], campos[3]);
                    tablaHash.put(agrupacion.getCodigo(), agrupacion);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
