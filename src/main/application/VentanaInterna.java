package main.application; // Ajusta a tu paquete real

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class VentanaInterna extends BorderPane {

    private double xOffset = 0;
    private double yOffset = 0;

    public VentanaInterna(String titulo, Node contenido) {
        // Estilo básico de la ventana interna
        this.setStyle("-fx-background-color: #ffffff; -fx-border-color: #404040; -fx-border-width: 2px; -fx-background-radius: 4px; -fx-border-radius: 4px;");
        this.setPrefSize(400, 300); // Tamaño por defecto, puedes hacerlo dinámico

        // 1. Crear la barra de título
        HBox barraTitulo = new HBox();
        barraTitulo.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 5px;");
        barraTitulo.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        // Botón de cerrar
        Button btnCerrar = new Button("X");
        btnCerrar.setStyle("-fx-background-color: #ff5f56; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        
        // Al hacer clic en cerrar, removemos esta ventana del contenedor padre
        btnCerrar.setOnAction(e -> {
            Pane escritorioPadre = (Pane) this.getParent();
            if (escritorioPadre != null) {
                escritorioPadre.getChildren().remove(this);
            }
        });

        // Organizar elementos en la barra de título
        Pane espaciador = new Pane();
        HBox.setHgrow(espaciador, javafx.scene.layout.Priority.ALWAYS);
        barraTitulo.getChildren().addAll(lblTitulo, espaciador, btnCerrar);

        // 2. Hacer la ventana arrastrable mediante la barra de título
        barraTitulo.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - this.getLayoutX();
            yOffset = event.getSceneY() - this.getLayoutY();
            this.toFront(); // Traer al frente al hacer clic
        });

        barraTitulo.setOnMouseDragged(event -> {
            this.setLayoutX(event.getSceneX() - xOffset);
            this.setLayoutY(event.getSceneY() - yOffset);
        });
        
        // Traer al frente también si hacen clic en el contenido
        this.setOnMousePressed(event -> this.toFront());

        // 3. Ensamblar la ventana interna
        this.setTop(barraTitulo);
        this.setCenter(contenido);
        BorderPane.setMargin(contenido, new Insets(10));
    }
}