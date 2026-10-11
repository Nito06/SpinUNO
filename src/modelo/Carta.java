package modelo;

public class Carta {

    public void setColor(String nuevoColor) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Opciones limitadas para colores y tipos
    public enum Color { ROJO, AZUL, VERDE, AMARILLO, COMODIN }
    public enum Tipo { NUMERO, SALTA, REVERSA, TOMA_DOS, CAMBIO_COLOR, TOMA_CUATRO }

    private Color color;
    private Tipo tipo;
    private int numero; // Del 0 al 9. Si es de acción, usamos -1.
    private boolean esSpin; // Verdadero si es del 1 al 5

    public Carta(Color color, Tipo tipo, int numero, boolean esSpin) {
        this.color = color;
        this.tipo = tipo;
        this.numero = numero;
        this.esSpin = esSpin;
    }

    // Getters lógicos
    public Color getColor() { return color; }
    public Tipo getTipo() { return tipo; }
    public int getNumero() { return numero; }
    public boolean isEsSpin() { return esSpin; }

    // Método que usará la Interfaz Gráfica para saber qué imagen cargar
    public String getNombreArchivo() {
        return generarNombreArchivo();
    }

// Genera el nombre del archivo basado en las imágenes de tu carpeta
    private String generarNombreArchivo() {
        String nombreColor = "";
        
        switch (color) {
            case ROJO: nombreColor = "rojo"; break;
            case AZUL: nombreColor = "azul"; break;
            case VERDE: nombreColor = "verde"; break;
            case AMARILLO: nombreColor = "amarillo"; break;
            // Usamos rojo como base para los comodines porque así venían en tus recortes
            case COMODIN: nombreColor = "rojo"; break; 
        }

        if (tipo == Tipo.NUMERO) {
            return nombreColor + "_numero_" + numero + ".png";
        } else if (tipo == Tipo.REVERSA) {
            return nombreColor + "_cambio_sentido.png";
        } else if (tipo == Tipo.SALTA) {
            return nombreColor + "_saltar_turno.png";
        } else if (tipo == Tipo.TOMA_DOS) {
            return nombreColor + "_roba_2.png";
        } else if (tipo == Tipo.CAMBIO_COLOR) {
            return nombreColor + "_comodin_cambio_color.png";
        } else if (tipo == Tipo.TOMA_CUATRO) {
            // AQUÍ ESTABA EL DETALLE: Agregamos "_copia" para que coincida con tu imagen
            return nombreColor + "_comodin_roba_4_copia.png";
        }
        
        return "reverso.png"; 
    }

    // Para ver en la consola qué carta es
    @Override
    public String toString() {
        if (tipo == Tipo.NUMERO) {
            return "Carta [" + color + " " + numero + (esSpin ? " (SPIN)" : "") + "]";
        }
        return "Carta [" + color + " " + tipo + "]";
    }
}