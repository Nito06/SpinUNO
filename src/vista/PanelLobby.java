package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelLobby extends JPanel {

    private JSpinner spinnerComodines;
    private JSpinner spinnerAcciones;
    private JSpinner spinnerTiempo;
    private JButton btnIniciar;

    public PanelLobby() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // --- HEADER ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblLogo = new JLabel("UNO SPIN  LOBBY");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(new Color(15, 23, 42));

        JLabel lblCodigo = new JLabel("Código de sala: SPIN-992");
        lblCodigo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblCodigo.setForeground(new Color(100, 116, 139));

        panelHeader.add(lblLogo, BorderLayout.WEST);
        panelHeader.add(lblCodigo, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- CENTRO (2 COLUMNAS) ---
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 25, 0));
        panelCentro.setOpaque(false);

        // Columna Izquierda: Reglas del Juego
        JPanel panelReglas = new JPanel();
        panelReglas.setLayout(new BoxLayout(panelReglas, BoxLayout.Y_AXIS));
        panelReglas.setBackground(Color.WHITE);
        panelReglas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTituloReglas = new JLabel("Reglas del Juego");
        lblTituloReglas.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloReglas.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelReglas.add(lblTituloReglas);
        panelReglas.add(Box.createVerticalStrut(20));

        // Spinners de Ajuste
        spinnerComodines = crearFilaAjuste(panelReglas, "Cantidad de Comodines (1 a 8)", 4, 1, 8);
        spinnerAcciones = crearFilaAjuste(panelReglas, "Cantidad de Cartas de Acción (1 a 8)", 4, 1, 8);
        spinnerTiempo = crearFilaAjuste(panelReglas, "Tiempo máximo de ruleta (segundos)", 15, 5, 60);

        // Mensaje Regla Spin
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(241, 245, 249));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfoTitle = new JLabel("REGLA SPIN ACTIVA:");
        lblInfoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        lblInfoTitle.setForeground(new Color(71, 85, 105));

        JLabel lblInfoDesc = new JLabel("<html>Al jugar una carta del 1 al 5 (Spin), se activará automáticamente la ruleta.</html>");
        lblInfoDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfoDesc.setForeground(new Color(100, 116, 139));

        panelInfo.add(lblInfoTitle);
        panelInfo.add(Box.createVerticalStrut(4));
        panelInfo.add(lblInfoDesc);

        panelReglas.add(Box.createVerticalGlue());
        panelReglas.add(panelInfo);

        // Columna Derecha: Jugadores Conectados
        JPanel panelJugadores = new JPanel(new BorderLayout(0, 15));
        panelJugadores.setBackground(Color.WHITE);
        panelJugadores.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTituloJugadores = new JLabel("Jugadores Conectados (4/4)");
        lblTituloJugadores.setFont(new Font("Arial", Font.BOLD, 16));
        panelJugadores.add(lblTituloJugadores, BorderLayout.NORTH);

        JPanel panelLista = new JPanel(new GridLayout(4, 1, 0, 10));
        panelLista.setOpaque(false);
        panelLista.add(crearItemJugador("Sofía", new Color(59, 130, 246)));
        panelLista.add(crearItemJugador("Mateo", new Color(239, 68, 68)));
        panelLista.add(crearItemJugador("Valeria", new Color(34, 197, 94)));
        panelLista.add(crearItemJugador("Lucas", new Color(234, 179, 8)));

        panelJugadores.add(panelLista, BorderLayout.CENTER);

        btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 15));
        btnIniciar.setBackground(new Color(15, 23, 42));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(0, 45));

        panelJugadores.add(btnIniciar, BorderLayout.SOUTH);

        panelCentro.add(panelReglas);
        panelCentro.add(panelJugadores);

        add(panelCentro, BorderLayout.CENTER);
    }

    private JSpinner crearFilaAjuste(JPanel panelPadre, String texto, int valor, int min, int max) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(1000, 40));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(valor, min, max, 1));
        spinner.setPreferredSize(new Dimension(60, 30));

        fila.add(lbl, BorderLayout.WEST);
        fila.add(spinner, BorderLayout.EAST);

        panelPadre.add(fila);
        panelPadre.add(Box.createVerticalStrut(15));
        return spinner;
    }

    private JPanel crearItemJugador(String nombre, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(248, 250, 252));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblColor = new JLabel("■ Color Asignado");
        lblColor.setFont(new Font("Arial", Font.PLAIN, 12));
        lblColor.setForeground(color);

        p.add(lblNombre, BorderLayout.WEST);
        p.add(lblColor, BorderLayout.EAST);
        return p;
    }

    public void addBtnIniciarListener(ActionListener l) {
        btnIniciar.addActionListener(l);
    }

    public int getCantidadComodines() {
        return (int) spinnerComodines.getValue();
    }

    public int getCantidadAccion() {
        return (int) spinnerAcciones.getValue();
    }

    public int getTiempoRuleta() {
        return (int) spinnerTiempo.getValue();
    }
}