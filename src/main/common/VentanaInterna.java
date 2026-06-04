package main.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class VentanaInterna extends BorderPane {

    private double xOffset = 0;
    private double yOffset = 0;
    private boolean minimizada = false;
    private boolean maximizada = false;

    // Guardaremos el tamaño base para restaurar correctamente
    private final double widthBase = 800;
    private final double heightBase = 600;

    private double prevX;
    private double prevY;
    private double prevW;
    private double prevH;

    private Node contenido;

    public VentanaInterna(String titulo, Node contenido) {

        this.contenido = contenido;
        
        // =========================
        // ESTILO GENERAL
        // =========================

        this.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #404040;" +
            "-fx-border-width: 2px;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;"
        );

        // AQUI ESTA LA CLAVE: Hacerla más grande desde el principio
        this.setPrefSize(widthBase, heightBase);

        // =========================
        // BARRA TITULO
        // =========================

        HBox barraTitulo = new HBox();
        barraTitulo.setAlignment(Pos.CENTER_LEFT);
        barraTitulo.setPadding(new Insets(5));
        barraTitulo.setStyle(
            "-fx-background-color: #2b2b2b;" +
            "-fx-background-radius: 4px 4px 0 0;"
        );
        
        // =========================
        // TITULO
        // =========================

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        
        // =========================
        // ESPACIADOR
        // =========================

        Pane espaciador = new Pane();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        // =========================
        // BOTON MINIMIZAR
        // =========================

        Button btnMin = new Button("—");
        btnMin.setStyle(
            "-fx-background-color: #fdbc40;" +
            "-fx-text-fill: black;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;"
        );
        btnMin.setOnAction(e -> {
            minimizada = !minimizada;
            if (minimizada) {
                // Oculta contenido
                this.setCenter(null);
                // Solo dejar visible la barra
                this.setPrefHeight(35);
                this.setMinHeight(35);
                this.setMaxHeight(35);
            } else {
                // Restaurar contenido
                this.setCenter(contenido);
                // Restaurar tamaño a los valores actuales
                this.setPrefHeight(maximizada ? Region.USE_COMPUTED_SIZE : prevH != 0 ? prevH : heightBase);
                this.setMinHeight(Region.USE_COMPUTED_SIZE);
                this.setMaxHeight(Region.USE_COMPUTED_SIZE);
            }
        });
        
        // =========================
        // BOTON MAXIMIZAR
        // =========================

        Button btnMax = new Button("□");
        btnMax.setStyle(
            "-fx-background-color: #28c840;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;"
        );
        btnMax.setOnAction(e -> {
            Pane escritorioPadre = (Pane) this.getParent();
            if (escritorioPadre == null) {
                return;
            }

            maximizada = !maximizada;

            if (maximizada) {
                // Guardar posicion anterior antes de maximizar
                prevX = this.getLayoutX();
                prevY = this.getLayoutY();
                prevW = this.getWidth();
                prevH = this.getHeight();

                // Maximizar
                this.setLayoutX(0);
                this.setLayoutY(0);
                this.setPrefWidth(escritorioPadre.getWidth());
                this.setPrefHeight(escritorioPadre.getHeight());

            } else {
                // Restaurar a los valores guardados, o a los default si por alguna razón falló
                this.setLayoutX(prevX != 0 ? prevX : 50);
                this.setLayoutY(prevY != 0 ? prevY : 50);
                this.setPrefWidth(prevW != 0 ? prevW : widthBase);
                this.setPrefHeight(prevH != 0 ? prevH : heightBase);
            }
        });
        
        // =========================
        // BOTON CERRAR
        // =========================

        Button btnCerrar = new Button("X");
        btnCerrar.setStyle(
            "-fx-background-color: #ff5f56;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;"
        );
        btnCerrar.setOnAction(e -> {
            Pane escritorioPadre = (Pane) this.getParent();
            if (escritorioPadre != null) {
                escritorioPadre.getChildren().remove(this);
            }
        });
        
        // =========================
        // ENSAMBLAR BARRA
        // =========================

        barraTitulo.getChildren().addAll(
            lblTitulo,
            espaciador,
            btnMin,
            btnMax,
            btnCerrar
        );
        
        // =========================
        // DRAG
        // =========================

        barraTitulo.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - this.getLayoutX();
            yOffset = event.getSceneY() - this.getLayoutY();
            this.toFront();
        });
        
        barraTitulo.setOnMouseDragged(event -> {
            if (!maximizada) {
                this.setLayoutX(event.getSceneX() - xOffset);
                this.setLayoutY(event.getSceneY() - yOffset);
            }
        });
        
        // Traer al frente
        this.setOnMousePressed(event -> this.toFront());
        
        // =========================
        // CONTENIDO
        // =========================

        this.setTop(barraTitulo);
        this.setCenter(contenido);
        BorderPane.setMargin(contenido, new Insets(10));
        
        // =========================
        // DRAG PARA REDIMENSIONAR (RESIZE HANDLE)
        // =========================
        Label resizeHandle = new Label(" ↘ ");
        resizeHandle.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 16px; -fx-cursor: se-resize;");
        
        HBox bottomPane = new HBox(resizeHandle);
        bottomPane.setAlignment(Pos.BOTTOM_RIGHT);
        bottomPane.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 0 0 4px 4px;");

        final double[] dragDelta = new double[2];
        
        resizeHandle.setOnMousePressed(e -> {
            // Guardamos la diferencia entre el tamaño actual y donde hizo clic el usuario
            dragDelta[0] = this.getPrefWidth() - e.getScreenX();
            dragDelta[1] = this.getPrefHeight() - e.getScreenY();
        });
        
        resizeHandle.setOnMouseDragged(e -> {
            if (!maximizada) {
                // Calculamos el nuevo tamaño mientras arrastra
                double newWidth = e.getScreenX() + dragDelta[0];
                double newHeight = e.getScreenY() + dragDelta[1];
                
                // Evitamos que la ventana se haga demasiado pequeña
                if (newWidth > 300) this.setPrefWidth(newWidth);
                if (newHeight > 200) this.setPrefHeight(newHeight);
            }
        });

        // Agregamos la barra inferior a nuestra ventana
        this.setBottom(bottomPane); 
    }
}