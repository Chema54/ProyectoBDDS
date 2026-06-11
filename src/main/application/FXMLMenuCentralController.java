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

    @FXML
    private AnchorPane pane_Escritorio;

    @FXML
    private Menu menuUsuarios;
    @FXML
    private Menu menuEmpleados;
    @FXML
    private Menu menuDepartamentos;
    @FXML
    private Menu menuArticulos;
    @FXML
    private Menu menuReportes;
    @FXML
    private Menu menuSucursales;

    @FXML
    private MenuItem miRegistrarSucursal;
    @FXML
    private MenuItem miModificarSucursal;
    @FXML
    private MenuItem miAsignarDepto;

    @FXML
    private MenuItem miCrearSolicitud;
    @FXML
    private MenuItem miAprobarSalidas;
    @FXML
    private MenuItem miRegistrarEntradas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String rolActual = SesionGlobal.getInstance().getRolActual();

        if (rolActual != null && !rolActual.equals("CENTRAL")) {

            menuUsuarios.setVisible(false);
            menuReportes.setVisible(false);

            if (rolActual.equals("SUCURSAL")) {
                miAprobarSalidas.setVisible(false);
                miRegistrarSucursal.setVisible(false);
                miModificarSucursal.setVisible(false);

            } else if (rolActual.equals("SALIDAS")) {
                menuEmpleados.setVisible(false);
                menuDepartamentos.setVisible(false);
                miCrearSolicitud.setVisible(false);
                miRegistrarEntradas.setVisible(false);
                menuSucursales.setVisible(false);

            } else if (rolActual.equals("DEPARTAMENTO")) {
                menuEmpleados.setVisible(false);
                menuDepartamentos.setVisible(false);
                menuArticulos.setVisible(false);
                miAprobarSalidas.setVisible(false);
                miRegistrarEntradas.setVisible(false);
                menuSucursales.setVisible(false);
            }
        }
    }

    private void abrirVentanaFlotante(String titulo, String rutaFXML) {
        try {
            Node formulario = FXMLLoader.load(getClass().getResource(rutaFXML));
            VentanaInterna ventana = new VentanaInterna(titulo, formulario);
            double offset = (pane_Escritorio.getChildren().size() * 30) + 20;
            ventana.setLayoutX(offset);
            ventana.setLayoutY(offset);

            pane_Escritorio.getChildren().add(ventana);
        } catch (IOException ex) {
            System.out.println("Error al cargar " + rutaFXML + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void irAcercaDe(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Acerca de Global Finance",
                "Sistema de Control de Almacén\nPor Lenin y Chema, llamenos si necesita ayuda",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void irCerrarSesion(ActionEvent event) {
        try {
            SesionGlobal.getInstance().cerrarSesion();
            main.database.DBConnector.getInstance().resetCredentials();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/gui/FXMLInicioSesionView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Global Finance - Iniciar Sesión");
            loginStage.setScene(new Scene(root));
            loginStage.centerOnScreen();
            loginStage.show();
            Stage menuStage = (Stage) pane_Escritorio.getScene().getWindow();
            menuStage.close();

        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error", "Ocurrió un problema al cerrar sesión.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void irRegistrarUsuarios(ActionEvent event) {
        abrirVentanaFlotante("Registrar Usuario", "/main/resources/gui/FXMLRegistroUsuariosView.fxml");
    }

    @FXML
    private void irModificarUsuarios(ActionEvent event) {
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

    @FXML
    private void irConsultarInventario(ActionEvent event) {
        abrirVentanaFlotante("Inventario Actual", "/main/resources/gui/FXMLMostrarInventarioView.fxml");
    }

    @FXML
    private void irRegistrarSucursales(ActionEvent event) {
        abrirVentanaFlotante("Registrar Sucursal", "/main/resources/gui/FXMLRegistroSucursalView.fxml");
    }

    @FXML
    private void irTablaModificarSucursales(ActionEvent event) {
        abrirVentanaFlotante("Registrar Sucursal", "/main/resources/gui/FXMLTablaModificarSucursalView.fxml");
    }

    @FXML
    private void irAsignarDepartamentoASucursal(ActionEvent event) {
        abrirVentanaFlotante("Registrar Sucursal", "/main/resources/gui/FXMLRegistroDepartamentoView.fxml");
    }
}
