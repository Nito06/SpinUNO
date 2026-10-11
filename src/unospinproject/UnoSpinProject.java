package unospinproject;

import modelo.Juego;
import vista.VentanaPrincipal;
import vista.PanelLobby;
import vista.PanelMesa;
import vista.PanelResultados;
import controlador.ControladorJuego;

import java.util.ArrayList;
import java.util.Arrays;

public class UnoSpinProject {

    public static void main(String[] args) {
        // 1. Crear Ventana Principal
        VentanaPrincipal ventana = new VentanaPrincipal();

        // 2. Crear Vistas (Lobby, Mesa y Resultados)
        PanelLobby panelLobby = new PanelLobby();
        PanelMesa panelMesa = new PanelMesa();
        PanelResultados panelResultados = new PanelResultados();

        // 3. Registrar Pantallas en la Ventana
        ventana.agregarPantalla(panelLobby, "PANTALLA_LOBBY");
        ventana.agregarPantalla(panelMesa, "PANTALLA_MESA");
        ventana.agregarPantalla(panelResultados, "PANTALLA_RESULTADOS");

        // 4. Crear Modelo
        ArrayList<String> nombres = new ArrayList<>(Arrays.asList("Sofía", "Mateo", "Valeria", "Lucas"));
        Juego modelo = new Juego(nombres);

        // 5. Crear Controlador conectando modelo y las 3 vistas
        ControladorJuego controlador = new ControladorJuego(modelo, ventana, panelLobby, panelMesa, panelResultados);

        // 6. Mostrar Pantalla Inicial
        ventana.mostrarPantalla("PANTALLA_LOBBY");
        ventana.setVisible(true);
    }
}