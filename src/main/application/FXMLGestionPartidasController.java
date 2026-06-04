package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.PartidaPresupuestalDAO;
import main.business.dto.PartidaPresupuestalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLGestionPartidasController implements Initializable {

    @FXML private TextField txtClave;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<PartidaPresupuestalDTO> tablaPartidas;
    @FXML private TableColumn<PartidaPresupuestalDTO, Integer> colId;
    @FXML private TableColumn<PartidaPresupuestalDTO, String> colClave;
    @FXML private TableColumn<PartidaPresupuestalDTO, String> colDescripcion;

    private PartidaPresupuestalDAO partidaDAO = new PartidaPresupuestalDAO();
    private ObservableList<PartidaPresupuestalDTO> listaPartidas = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarPartidas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPartida"));
        colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    private void cargarPartidas() {
        try {
            listaPartidas.setAll(partidaDAO.getAll());
            tablaPartidas.setItems(listaPartidas);
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las partidas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        String clave = txtClave.getText().trim();
        String desc = txtDescripcion.getText().trim();

        if (clave.isEmpty() || desc.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos Vacíos", "Llena todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            PartidaPresupuestalDTO nueva = new PartidaPresupuestalDTO(0, clave, desc);
            partidaDAO.createOne(nueva);
            Utilidades.mostrarAlertaSimple("Éxito", "Partida registrada correctamente.", Alert.AlertType.INFORMATION);
            txtClave.clear();
            txtDescripcion.clear();
            cargarPartidas(); // Recargar la tabla
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "La clave podría estar duplicada.", Alert.AlertType.ERROR);
        }
    }
}