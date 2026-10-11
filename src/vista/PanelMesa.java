package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import modelo.Carta;

public class PanelMesa extends JPanel {
    
    private JPanel panelMano;
    private JLabel lblDescarte;
    private JLabel lblMazo;
    private JButton btnRuleta;
    private JButton btnUno;
    private JLabel lblTurno;

    public interface CartaClickListener {
        void onCartaClick(int indice);
    }
    private CartaClickListener cartaListener;

    public void setCartaClickListener(CartaClickListener listener) {
        this.cartaListener = listener;
    }

    public PanelMesa() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 40)); 

        // --- 1. HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitulo = new JLabel("<html><font color='white'><b>UNO SPIN</b> - MESA DE JUEGO </font><span style='background-color:#28a745; color:white; font-size:10px;'> EN CURSO </span></html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        lblTurno = new JLabel("Turno actual: Esperando...");
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblTurno, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- 2. CENTRO ---
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20); 
        
        // Mazo para robar (con cursor de manita)
        lblMazo = crearEtiquetaCarta("reverso.png");
        lblMazo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Pila de Descarte 
        lblDescarte = crearEtiquetaCarta("reverso.png"); 
        
        // Botón Ruleta (¡Colores arreglados!)
        btnRuleta = new JButton("RULETA SPIN");
        btnRuleta.setBackground(new Color(40, 50, 70));
        btnRuleta.setForeground(Color.WHITE);
        btnRuleta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRuleta.setFocusPainted(false);
        btnRuleta.setOpaque(true);           // Obliga a pintar el fondo
        btnRuleta.setBorderPainted(false);   // Quita el borde nativo de Windows
        btnRuleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRuleta.setPreferredSize(new Dimension(150, 150));
        
        panelCentro.add(lblMazo, gbc);
        panelCentro.add(lblDescarte, gbc);
        panelCentro.add(btnRuleta, gbc);
        
        add(panelCentro, BorderLayout.CENTER);

        // --- 3. SUR ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        JLabel lblTusCartas = new JLabel("Tus cartas en mano");
        lblTusCartas.setForeground(new Color(200, 200, 200));
        lblTusCartas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelSur.add(lblTusCartas, BorderLayout.NORTH);
        
        panelMano = new JPanel(new FlowLayout(FlowLayout.CENTER, -30, 10));
        panelMano.setOpaque(false);
        panelSur.add(panelMano, BorderLayout.CENTER);
        
        // Botón UNO (¡Colores arreglados!)
        btnUno = new JButton("UNO");
        btnUno.setBackground(new Color(220, 53, 69)); 
        btnUno.setForeground(Color.WHITE);
        btnUno.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnUno.setPreferredSize(new Dimension(80, 80)); 
        btnUno.setFocusPainted(false);
        btnUno.setOpaque(true);
        btnUno.setBorderPainted(false);
        btnUno.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSur.add(btnUno, BorderLayout.EAST);
        
        add(panelSur, BorderLayout.SOUTH);
    }

    private JLabel crearEtiquetaCarta(String nombreArchivo) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(100, 150)); 
        try {
            URL ruta = getClass().getResource("/recursos/imagenes/" + nombreArchivo);
            if (ruta != null) {
                ImageIcon iconoOriginal = new ImageIcon(ruta);
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imagenEscalada));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return label;
    }

    public void dibujarMano(ArrayList<Carta> cartasJugador) {
        panelMano.removeAll(); 
        for (int i = 0; i < cartasJugador.size(); i++) {
            Carta c = cartasJugador.get(i);
            JLabel lblCarta = crearEtiquetaCarta(c.getNombreArchivo());
            lblCarta.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            final int index = i; 
            lblCarta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (cartaListener != null) {
                        cartaListener.onCartaClick(index);
                    }
                }
            });
            panelMano.add(lblCarta);
        }
        panelMano.revalidate();
        panelMano.repaint();
    }
    
    public void actualizarDescarte(Carta cartaEnMesa) {
        lblDescarte.setIcon(crearEtiquetaCarta(cartaEnMesa.getNombreArchivo()).getIcon());
    }

    public void setTurnoActual(String nombre) {
        lblTurno.setText("Turno actual: " + nombre);
    }

    // --- NUEVOS MÉTODOS PARA CONECTAR EL CONTROLADOR ---
    public void addMazoListener(MouseAdapter listener) {
        lblMazo.addMouseListener(listener);
    }

    public void addRuletaListener(ActionListener listener) {
        btnRuleta.addActionListener(listener);
    }
    
    public void addBtnUnoListener(java.awt.event.ActionListener listener) {
        // Asegúrate de que el nombre de tu botón coincida, si le pusiste otro nombre (ej. botonUno), cámbialo aquí:
        btnUno.addActionListener(listener); 
    }
}