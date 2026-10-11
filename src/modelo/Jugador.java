package modelo;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public ArrayList<Carta> getMano() { 
        return mano; 
    }

    // Añade una carta robada a la mano del jugador
    public void recibirCarta(Carta carta) {
        if (carta != null) {
            mano.add(carta);
        }
    }

    // Retira y devuelve una carta específica cuando el jugador la tira a la mesa
    public Carta jugarCarta(int indice) {
        if (indice >= 0 && indice < mano.size()) {
            return mano.remove(indice);
        }
        return null;
    }

    public int getCantidadCartas() {
        return mano.size();
    }

    // Regla clásica
    public boolean tieneUno() {
        return mano.size() == 1;
    }
}