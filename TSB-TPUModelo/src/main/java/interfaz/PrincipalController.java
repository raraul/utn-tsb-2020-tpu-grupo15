package interfaz;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import negocio.Agrupacion;
import negocio.Agrupaciones;
import soporte.ArchivoDeDatos;

import java.io.File;

public class PrincipalController {
    public Label lblOrigenDatosRuta;
    public TextArea txtAgrupaciones;

    public void cambiarUbicacion(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Seleccionar ubicación de los datos");
        //Si se quiere cambiar el directorio actual, te deja elegir desde donde estas actualmente
        if (!lblOrigenDatosRuta.getText().equals("Seleccionar ubicación de los datos")) {
            dc.setInitialDirectory(new File(lblOrigenDatosRuta.getText()));
        }

        File carpetaOrigenDatos = dc.showDialog(null);
        if (carpetaOrigenDatos != null) {
            lblOrigenDatosRuta.setText(carpetaOrigenDatos.getPath());
            CargarDatos();
        }
    }

    public void CargarDatos() {

        // Movida la creacion de los archivos a el constructor de Agrupaciones

//        ArchivoDeDatos archivoAgrupaciones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_postulaciones.dsv");
//        ArchivoDeDatos archivoRegiones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_regiones.dsv");
//        ArchivoDeDatos archivoMesas = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\mesas_totales_agrp_politica.dsv");

//        System.out.println(archivoAgrupaciones.primeraLinea());
//        System.out.println(archivoRegiones.primeraLinea());
//        System.out.println(archivoMesas.primeraLinea());

        Agrupaciones agrupaciones = new Agrupaciones(lblOrigenDatosRuta.getText());
        // acá habría que mostrarlo en la GUI
//        System.out.println(agrupaciones.toString());
        txtAgrupaciones.setText(agrupaciones.toString());

    }
}
