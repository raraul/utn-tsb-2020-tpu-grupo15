package interfaz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import negocio.Agrupaciones;
import negocio.Region;
import negocio.Regiones;
import negocio.Resultados;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

public class PrincipalController {
    public Label lblOrigenDatosRuta;
    public ListView lvwListaResultados;
    public ComboBox cboDistritos, cboSecciones, cboCircuitos, cboMesas;
    public Resultados resultados;
    //    private Dialog<Object> alert;


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
        ObservableList oblist;
        try {
            Agrupaciones.leerAgrupaciones(lblOrigenDatosRuta.getText());
            Regiones regiones = new Regiones(lblOrigenDatosRuta.getText());
            String mensajeAlerta = "La carga de datos y su procesamiento pueden demorar y consumir recursos de su PC." +
                    "\n¿Desea continuar?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensajeAlerta, ButtonType.YES, ButtonType.CANCEL);
            // Shows the dialog but does not wait for a user response (in other words, this brings up a non-blocking dialog).
            // Se tilda antes de llegar a mostrarse a veces jaja
            // alert.show();
            alert.showAndWait();
//            alertResponse = alert.getResult();
            if (alert.getResult() == ButtonType.CANCEL) {
                ArrayList<String> listaEspera = new ArrayList<>();
//                listaEspera.add("");
                ObservableList oblistEspera = FXCollections.observableArrayList(listaEspera);
                lvwListaResultados.setItems(oblistEspera);
                return;
            }

            oblist = FXCollections.observableArrayList(regiones.getDistritos());
            cboDistritos.setItems(oblist);

            resultados = new Resultados(lblOrigenDatosRuta.getText(), regiones.getPais());
            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion("00"));
            lvwListaResultados.setItems(oblist);
            cboDistritos.setDisable(false);
        } catch (FileNotFoundException e) {
            ArrayList<String> listaEspera = new ArrayList<>();
            listaEspera.add("No se pudieron leer los archivos.");
            listaEspera.add("Asegúrese de haber seleccionado correctamente el directorio.");
            ObservableList oblistEspera = FXCollections.observableArrayList(listaEspera);
            lvwListaResultados.setItems(oblistEspera);
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudieron leer los archivos.\nAsegúrese de haber seleccionado correctamente el directorio.", ButtonType.OK);
            alert.showAndWait();
            cboDistritos.setDisable(true);
//            Optional<Optional> result = alert.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK) {
//                formatSystem();
//            }
        }
        cboCircuitos.setDisable(true);
        cboSecciones.setDisable(true);
        cboMesas.setDisable(true);
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

    //TODO corregir validacion para circuitos cuando se reutiliza secciones/circuitos/etc
    public void elegirSeccion(ActionEvent actionEvent) {
        ObservableList oblist;
        if (cboSecciones.getValue() != null) {
            Region seccion = (Region) cboSecciones.getValue();
            oblist = FXCollections.observableArrayList(seccion.getSubregiones());
            cboCircuitos.setItems(oblist);
            cboCircuitos.setDisable(false);
            cboMesas.setDisable(true);

            oblist = FXCollections.observableArrayList(resultados.getResultadosRegion(seccion.getCodigo()));
            System.out.println("\n\n\n\n");
            System.out.println(resultados.getResultadosRegion(seccion.getCodigo()));
            System.out.println("\n\n\n\n");
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

    //public void cargarDatos() {
    //
    //    // Movida la creacion de los archivos a el constructor de Agrupaciones
    //
    //    //ArchivoDeDatos archivoAgrupaciones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_postulaciones.dsv");
    //    //ArchivoDeDatos archivoRegiones = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\descripcion_regiones.dsv");
    //    //ArchivoDeDatos archivoMesas = new ArchivoDeDatos(lblOrigenDatosRuta.getText() + "\\mesas_totales_agrp_politica.dsv");
    //
    //    //System.out.println(archivoAgrupaciones.primeraLinea());
    //    //System.out.println(archivoRegiones.primeraLinea());
    //    //System.out.println(archivoMesas.primeraLinea());
    //
    //}
}
