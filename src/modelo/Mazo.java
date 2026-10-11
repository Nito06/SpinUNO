package modelo;

import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    
    private ArrayList<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        inicializarMazoPersonalizado(4, 4); // Valores iniciales por defecto
    }

    public void inicializarMazoPersonalizado(int numComodines, int numAccion) {
        cartas.clear();
        Carta.Color[] colores = {Carta.Color.AMARILLO, Carta.Color.AZUL, Carta.Color.ROJO, Carta.Color.VERDE};

        for (Carta.Color color : colores) {
            // 1. Carta Número 0 (Una por color)
            cartas.add(new Carta(color, Carta.Tipo.NUMERO, 0, false));

            // 2. Cartas del 1 al 9 (Dos de cada una)
            for (int j = 1; j <= 9; j++) {
                boolean esSpin = (j >= 1 && j <= 5);
                cartas.add(new Carta(color, Carta.Tipo.NUMERO, j, esSpin));
                cartas.add(new Carta(color, Carta.Tipo.NUMERO, j, esSpin));
            }

            // 3. Cartas de Acción dinámicas por color
            for (int a = 0; a < Math.max(1, numAccion / 2); a++) {
                cartas.add(new Carta(color, Carta.Tipo.REVERSA, -1, false));
                cartas.add(new Carta(color, Carta.Tipo.SALTA, -1, false));
                cartas.add(new Carta(color, Carta.Tipo.TOMA_DOS, -1, false));
            }
        }

        // 4. Comodines dinámicos (Cambio de Color y Toma Cuatro)
        for (int c = 0; c < Math.max(1, numComodines / 2); c++) {
            cartas.add(new Carta(Carta.Color.COMODIN, Carta.Tipo.CAMBIO_COLOR, -1, false));
            cartas.add(new Carta(Carta.Color.COMODIN, Carta.Tipo.TOMA_CUATRO, -1, false));
        }

        barajar();
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null; 
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }
}