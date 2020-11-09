package interfaz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import negocio.Agrupaciones;
import negocio.Region;
import negocio.Regiones;
import negocio.Resultados;
import org.junit.jupiter.api.parallel.Resources;
import soporte.BaseDeDatos;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    public Label lblOrigenDatosRuta;
    public ListView lvwListaResultados;
    public ComboBox cboDistritos, cboSecciones, cboCircuitos, cboMesas;
    public Resultados resultados;
    public boolean usarDb = false;
    public Button btnGuardarEnDb;
    public Regiones regiones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        if (BaseDeDatos.existeArchivoDB()) {
//            String mensajeAlerta = "Se ha detectado un archivo de base de datos.\n¿Desea cargarlo?";
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensajeAlerta, ButtonType.YES, ButtonType.NO);
//            alert.setTitle("¿Cargar archivo DB?");
//            alert.showAndWait();
//            if (alert.getResult() == ButtonType.CANCEL) {
//                usarDb = true;
//            }
//        }
    }

    public void cambiarUbicacion(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Seleccionar ubicación de los datos");
        //Si se quiere cambiar el directorio actual, te deja elegir desde donde estas actualmente
        if (!lblOrigenDatosRuta.getText().equals("Seleccionar ubicación de los datos")) {
            dc.setInitialDirectory(new File(lblOrigenDatosRuta.getText()));
        } else {
            // Abrimos la ventana en el directorio actual
            dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        }

        File carpetaOrigenDatos = dc.showDialog(null);
        if (carpetaOrigenDatos != null) {
            lblOrigenDatosRuta.setText(carpetaOrigenDatos.getPath());
//            cargarDatos();
            mostrarMensajeEspera();
        }
    }

    public void mostrarMensajeEspera() {
        ArrayList<String> listaEspera = new ArrayList<>();
        listaEspera.add("Cargando votos... espere por favor.");
        ObservableList oblistEspera = FXCollections.observableArrayList(listaEspera);
        lvwListaResultados.setItems(oblistEspera);
        cargarDatos();
    }

    public void cargarDatos() {
        String mensajeAlerta = "La carga de datos y su procesamiento pueden demorar y consumir recursos de su PC." +
                "\n¿Desea continuar?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensajeAlerta, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Confirmar carga");
        // Shows the dialog but does not wait for a user response
        // (in other words, this brings up a non-blocking dialog).
        // Se tilda antes de llegar a mostrarse a veces jaja
        // alert.show();
        alert.showAndWait();
        // alertResponse = alert.getResult();
        if (alert.getResult() == ButtonType.CANCEL) {
            ArrayList<String> listaEspera = new ArrayList<>();
            // listaEspera.add("");
            ObservableList oblistEspera = FXCollections.observableArrayList(listaEspera);
            lvwListaResultados.setItems(oblistEspera);
            return;
        }

        ObservableList oblist;
        try {
            if (usarDb) {
                Agrupaciones.leerAgrupaciones();
                regiones = new Regiones();
                resultados = new Resultados(regiones.getPais());
            } else {
                Agrupaciones.leerAgrupaciones(lblOrigenDatosRuta.getText());
                regiones = new Regiones(lblOrigenDatosRuta.getText());
                resultados = new Resultados(lblOrigenDatosRuta.getText(), regiones.getPais());
            }

            oblist = FXCollections.observableArrayList(regiones.getDistritos());
            cboDistritos.setItems(oblist);

            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion("00"));
            lvwListaResultados.setItems(oblist);
            cboDistritos.setDisable(false);
        } catch (FileNotFoundException e) {
            ArrayList<String> listaEspera = new ArrayList<>();
            listaEspera.add("No se pudieron leer los archivos.");
            listaEspera.add("Asegúrese de haber seleccionado correctamente el directorio.");
            ObservableList oblistEspera = FXCollections.observableArrayList(listaEspera);
            lvwListaResultados.setItems(oblistEspera);

            alert = new Alert(Alert.AlertType.ERROR, "No se pudieron leer los archivos.\nAsegúrese de haber seleccionado correctamente el directorio.", ButtonType.OK);
            alert.setTitle("Error en la lectura");
            alert.showAndWait();

            cboDistritos.setDisable(true);
        } catch (SQLException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR,
                    "No se pudieron leer los datos desde la base de datos." +
                            "\nAsegúrese de que exista y esté correcto el archivo de soporte de la base de datos.",
                    ButtonType.OK);
            alert.setTitle("Error en la conexión con la DB");
            alert.showAndWait();
        }
        cboCircuitos.setDisable(true);
        cboSecciones.setDisable(true);
        cboMesas.setDisable(true);
        btnGuardarEnDb.setDisable(false);
    }

    public void elegirDistrito(ActionEvent actionEvent) {
        ObservableList oblist;
        Region distrito = (Region) cboDistritos.getValue();
        oblist = FXCollections.observableArrayList(distrito.getSubregiones());
        cboSecciones.setItems(oblist);
        cboSecciones.setDisable(false);
        cboCircuitos.setDisable(true);
        cboMesas.setDisable(true);

        oblist = FXCollections.observableArrayList(resultados.getResultadosRegion(distrito.getCodigo()));
        lvwListaResultados.setItems(oblist);
    }

    public void elegirSeccion(ActionEvent actionEvent) {
        ObservableList oblist;
        if (cboSecciones.getValue() != null) {
            Region seccion = (Region) cboSecciones.getValue();
            oblist = FXCollections.observableArrayList(seccion.getSubregiones());
            cboCircuitos.setItems(oblist);
            cboCircuitos.setDisable(false);
            cboMesas.setDisable(true);

            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion(seccion.getCodigo()));
            lvwListaResultados.setItems(oblist);
        } else
            cboCircuitos.setItems(null);
    }

    public void elegirCircuito(ActionEvent actionEvent) {
        ObservableList oblist;
        if (cboCircuitos.getValue() != null) {
            Region circuito = (Region) cboCircuitos.getValue();
            oblist = FXCollections.observableArrayList(circuito.getSubregiones());
            cboMesas.setItems(oblist);
            cboMesas.setDisable(false);

            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion(circuito.getCodigo()));
            lvwListaResultados.setItems(oblist);
        } else
            cboMesas.setItems(null);
    }

    public void elegirMesas(ActionEvent actionEvent) {
        ObservableList oblist;
        if (cboMesas.getValue() != null) {
            Region mesa = (Region) cboMesas.getValue();

            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion(mesa.getCodigo()));
            lvwListaResultados.setItems(oblist);
        }
    }


    public void guardarEnDb(ActionEvent actionEvent) {

        try {
            BaseDeDatos.eliminarTodasLasTablas();
            BaseDeDatos.crearBaseDeDatos();
            System.out.println("Mirá el archivo ahora.");
//            BaseDeDatos.guardarRegiones(regiones.getPais());
//            BaseDeDatos.guardarVotos(resultados);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No se pudo guardar en la base de datos." +
                            "\nAsegúrese de que exista y esté correcto el archivo de soporte de la base de datos.",
                    ButtonType.OK);
            alert.setTitle("Error al guardar en DB");
            alert.showAndWait();
            return;
        }

        // Informamos al usuario el guardado exitoso
        String mensajeAlerta = "Se han guardado exitosamente los datos en la base de datos.";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensajeAlerta, ButtonType.OK);
        alert.setTitle("Guardado exitoso");
        alert.showAndWait();

        // Se acaba de hacer el guardado, deshabilitamos el botón
        btnGuardarEnDb.setDisable(true);

    }
}
