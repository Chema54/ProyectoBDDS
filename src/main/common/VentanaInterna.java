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

        this.setPrefSize(150, 100);

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

                // Restaurar tamaño
                this.setPrefHeight(300);
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

                // Guardar posicion anterior
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

                // Restaurar
                this.setLayoutX(prevX);
                this.setLayoutY(prevY);

                this.setPrefWidth(prevW);
                this.setPrefHeight(prevH);
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
    }
}