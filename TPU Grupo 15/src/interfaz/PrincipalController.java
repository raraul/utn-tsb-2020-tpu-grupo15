package interfaz;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import soporte.ArchivoDeDatos;

import java.io.File;

public class PrincipalController {
    public Label lblOrigenDatosRuta;

    public void cambiarUbicacion(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Seleccionar ubicación de los datos");
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
        ArchivoDeDatos archivoAgrupaciones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_postulaciones.dsv");
        ArchivoDeDatos archivoRegiones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_regiones.dsv");
        ArchivoDeDatos archivoMesas = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\mesas_totales_agrp_politica.dsv");

        System.out.println(archivoAgrupaciones.primeraLinea());
        System.out.println(archivoRegiones.primeraLinea());
        System.out.println(archivoMesas.primeraLinea());
    }
}
