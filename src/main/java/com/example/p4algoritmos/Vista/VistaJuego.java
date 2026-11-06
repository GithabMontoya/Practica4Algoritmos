package com.example.p4algoritmos.Vista;

import com.example.p4algoritmos.Modelo.CartaInglesa;
import com.example.p4algoritmos.Modelo.Juego;
import com.example.p4algoritmos.Modelo.Movimiento;
import com.example.p4algoritmos.Modelo.Palo;
import com.example.p4algoritmos.Modelo.Pila;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VistaJuego {

    public interface Escuchador {
        void alPulsarLugar(Pila<CartaInglesa> lugar, int pos);
        void alPulsarUndo();
        void alPulsarPista();
        void alPulsarSalir();
    }

    private static final String CARPETA = "cartas poker";
    private static final String[] EXTENSIONES = {".jpg"};

    private static final double ANCHO = 80;
    private static final double ALTO = 112;
    private static final double DESFASE = 24;

    private final Escuchador escuchador;
    private final Map<String, Image> imagenes = new HashMap<>();

    private final Scene escena;
    private final HBox filaCeldas = new HBox(10);
    private final HBox filaFundaciones = new HBox(10);
    private final HBox filaColumnas = new HBox(10);
    private final Label lblEstado = new Label();
    private final Label lblInfo = new Label();

    private Pila<CartaInglesa> seleccionada;
    private int cantidadSeleccionada;
    private Movimiento pista;

    public VistaJuego(Escuchador escuchador) {
        this.escuchador = escuchador;

        Button btnUndo = new Button("Undo");
        btnUndo.setOnAction(e -> escuchador.alPulsarUndo());
        Button btnPista = new Button("Pista");
        btnPista.setOnAction(e -> escuchador.alPulsarPista());
        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> escuchador.alPulsarSalir());

        lblEstado.setTextFill(Color.WHITE);
        lblEstado.setFont(Font.font("Arial", 15));
        lblInfo.setTextFill(Color.GOLD);
        lblInfo.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox superior = new HBox(30, filaCeldas, espacio, filaFundaciones);

        HBox botones = new HBox(12, btnUndo, btnPista, btnSalir, lblInfo);
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox raiz = new VBox(18, superior, filaColumnas, new VBox(6, botones, lblEstado));
        raiz.setPadding(new Insets(15));
        raiz.setStyle("-fx-background-color: #0b6623;");

        ScrollPane scroll = new ScrollPane(raiz);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #0b6623; -fx-background-color: #0b6623;");

        escena = new Scene(scroll, 1150, 780);
    }

    public Scene getEscena() {
        return escena;
    }

    public void mostrarVictoria() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Completaste el tablero");
        alerta.setHeaderText("Ganaste");
        alerta.showAndWait();
    }

    public void dibujar(Juego juego, Pila<CartaInglesa> seleccionada, int cantidadSeleccionada,
                        Movimiento pista, String mensaje) {
        this.seleccionada = seleccionada;
        this.cantidadSeleccionada = cantidadSeleccionada;
        this.pista = pista;

        dibujarCeldas(juego);
        dibujarFundaciones(juego);
        dibujarColumnas(juego);

        lblInfo.setText("Movimientos: " + juego.movimientos()
                + "   |   Celdas libres: " + juego.celdasLibres()
                + "   |   Cartas que puedes mover juntas: " + juego.maxMovibles());
        lblEstado.setText(mensaje);
    }

    private void dibujarCeldas(Juego juego) {
        filaCeldas.getChildren().clear();
        for (int i = 0; i < Juego.CELDAS; i++) {
            Pila<CartaInglesa> celda = juego.lugarCelda(i);
            CartaInglesa carta = juego.cima(celda);

            StackPane slot = espacioVacio(null);
            if (carta != null) {
                slot.getChildren().add(conEfecto(dibujoCarta(carta), celda == seleccionada, esOrigenDePista(celda), false));
            } else if (esDestinoDePista(celda)) {
                slot.setEffect(brillo(Color.LIMEGREEN));
            }
            slot.setOnMouseClicked(e -> escuchador.alPulsarLugar(celda, 0));
            filaCeldas.getChildren().add(slot);
        }
    }

    private void dibujarFundaciones(Juego juego) {
        filaFundaciones.getChildren().clear();
        for (int i = 0; i < Juego.FUNDACIONES; i++) {
            Pila<CartaInglesa> fundacion = juego.lugarFundacion(i);
            CartaInglesa cima = juego.cima(fundacion);

            StackPane slot = espacioVacio(simbolo(Palo.values()[i]));
            if (cima != null) slot.getChildren().add(dibujoCarta(cima));
            if (esDestinoDePista(fundacion)) slot.setEffect(brillo(Color.LIMEGREEN));
            slot.setOnMouseClicked(e -> escuchador.alPulsarLugar(fundacion, 0));
            filaFundaciones.getChildren().add(slot);
        }
    }

    private void dibujarColumnas(Juego juego) {
        filaColumnas.getChildren().clear();
        for (int i = 0; i < Juego.COLUMNAS; i++) {
            Pila<CartaInglesa> columna = juego.lugarColumna(i);
            int n = juego.cantidadEn(columna);

            Pane pane = new Pane();
            pane.setPrefSize(ANCHO, ALTO + Math.max(0, n - 1) * DESFASE);
            pane.setOnMouseClicked(e -> escuchador.alPulsarLugar(columna, -1));

            StackPane vacio = espacioVacio(null);
            if (n == 0 && esDestinoDePista(columna)) vacio.setEffect(brillo(Color.LIMEGREEN));
            pane.getChildren().add(vacio);

            for (int pos = 0; pos < n; pos++) {
                final int fila = pos;
                boolean estaSeleccionada = columna == seleccionada && pos >= n - cantidadSeleccionada;
                boolean origen = esOrigenDePista(columna) && pos >= n - pista.cantidad;
                boolean destino = esDestinoDePista(columna) && pos == n - 1;
                boolean oscura = !juego.esMovible(columna, pos);

                Node carta = conEfecto(dibujoCarta(juego.cartaEn(columna, pos)), estaSeleccionada, origen || destino, oscura);
                carta.setLayoutY(pos * DESFASE);
                carta.setOnMouseClicked(e -> {
                    escuchador.alPulsarLugar(columna, fila);
                    e.consume();
                });
                pane.getChildren().add(carta);
            }
            filaColumnas.getChildren().add(pane);
        }
    }

    private boolean esOrigenDePista(Pila<CartaInglesa> lugar) {
        return pista != null && pista.origen == lugar;
    }

    private boolean esDestinoDePista(Pila<CartaInglesa> lugar) {
        return pista != null && pista.destino == lugar;
    }

    private Node conEfecto(Node carta, boolean estaSeleccionada, boolean enPista, boolean oscura) {
        if (estaSeleccionada) {
            carta.setEffect(brillo(Color.GOLD));
        } else if (enPista) {
            carta.setEffect(brillo(Color.LIMEGREEN));
        } else if (oscura) {
            ColorAdjust oscurecer = new ColorAdjust();
            oscurecer.setBrightness(-0.5);
            carta.setEffect(oscurecer);
        }
        return carta;
    }

    private DropShadow brillo(Color color) {
        DropShadow sombra = new DropShadow(18, color);
        sombra.setSpread(0.6);
        return sombra;
    }

    private StackPane espacioVacio(String simbolo) {
        Rectangle marco = new Rectangle(ANCHO, ALTO);
        marco.setArcWidth(10);
        marco.setArcHeight(10);
        marco.setFill(Color.rgb(0, 0, 0, 0.18));
        marco.setStroke(Color.rgb(255, 255, 255, 0.55));
        marco.getStrokeDashArray().addAll(6.0, 5.0);

        StackPane espacio = new StackPane(marco);
        espacio.setMinSize(ANCHO, ALTO);
        espacio.setMaxSize(ANCHO, ALTO);
        if (simbolo != null) {
            Text texto = new Text(simbolo);
            texto.setFont(Font.font(34));
            texto.setFill(Color.rgb(255, 255, 255, 0.6));
            espacio.getChildren().add(texto);
        }
        return espacio;
    }

    private Node dibujoCarta(CartaInglesa carta) {
        Image imagen = imagenDe(carta);
        if (imagen != null) {
            ImageView vista = new ImageView(imagen);
            vista.setFitWidth(ANCHO);
            vista.setFitHeight(ALTO);
            return vista;
        }
        Rectangle fondo = new Rectangle(ANCHO, ALTO, Color.WHITE);
        fondo.setArcWidth(10);
        fondo.setArcHeight(10);
        fondo.setStroke(Color.BLACK);
        Text texto = new Text(nombreValor(carta.getValorBajo()) + "\n" + simbolo(carta.getPalo()));
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        texto.setFill(carta.getColor().equals("rojo") ? Color.RED : Color.BLACK);

        StackPane dibujo = new StackPane(fondo, texto);
        dibujo.setMinSize(ANCHO, ALTO);
        dibujo.setMaxSize(ANCHO, ALTO);
        return dibujo;
    }

    private String simbolo(Palo palo) {
        return palo.getFigura().replace("\uFE0E", "").replace("\uFE0F", "");
    }

    private String nombreValor(int valor) {
        switch (valor) {
            case 1:  return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(valor);
        }
    }

    private String nombrePalo(Palo palo) {
        switch (palo) {
            case DIAMANTE: return "DIAMANTE";
            case CORAZON:  return "CORAZON";
            case PICA:     return "PICAS";
            default:       return "TREBOL";
        }
    }

    private Image imagenDe(CartaInglesa carta) {
        String nombre = "CARTA " + carta.getValorBajo() + " " + nombrePalo(carta.getPalo());
        if (!imagenes.containsKey(nombre)) {
            imagenes.put(nombre, cargarImagen(nombre));
        }
        return imagenes.get(nombre);
    }

    private Image cargarImagen(String nombre) {
        for (String extension : EXTENSIONES) {
            String archivo = CARPETA + "/" + nombre + extension;

            InputStream flujo = VistaJuego.class.getResourceAsStream("/" + archivo);
            if (flujo != null) {
                return new Image(flujo, ANCHO * 2, ALTO * 2, false, true);
            }
            File f = new File(archivo);
            if (f.exists()) {
                return new Image(f.toURI().toString(), ANCHO * 2, ALTO * 2, false, true);
            }
        }
        System.out.println("No se encontró la imagen \"" + nombre + "\" en la carpeta \"" + CARPETA
                + "\" (directorio actual: " + new File(".").getAbsolutePath() + ")");
        return null;
    }
}