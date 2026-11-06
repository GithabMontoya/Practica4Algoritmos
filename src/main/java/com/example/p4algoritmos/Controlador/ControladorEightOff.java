package com.example.p4algoritmos.Controlador;

import com.example.p4algoritmos.Modelo.CartaInglesa;
import com.example.p4algoritmos.Modelo.Juego;
import com.example.p4algoritmos.Modelo.Movimiento;
import com.example.p4algoritmos.Modelo.Pila;
import com.example.p4algoritmos.Vista.VistaJuego;
import com.example.p4algoritmos.Vista.VistaMenu;
import javafx.stage.Stage;

public class ControladorEightOff implements VistaJuego.Escuchador {

    private final Stage stage;
    private Juego juego;        // modelo
    private VistaJuego vista;   // vista

    private Pila<CartaInglesa> origen = null;
    private int cantidad = 0;

    private Movimiento pista = null;
    private String mensaje = "";

    public ControladorEightOff(Stage stage) {
        this.stage = stage;
    }


    public void mostrarMenu() {
        VistaMenu menu = new VistaMenu(this::iniciarJuego, stage::close);
        stage.setScene(menu.getEscena());
    }

    private void iniciarJuego() {
        juego = new Juego();
        vista = new VistaJuego(this);
        origen = null;
        cantidad = 0;
        pista = null;
        mensaje = "Selecciona una carta para moverla.";
        stage.setScene(vista.getEscena());
        redibujar();
    }

    private void redibujar() {
        vista.dibujar(juego, origen, cantidad, pista, mensaje);
    }

    @Override
    public void alPulsarUndo() {
        mensaje = juego.undo() ? "Movimiento deshecho." : "No hay movimientos que deshacer.";
        origen = null;
        pista = null;
        redibujar();
    }

    @Override
    public void alPulsarPista() {
        origen = null;
        pista = juego.siguientePista();
        mensaje = (pista == null) ? "No hay movimientos posibles, prueba con Undo."
                : "Pista: " + juego.describir(pista);
        redibujar();
    }

    @Override
    public void alPulsarSalir() {
        mostrarMenu();
    }

    @Override
    public void alPulsarLugar(Pila<CartaInglesa> lugar, int pos) {
        pista = null;
        boolean habiaSeleccion = (origen != null);

        if (habiaSeleccion) {
            if (lugar == origen) {
                origen = null;
                mensaje = "Selección cancelada.";
                redibujar();
                return;
            }
            if (juego.mover(new Movimiento(origen, cantidad, lugar))) {
                origen = null;
                mensaje = "Movimiento realizado.";
                redibujar();
                if (juego.gano()) {
                    vista.mostrarVictoria();
                    mostrarMenu();
                }
                return;
            }
        }

        origen = null;
        if (juego.esMovible(lugar, pos)) {
            origen = lugar;
            cantidad = juego.cantidadEn(lugar) - pos;
            mensaje = "Seleccionadas " + cantidad + " carta(s).";
        } else {
            mensaje = habiaSeleccion ? "Ese movimiento no es válido."
                    : "Selecciona una carta que se pueda mover.";
        }
        redibujar();
    }
}
