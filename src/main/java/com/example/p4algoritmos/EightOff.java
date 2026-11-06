package com.example.p4algoritmos;

import com.example.p4algoritmos.Controlador.ControladorEightOff;
import javafx.application.Application;
import javafx.stage.Stage;


public class EightOff extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Eight Off");
        new ControladorEightOff(stage).mostrarMenu();
        stage.show();
    }
}