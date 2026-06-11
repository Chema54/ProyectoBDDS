package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLModificarEmpleadosController implements Initializable {

    @FXML
    private ComboBox<EmpleadoDTO> cbBuscarEmpleado;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtNumPersonal;
    @FXML
    private ComboBox<DepartamentoDTO> cbDepartamento;

    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private DepartamentoDAO departamentoDAO = new DepartamentoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarListas();

        cbBuscarEmpleado.valueProperty().addListener((obs, oldVal, empleadoSeleccionado) -> {
            if (empleadoSeleccionado != null) {
                txtNombre.setText(empleadoSeleccionado.getNombre());
                txtApellidos.setText(empleadoSeleccionado.getApellidos());
                txtNumPersonal.setText(empleadoSeleccionado.getNumeroPersonal());

                for (DepartamentoDTO depto : cbDepartamento.getItems()) {
                    if (depto.getIDDepartamento() == empleadoSeleccionado.getIDDepartamento()) {
                        cbDepartamento.setValue(depto);
                        break;
                    }
                }
            }
        });
    }

    private void cargarListas() {
        try {
            cbBuscarEmpleado.setItems(FXCollections.observableArrayList(empleadoDAO.getAll()));
            cbDepartamento.setItems(FXCollections.observableArrayList(departamentoDAO.getAll()));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Fallo al cargar la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardarCambios(ActionEvent event) {
        EmpleadoDTO empleadoOriginal = cbBuscarEmpleado.getValue();

        if (empleadoOriginal == null || txtNombre.getText().isEmpty() || cbDepartamento.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Seleccione un empleado y llene los campos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            EmpleadoDTO empleadoActualizado = new EmpleadoDTO.EmpleadoBuilder()
                    .setIDEmpleado(empleadoOriginal.getIDEmpleado())
                    .setNombre(txtNombre.getText().trim())
                    .setApellidos(txtApellidos.getText().trim())
                    .setNumeroPersonal(txtNumPersonal.getText().trim())
                    .setIDDepartamento(cbDepartamento.getValue().getIDDepartamento())
                    .setTelefono(empleadoOriginal.getTelefono())
                    .setCorreo(empleadoOriginal.getCorreo())
                    .build();

            empleadoDAO.updateOne(empleadoActualizado);

            Utilidades.mostrarAlertaSimple("Éxito", "Empleado actualizado correctamente.", Alert.AlertType.INFORMATION);
            cbBuscarEmpleado.setValue(null);
            txtNombre.clear();
            txtApellidos.clear();
            txtNumPersonal.clear();
            cargarListas();

        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el empleado en la Base de Datos.", Alert.AlertType.ERROR);
        }
    }
}
