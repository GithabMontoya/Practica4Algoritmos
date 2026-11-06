package com.example.p4algoritmos.Vista;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class VistaMenu {

    private final Scene escena;

    public VistaMenu(Runnable alJugar, Runnable alSalir) {
        Text titulo = new Text("EIGHT OFF");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        titulo.setFill(Color.WHITE);

        Button btnJugar = botonGrande("Jugar");
        btnJugar.setOnAction(e -> alJugar.run());
        Button btnSalir = botonGrande("Salir");
        btnSalir.setOnAction(e -> alSalir.run());

        VBox menu = new VBox(25, titulo, btnJugar, btnSalir);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: #0b6623;");

        escena = new Scene(menu, 1150, 780);
    }

    public Scene getEscena() {
        return escena;
    }

    private Button botonGrande(String texto) {
        Button b = new Button(texto);
        b.setPrefWidth(220);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        return b;
    }
}
