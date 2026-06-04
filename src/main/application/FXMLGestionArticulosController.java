package main.application;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.ArticuloDAO;
import main.business.dao.PartidaPresupuestalDAO;
import main.business.dto.ArticuloDTO;
import main.business.dto.PartidaPresupuestalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLGestionArticulosController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<PartidaPresupuestalDTO> cbPartida;
    @FXML
    private TableView<ArticuloDTO> tablaArticulos;
    @FXML
    private TableColumn<ArticuloDTO, Integer> colId;
    @FXML
    private TableColumn<ArticuloDTO, String> colNombre;
    @FXML
    private TableColumn<ArticuloDTO, String> colDescripcion;
    @FXML
    private TableColumn<ArticuloDTO, String> colPartida;
    @FXML
    private TableColumn<ArticuloDTO, String> colEstado;

    private ArticuloDAO articuloDAO = new ArticuloDAO();
    private PartidaPresupuestalDAO partidaDAO = new PartidaPresupuestalDAO();
    private ObservableList<ArticuloDTO> listaArticulos = FXCollections.observableArrayList();
    private Map<Integer, String> mapaPartidas = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCaches();
        configurarColumnas();
        cargarArticulos();
    }

    private void cargarCaches() {
        try {
            ObservableList<PartidaPresupuestalDTO> partidas = FXCollections.observableArrayList(partidaDAO.getAll());
            cbPartida.setItems(partidas);
            for (PartidaPresupuestalDTO p : partidas) {
                mapaPartidas.put(p.getIdPartida(), p.getClave() + " - " + p.getDescripcion());
            }
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se cargaron las partidas.", Alert.AlertType.ERROR);
        }
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("IDArticulo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPartida.setCellValueFactory(cellData -> {
            String descPartida = mapaPartidas.get(cellData.getValue().getIdPartida());
            return new SimpleStringProperty(descPartida != null ? descPartida : "Sin Partida");
        });
    }

    private void cargarArticulos() {
        try {
            listaArticulos.setAll(articuloDAO.getAll());
            tablaArticulos.setItems(listaArticulos);
        } catch (UserDisplayableException e) {
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (txtNombre.getText().trim().isEmpty() || cbPartida.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Error", "Falta nombre o partida.", Alert.AlertType.WARNING);
            return;
        }
        try {
            ArticuloDTO nuevo = new ArticuloDTO.ArticuloBuilder()
                    .setDescripcion(txtDescripcion.getText().trim())
                    .setIdPartida(cbPartida.getValue().getIdPartida())
                    .setEstado("activo")
                    .setNombre(txtNombre.getText().trim())
                    .build();
            articuloDAO.createOne(nuevo);
            txtNombre.clear();
            txtDescripcion.clear();
            cbPartida.setValue(null);
            cargarArticulos();
            Utilidades.mostrarAlertaSimple("Éxito", "Artículo registrado.", Alert.AlertType.INFORMATION);
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo registrar.", Alert.AlertType.ERROR);
        }
    }
}
