

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
package main.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import main.common.VentanaInterna;

public class FXMLMenuCentralController implements Initializable {
    
    @FXML private AnchorPane pane_Escritorio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización
    }    

    // ==========================================
    // MÉTODO AYUDANTE (Evita repetir código)
    // ==========================================
    private void abrirVentanaFlotante(String titulo, String rutaFXML) {
        try {
            Node formulario = FXMLLoader.load(getClass().getResource(rutaFXML));
            VentanaInterna ventana = new VentanaInterna(titulo, formulario);
            
            // Posición en cascada básica para que no se empalmen exacto encima
            double offset = (pane_Escritorio.getChildren().size() * 30) + 20;
            ventana.setLayoutX(offset);
            ventana.setLayoutY(offset);
            
            pane_Escritorio.getChildren().add(ventana);
        } catch (IOException ex) {
            System.out.println("Error al cargar " + rutaFXML + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ==========================================
    // ACCIONES DEL MENÚ: USUARIOS
    // ==========================================
    @FXML
    private void irRegistrarUsuarios(ActionEvent event) {
        abrirVentanaFlotante("Registrar Usuario", "/main/resources/gui/FXMLRegistroUsuariosView.fxml");
    }

    @FXML
    private void irModificarUsuarios(ActionEvent event) {
        // abrirVentanaFlotante("Modificar Usuario", "/main/resources/gui/FXMLModificarUsuariosView.fxml");
    }

    @FXML
    private void irMostrarUsuarios(ActionEvent event) {
        abrirVentanaFlotante("Directorio de Usuarios", "/main/resources/gui/FXMLMostrarUsuariosView.fxml");
    }

    // ==========================================
    // ACCIONES DEL MENÚ: EMPLEADOS
    // ==========================================
    @FXML
    private void irRegistrarEmpleados(ActionEvent event) {
        abrirVentanaFlotante("Registrar Empleado", "/main/resources/gui/FXMLRegistroEmpleadosView.fxml");
    }

    @FXML
    private void irModificarEmpleados(ActionEvent event) {
        abrirVentanaFlotante("Modificar Empleado", "/main/resources/gui/FXMLModificarEmpleadosView.fxml");
    }

    @FXML
    private void irMostrarEmpleados(ActionEvent event) {
        abrirVentanaFlotante("Directorio de Empleados", "/main/resources/gui/FXMLMostrarEmpleadosView.fxml");
    }

    // ==========================================
    // ACCIONES DEL MENÚ: DEPARTAMENTOS
    // ==========================================
    @FXML
    private void irModificarDepartamento(ActionEvent event) {
        abrirVentanaFlotante("Asignar Encargado", "/main/resources/gui/FXMLModificarDepartamentoView.fxml");
    }

    // ==========================================
    // ACCIONES DEL MENÚ: ARTÍCULOS
    // ==========================================
    @FXML
    private void irGestionPartidas(ActionEvent event) {
        abrirVentanaFlotante("Catálogo de Partidas", "/main/resources/gui/FXMLGestionPartidasView.fxml");
    }

    @FXML
    private void irGestionArticulos(ActionEvent event) {
        abrirVentanaFlotante("Catálogo de Artículos", "/main/resources/gui/FXMLGestionArticulosView.fxml");
    }
}