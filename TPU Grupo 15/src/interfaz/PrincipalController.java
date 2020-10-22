package interfaz;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

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
        }
    }
}
