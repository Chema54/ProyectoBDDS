/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.business.dao.UsuarioDAO;
import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.UserDisplayableException;

/**
 *
 * @author josem
 */
public class ProyectoBD extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/gui/FXMLInicioSesionView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        UsuarioDTO usuario = new UsuarioDTO(
                1,                                             
                "leninrevan",                                  
                UsuarioDTO.getGeneratedHashedPassword("1234"), 
                UsuarioRol.CENTRAL,                            
                true                                           
        );
        
        try {
            usuarioDAO.createOne(usuario);
            System.out.println("Usuario registrado correctamente.");
        } catch (UserDisplayableException e) {
            System.out.println(e.getMessage());
        }
        */
        
        launch(args);
    }
    
}
