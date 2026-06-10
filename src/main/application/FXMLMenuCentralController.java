package main.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import main.common.SesionGlobal;
import main.common.VentanaInterna;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLMenuCentralController implements Initializable {
    
    @FXML private AnchorPane pane_Escritorio;
    
    // Inyecciones para ocultar menús
    @FXML private Menu menuUsuarios;
    @FXML private Menu menuEmpleados;
    @FXML private Menu menuDepartamentos;
    @FXML private Menu menuArticulos;
    
    @FXML private MenuItem miCrearSolicitud;
    @FXML private MenuItem miAprobarSalidas;
    @FXML private MenuItem miRegistrarEntradas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // ==========================================
        // LÓGICA DE ROLES (Ocultar botones)
        // ==========================================
        String rolActual = SesionGlobal.getInstance().getRolActual();
        
        if (rolActual != null && !rolActual.equals("CENTRAL")) {
            
            // Ocultamos todos los catálogos a quienes no son de Central
            menuUsuarios.setVisible(false);
            menuEmpleados.setVisible(false);
            menuDepartamentos.setVisible(false);
            menuArticulos.setVisible(false);
            
            // Filtramos Logística
            if (rolActual.equals("DEPARTAMENTO")) {
                miAprobarSalidas.setVisible(false);
                miRegistrarEntradas.setVisible(false);
                
            } else if (rolActual.equals("SALIDAS")) {
                miCrearSolicitud.setVisible(false);
                miRegistrarEntradas.setVisible(false);
            }
        }
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

    // ==========================================
    // ACCIONES DEL MENÚ: LOGÍSTICA
    // ==========================================
    @FXML
    private void irCrearSolicitud(ActionEvent event) {
        abrirVentanaFlotante("Punto de Venta - Solicitudes", "/main/resources/gui/FXMLCrearSolicitudView.fxml");
    }

    @FXML
    private void irAprobarSalidas(ActionEvent event) {
        abrirVentanaFlotante("Bandeja de Aprobación", "/main/resources/gui/FXMLAprobarSalidasView.fxml");
    }

    @FXML
    private void irRegistrarEntradas(ActionEvent event) {
        abrirVentanaFlotante("Registro de Compras (Facturas)", "/main/resources/gui/FXMLRegistrarEntradasView.fxml");
    }

    @FXML
    private void irCentralReportes(ActionEvent event) {
        abrirVentanaFlotante("Reportes", "/main/resources/gui/FXMLCentralReportesView.fxml");
    }
}