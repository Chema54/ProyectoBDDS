package main.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.common.SesionGlobal;
import main.common.Utilidades;
import main.common.VentanaInterna;

public class FXMLMenuCentralController implements Initializable {
    
    @FXML private AnchorPane pane_Escritorio;
    
    // Inyecciones para ocultar menús
    @FXML private Menu menuUsuarios;
    @FXML private Menu menuEmpleados;
    @FXML private Menu menuDepartamentos;
    @FXML private Menu menuArticulos;
    @FXML private Menu menuReportes; // Inyectado para ocultárselo a los demás
    
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
            
            // 1. Ocultamos todos los catálogos y reportes a quienes NO son de Central
            menuUsuarios.setVisible(false);
            menuEmpleados.setVisible(false);
            menuDepartamentos.setVisible(false);
            menuArticulos.setVisible(false);
            menuReportes.setVisible(false);
            
            // 2. Filtramos la pestaña de Logística específicamente para cada uno
            if (rolActual.equals("DEPARTAMENTO")) {
                // El DEPARTAMENTO solo solicita. No ve nada más.
                miAprobarSalidas.setVisible(false);
                miRegistrarEntradas.setVisible(false);
                
            } else if (rolActual.equals("SALIDAS")) {
                // El almacén (Salidas) solo aprueba salidas.
                miCrearSolicitud.setVisible(false);
                miRegistrarEntradas.setVisible(false);
                
            } else if (rolActual.equals("SUCURSAL")) {
                // El admin de SUCURSAL hace las compras y puede pedir para sí mismo
                miAprobarSalidas.setVisible(false);
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
            
            // Posición en cascada básica para que no se empalmen
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
    // ACCIONES DE CERRAR SESIÓN Y ACERCA DE...
    // ==========================================
    @FXML
    private void irAcercaDe(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Acerca de Global Finance", 
                "Sistema de Control de Almacén\nVersión 1.0\n\nDesarrollado para la Facultad de Estadística e Informática.\nCumplimiento de estándares DAO y roles de base de datos nativos.", 
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void irCerrarSesion(ActionEvent event) {
        try {
            // 1. Limpiamos la Sesión de Java y de MariaDB
            SesionGlobal.getInstance().cerrarSesion();
            main.database.DBConnector.getInstance().resetCredentials();
            
            // 2. Cargamos la vista del Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/gui/FXMLInicioSesionView.fxml"));
            Parent root = loader.load();
            
            // 3. Tomamos la ventana grandota actual y la encogemos al Login
            Stage stage = (Stage) pane_Escritorio.getScene().getWindow();
            
            stage.setTitle("Global Finance - Iniciar Sesión");
            stage.setScene(new Scene(root));
            
            // FIX: "Computed Size" (sizeToScene) en lugar de números hardcodeados
            stage.setMaximized(false); 
            stage.sizeToScene(); // Hace que la ventana tome las dimensiones exactas del FXML
            stage.centerOnScreen(); // Lo devolvemos al centro del monitor
            
        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error", "Ocurrió un problema al cerrar sesión.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // ==========================================
    // ACCIONES DE LOS DEMÁS MENÚS (¡YA CONECTADOS!)
    // ==========================================
    @FXML
    private void irRegistrarUsuarios(ActionEvent event) { 
        abrirVentanaFlotante("Registrar Usuario", "/main/resources/gui/FXMLRegistroUsuariosView.fxml"); 
    }
    
    @FXML
    private void irModificarUsuarios(ActionEvent event) { 
        // FIX: Ya está descomentado y conectado a tu nueva vista
        abrirVentanaFlotante("Modificar Usuario", "/main/resources/gui/FXMLModificarUsuariosView.fxml"); 
    }
    
    @FXML
    private void irMostrarUsuarios(ActionEvent event) { 
        abrirVentanaFlotante("Directorio de Usuarios", "/main/resources/gui/FXMLMostrarUsuariosView.fxml"); 
    }
    
    @FXML
    private void irRegistrarEmpleados(ActionEvent event) { 
        abrirVentanaFlotante("Registrar Empleado", "/main/resources/gui/FXMLRegistroEmpleadosView.fxml"); 
    }
    
    @FXML
    private void irModificarEmpleados(ActionEvent event) { 
        // FIX: Conectado a la vista de modificar empleados
        abrirVentanaFlotante("Modificar Empleado", "/main/resources/gui/FXMLModificarEmpleadosView.fxml"); 
    }
    
    @FXML
    private void irMostrarEmpleados(ActionEvent event) { 
        abrirVentanaFlotante("Directorio de Empleados", "/main/resources/gui/FXMLMostrarEmpleadosView.fxml"); 
    }
    
    @FXML
    private void irModificarDepartamento(ActionEvent event) { 
        abrirVentanaFlotante("Asignar Encargado", "/main/resources/gui/FXMLModificarDepartamentoView.fxml"); 
    }
    
    @FXML
    private void irGestionPartidas(ActionEvent event) { 
        abrirVentanaFlotante("Catálogo de Partidas", "/main/resources/gui/FXMLGestionPartidasView.fxml"); 
    }
    
    @FXML
    private void irGestionArticulos(ActionEvent event) { 
        abrirVentanaFlotante("Catálogo de Artículos", "/main/resources/gui/FXMLGestionArticulosView.fxml"); 
    }
    
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