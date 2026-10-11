package controlador;

import modelo.Juego;
import modelo.Carta;
import modelo.Jugador;
import vista.VentanaPrincipal;
import vista.PanelLobby;
import vista.PanelMesa;
import vista.PanelResultados;
import vista.DialogoRuleta;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {
    
    private Juego modelo;
    private VentanaPrincipal ventana;
    private PanelLobby panelLobby;
    private PanelMesa panelMesa;
    private PanelResultados panelResultados;

    public ControladorJuego(Juego modelo, VentanaPrincipal ventana, PanelLobby panelLobby, PanelMesa panelMesa, PanelResultados panelResultados) {
        this.modelo = modelo;
        this.ventana = ventana;
        this.panelLobby = panelLobby;
        this.panelMesa = panelMesa;
        this.panelResultados = panelResultados;

        // Iniciar Juego desde el Lobby
        this.panelLobby.addBtnIniciarListener(e -> iniciarJuego());

        // Jugar carta
        this.panelMesa.setCartaClickListener(indice -> intentarJugada(indice));

        // Robar del Mazo
        this.panelMesa.addMazoListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robarCarta();
            }
        });

        // Botón Ruleta y Botón UNO
        this.panelMesa.addRuletaListener(e -> girarRuleta());
        this.panelMesa.addBtnUnoListener(e -> gritarUno());

        // Botones de la Pantalla de Resultados
        this.panelResultados.addBtnVolverJugarListener(e -> {
            iniciarJuego();
        });

        this.panelResultados.addBtnSalirLobbyListener(e -> {
            ventana.mostrarPantalla("PANTALLA_LOBBY");
        });
    }

    private void iniciarJuego() {
        // Obtenemos los valores ingresados en los spinners del Lobby
        int numComodines = panelLobby.getCantidadComodines();
        int numAcciones = panelLobby.getCantidadAccion();

        modelo.iniciarPartida(numComodines, numAcciones);
        actualizarPantallaMesa();
        ventana.mostrarPantalla("PANTALLA_MESA");
    }

    private void intentarJugada(int indice) {
        Carta cartaAJugar = modelo.getJugadorActual().getMano().get(indice);
        
        boolean esComodin = (cartaAJugar.getColor() == Carta.Color.COMODIN); 
        boolean esCartaSpin = cartaAJugar.isEsSpin();
        
        boolean jugadaExitosa = modelo.jugarTurno(indice);
        
        if (jugadaExitosa) {
            if (esComodin) {
                Carta.Color nuevoColor = elegirColor();
                modelo.setColorActivo(nuevoColor);
                JOptionPane.showMessageDialog(ventana, "El nuevo color en juego es: " + nuevoColor);
            }

            if (esCartaSpin) {
                JOptionPane.showMessageDialog(ventana, 
                    "¡Has tirado una carta SPIN! Se activará la ruleta.", 
                    "Regla SPIN", 
                    JOptionPane.INFORMATION_MESSAGE);
                girarRuleta();
            }

            verificarGanador();
            actualizarPantallaMesa();
        } else {
            JOptionPane.showMessageDialog(ventana, 
                "Jugada inválida. Debes coincidir en color, número o usar un comodín.", 
                "Movimiento no permitido", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void girarRuleta() {
        DialogoRuleta dialogo = new DialogoRuleta(ventana);
        int resultadoSpin = dialogo.ejecutarGiro();

        aplicarEfectoRuleta(resultadoSpin);
        actualizarPantallaMesa();
        verificarGanador();
    }

    private void aplicarEfectoRuleta(int opcion) {
        Jugador jugadorActual = modelo.getJugadorActual();
        List<Jugador> todosJugadores = modelo.getJugadores();

        switch (opcion) {
            case 0: // 1. Casi UNO
                if (jugadorActual.getMano().size() > 1) {
                    Carta cartaConservada = jugadorActual.getMano().get(0);
                    jugadorActual.getMano().clear();
                    jugadorActual.getMano().add(cartaConservada);
                    JOptionPane.showMessageDialog(ventana, 
                        jugadorActual.getNombre() + " descarta su mano y se queda solo con 1 carta (¡Casi UNO!).");
                }
                break;

            case 1: // 2. Descartar por número
                Carta cartaMesa = modelo.getCartaEnMesa();
                int numMesa = cartaMesa.getNumero();
                if (numMesa >= 0) {
                    int antes = jugadorActual.getMano().size();
                    jugadorActual.getMano().removeIf(c -> c.getNumero() == numMesa);
                    int eliminadas = antes - jugadorActual.getMano().size();
                    JOptionPane.showMessageDialog(ventana, 
                        jugadorActual.getNombre() + " descartó " + eliminadas + " cartas con el número " + numMesa + ".");
                } else {
                    JOptionPane.showMessageDialog(ventana, "La carta de la mesa no es un número. No hay descartes.");
                }
                break;

            case 2: // 3. Descartar por color
                Carta.Color colorMesa = modelo.getCartaEnMesa().getColor();
                int antesColor = jugadorActual.getMano().size();
                jugadorActual.getMano().removeIf(c -> c.getColor() == colorMesa);
                int eliminadasColor = antesColor - jugadorActual.getMano().size();
                JOptionPane.showMessageDialog(ventana, 
                    jugadorActual.getNombre() + " descartó " + eliminadasColor + " cartas de color " + colorMesa + ".");
                break;

            case 3: // 4. Robar hasta Azul o Rojo
                int cartasRobadas = 0;
                while (true) {
                    int antes = jugadorActual.getMano().size();
                    modelo.robarCartaTurno();
                    if (jugadorActual.getMano().size() == antes) break;
                    cartasRobadas++;
                    Carta ultima = jugadorActual.getMano().get(jugadorActual.getMano().size() - 1);
                    if (ultima.getColor() == Carta.Color.AZUL || ultima.getColor() == Carta.Color.ROJO) {
                        break;
                    }
                }
                JOptionPane.showMessageDialog(ventana, 
                    jugadorActual.getNombre() + " robó " + cartasRobadas + " cartas hasta que le salió Azul o Rojo.");
                break;

            case 4: // 5. Guerra de cartas
                Jugador perdedor = todosJugadores.get(0);
                int menorValor = 999;
                for (Jugador j : todosJugadores) {
                    int suma = j.getMano().stream().mapToInt(c -> c.getNumero() > 0 ? c.getNumero() : 10).sum();
                    if (suma < menorValor) {
                        menorValor = suma;
                        perdedor = j;
                    }
                }
                for (int i = 0; i < 2; i++) {
                    modelo.robarCartaTurno();
                }
                JOptionPane.showMessageDialog(ventana, 
                    "¡Guerra de cartas! " + perdedor.getNombre() + " se lleva un castigo de 2 cartas.");
                break;

            case 5: // 6. Mostrar la mano
                StringBuilder manoTexto = new StringBuilder("Mano revelada de " + jugadorActual.getNombre() + ":\n\n");
                for (Carta c : jugadorActual.getMano()) {
                    manoTexto.append("• ").append(c.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(ventana, manoTexto.toString(), "Mano Revelada", JOptionPane.INFORMATION_MESSAGE);
                break;

            case 6: // 7. Intercambio de manos
                List<ArrayList<Carta>> manosAux = new ArrayList<>();
                for (Jugador j : todosJugadores) {
                    manosAux.add(new ArrayList<>(j.getMano()));
                }
                for (int i = 0; i < todosJugadores.size(); i++) {
                    int siguiente = (i + 1) % todosJugadores.size();
                    todosJugadores.get(siguiente).getMano().clear();
                    todosJugadores.get(siguiente).getMano().addAll(manosAux.get(i));
                }
                JOptionPane.showMessageDialog(ventana, "¡Intercambio de Manos! Todos los jugadores han rotado sus cartas.");
                break;

            case 7: // 8. Puntuación más baja
                Jugador menorPuntaje = todosJugadores.get(0);
                int minPts = 9999;
                for (Jugador j : todosJugadores) {
                    int pts = j.getMano().size();
                    if (pts < minPts) {
                        minPts = pts;
                        menorPuntaje = j;
                    }
                }
                for (int i = 0; i < 2; i++) {
                    modelo.robarCartaTurno();
                }
                JOptionPane.showMessageDialog(ventana, 
                    menorPuntaje.getNombre() + " tiene la puntuación más baja.");
                break;
        }
    }

    private void verificarGanador() {
        for (Jugador j : modelo.getJugadores()) {
            if (j.getMano().isEmpty()) {
                panelResultados.mostrarResultados(modelo.getJugadores());
                ventana.mostrarPantalla("PANTALLA_RESULTADOS");
                break;
            }
        }
    }

    private void gritarUno() {
        int cartasEnMano = modelo.getJugadorActual().getMano().size();
        if (cartasEnMano <= 2) {
            JOptionPane.showMessageDialog(ventana, 
                "¡" + modelo.getJugadorActual().getNombre() + " ha gritado UNO!", 
                "¡UNO!", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ventana, 
                "Aún tienes " + cartasEnMano + " cartas. ¡No puedes gritar UNO todavía!", 
                "Calma", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Carta.Color elegirColor() {
        String[] opciones = {"Rojo", "Azul", "Verde", "Amarillo"};
        int seleccion = JOptionPane.showOptionDialog(ventana,
                "Has jugado un Comodín.\nElige el nuevo color:",
                "Selección de Color",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        switch (seleccion) {
            case 0: return Carta.Color.ROJO;
            case 1: return Carta.Color.AZUL;
            case 2: return Carta.Color.VERDE;
            case 3: return Carta.Color.AMARILLO;
            default: return Carta.Color.ROJO; 
        }
    }

    private void robarCarta() {
        modelo.robarCartaTurno(); 
        actualizarPantallaMesa();
    }

    private void actualizarPantallaMesa() {
        panelMesa.actualizarDescarte(modelo.getCartaEnMesa());
        panelMesa.setTurnoActual(modelo.getJugadorActual().getNombre());
        panelMesa.dibujarMano(modelo.getJugadorActual().getMano());
    }
}