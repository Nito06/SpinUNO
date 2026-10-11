package modelo;

import java.util.Random;

public class Ruleta {
    
    // Las 8 secciones basadas en tu diseño
    private static final String[] REGLAS = {
        "Casi UNO",
        "Descartar por número",
        "Descartar por color",
        "Robar hasta azul/rojo",
        "Guerra",
        "Mostrar la mano",
        "Intercambio de manos",
        "Puntuación más baja"
    };

    private Random random;

    public Ruleta() {
        random = new Random();
    }

    // Genera un número aleatorio del 0 al 7 que representará en qué sección cayó la flecha
    public int girar() {
        return random.nextInt(8);
    }

    // Devuelve el texto de la regla según el número que cayó
    public String getRegla(int seccion) {
        if (seccion >= 0 && seccion < REGLAS.length) {
            return REGLAS[seccion];
        }
        return "Regla desconocida";
    }
}