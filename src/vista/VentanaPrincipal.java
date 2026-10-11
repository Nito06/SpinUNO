package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    public VentanaPrincipal() {
        setTitle("UNO Spin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768); // Ajusta según la resolución de tus frames en Figma
        setLocationRelativeTo(null); 

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // El fondo base
        panelContenedor.setBackground(new Color(20, 25, 40)); 

        add(panelContenedor);
    }

    // Método para agregar las diferentes pantallas desde el controlador
    public void agregarPantalla(JPanel panel, String nombre) {
        panelContenedor.add(panel, nombre);
    }

    // Método para navegar entre pantallas
    public void mostrarPantalla(String nombre) {
        cardLayout.show(panelContenedor, nombre);
    }
}