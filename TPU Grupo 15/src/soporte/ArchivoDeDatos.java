package soporte;

import interfaz.PrincipalController;
import javafx.scene.control.ProgressIndicator;
import negocio.Agrupacion;
import negocio.Region;
import negocio.Regiones;
import negocio.Resultados;

import java.io.*;
import java.util.Scanner;

public class ArchivoDeDatos {
    private File archivo;

    public ArchivoDeDatos(String path) {
        archivo = new File(path);
    }

    /**
     * Procesa un archivo de Descripcion de Postulaciones.dsv , linea a linea buscando que el primer campo sea
     * el codigo correspondiente a "Presidente", y crea una Instancia de la clase Agrupacion por cada una que encuentre
     * guardando en ella los campos 2 y 3, correspondientes a codigo de la agrupacion y nombre respectivamente
     * Guarda estas Agrupaciones en una tabla HASH con Key:'Codigo de Agrupacion', Value:'unaAgrupacion'
     *
     * @param *tablaHash es recibida como parametro y utilizada para cargar las agrupaciones.
     */
    //mismo proceso que contar votos por agrupacion
    public TSB_OAHashtable identificarAgrupacion() throws FileNotFoundException {
        TSB_OAHashtable tablaHash = new TSB_OAHashtable(10);
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                String[] campos = sc.nextLine().split("\\|");
                if (campos[0].equals("000100000000000")) {
                    Agrupacion agrupacion = new Agrupacion(campos[2], campos[3]);
                    tablaHash.put(agrupacion.getCodigo(), agrupacion);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo.");
            throw e;
        }
        return tablaHash;
    }

    public Region identificarRegiones() throws FileNotFoundException {
        Region pais = new Region("00", "Argentina");
        Region distrito, seccion;
        String codigo, nombre, campos[];
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                campos = sc.nextLine().split("\\|");
                codigo = campos[0];
                nombre = campos[1];
                switch (codigo.length()) {
                    case 2:
                        //Distrito
                        distrito = pais.getOrPutSubregion(codigo);
                        distrito.setNombre(nombre);
                        break;
                    case 5:
                        //Seccion
                        distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
                        seccion = distrito.getOrPutSubregion(codigo);
                        seccion.setNombre(nombre);
                        break;
                    case 11:
                        //Circuito
                        distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
                        seccion = distrito.getOrPutSubregion((codigo.substring(0, 5)));
                        seccion.agregarSubregion(new Region(codigo, nombre));
                        break;

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo.");
            throw e;
        }
        return pais;
    }

    public void contarVotosPorRegion(Resultados resultados, Region pais) {
        int votos;
        String linea = "", campos[];
        Region distrito, seccion, circuito;
        try {
            Scanner sc = new Scanner(archivo);
            while (sc.hasNext()) {
                linea = sc.nextLine();
                campos = linea.split("\\|");
                // campos[0] = CODIGO_DISTRITO
                // campos[1] = CODIGO_SECCION
                // campos[2] = CODIGO_CIRCUITO
                // campos[3] = CODIGO_MESA
                // campos[4] = CODIGO_CATEGORIA
                // campos[5] = CODIGO_AGRUPACION
                // campos[6] = VOTOS_AGRUPACION
                if (campos[4].equals("000100000000000")) {
                    votos = Integer.parseInt(campos[6]);
                    resultados.sumarVotos("00", campos[5], votos); // Votos de toda la Argentina
                    for (int i = 0; i < 4; i++) {
                        resultados.sumarVotos(campos[i], campos[5], votos);
                    }
                    //Para mostrar las mesas
                    distrito = pais.getSubregion(campos[0]);
                    seccion = distrito.getSubregion(campos[1]);
                    circuito = seccion.getSubregion(campos[2]);
                    circuito.agregarSubregion(new Region(campos[3], campos[3]));
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
