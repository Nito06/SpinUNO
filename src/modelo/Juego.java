package modelo;

import java.util.ArrayList;
import java.util.List;

public class Juego {

    private List<Jugador> jugadores;
    private Mazo mazo;
    private Carta cartaEnMesa;
    private Carta.Color colorActivo;
    private int turnoActualIndex;
    private int sentido = 1; // 1 = Horario, -1 = Antihorario

    public Juego(List<String> nombresJugadores) {
        this.jugadores = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            this.jugadores.add(new Jugador(nombre));
        }
        this.mazo = new Mazo();
    }

    public void iniciarPartida() {
        iniciarPartida(4, 4);
    }

    public void iniciarPartida(int comodines, int acciones) {
        sentido = 1; // Reiniciar dirección al inicio
        mazo.inicializarMazoPersonalizado(comodines, acciones);

        // Repartir 7 cartas a cada jugador
        for (Jugador j : jugadores) {
            j.getMano().clear();
            for (int i = 0; i < 7; i++) {
                Carta c = mazo.robarCarta();
                if (c != null) j.getMano().add(c);
            }
        }

        // Colocar la primera carta en mesa
        do {
            cartaEnMesa = mazo.robarCarta();
        } while (cartaEnMesa != null && cartaEnMesa.getColor() == Carta.Color.COMODIN);

        if (cartaEnMesa != null) {
            colorActivo = cartaEnMesa.getColor();
        }
        turnoActualIndex = 0;
    }

    public boolean jugarTurno(int indiceCarta) {
        Jugador jugador = getJugadorActual();
        if (indiceCarta < 0 || indiceCarta >= jugador.getMano().size()) {
            return false;
        }

        Carta carta = jugador.getMano().get(indiceCarta);

        // Validar si la carta se puede tirar
        boolean esValida = (carta.getColor() == Carta.Color.COMODIN) ||
                           (carta.getColor() == colorActivo) ||
                           (carta.getTipo() == Carta.Tipo.NUMERO && carta.getNumero() == cartaEnMesa.getNumero() && cartaEnMesa.getNumero() != -1) ||
                           (carta.getTipo() != Carta.Tipo.NUMERO && carta.getTipo() == cartaEnMesa.getTipo());

        if (esValida) {
            jugador.getMano().remove(indiceCarta);
            cartaEnMesa = carta;
            
            if (carta.getColor() != Carta.Color.COMODIN) {
                colorActivo = carta.getColor();
            }

            // --- EFECTOS DE CARTAS ESPECIALES Y COMODINES ---
            if (carta.getTipo() == Carta.Tipo.REVERSA) {
                sentido *= -1; // Invierte el sentido del juego
                if (jugadores.size() == 2) {
                    siguienteTurno(); // En partidas de 2 jugadores funciona como Salto
                }
            } else if (carta.getTipo() == Carta.Tipo.SALTA) {
                siguienteTurno(); // Salta al siguiente jugador
            } else if (carta.getTipo() == Carta.Tipo.TOMA_DOS) {
                siguienteTurno(); // Pasa al jugador afectado
                robarVariasCartas(getJugadorActual(), 2);
            } else if (carta.getTipo() == Carta.Tipo.TOMA_CUATRO) {
                siguienteTurno(); // Pasa al jugador afectado
                robarVariasCartas(getJugadorActual(), 4);
            }

            siguienteTurno();
            return true;
        }

        return false;
    }

    private void robarVariasCartas(Jugador jugador, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Carta c = mazo.robarCarta();
            if (c != null) {
                jugador.getMano().add(c);
            }
        }
    }

    public void robarCartaTurno() {
        Carta c = mazo.robarCarta();
        if (c != null) {
            getJugadorActual().getMano().add(c);
        }
        siguienteTurno();
    }

    private void siguienteTurno() {
        // Mueve el turno respetando si el sentido es horario (+1) o antihorario (-1)
        turnoActualIndex = (turnoActualIndex + sentido + jugadores.size()) % jugadores.size();
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActualIndex);
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Carta getCartaEnMesa() {
        return cartaEnMesa;
    }

    public Carta.Color getColorActivo() {
        return colorActivo;
    }

    public void setColorActivo(Carta.Color colorActivo) {
        this.colorActivo = colorActivo;
    }

    public Mazo getMazo() {
        return mazo;
    }
}