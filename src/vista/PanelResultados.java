package vista;

import modelo.Jugador;
import modelo.Carta;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PanelResultados extends JPanel {

    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton btnVolverJugar;
    private JButton btnSalirLobby;

    public PanelResultados() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Fondo claro de la interfaz
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- ENCABEZADO ---
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 0, 5));
        panelHeader.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Resultados y Clasificación");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(20, 25, 35));

        JLabel lblSubtitulo = new JLabel("Puntuaciones finales basadas en el conteo de cartas restantes de cada jugador.");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 110, 120));

        panelHeader.add(lblTitulo);
        panelHeader.add(lblSubtitulo);
        add(panelHeader, BorderLayout.NORTH);

        // --- TABLA DE RESULTADOS ---
        String[] columnas = {"Posición", "Nombre del Jugador", "Cartas Restantes", "Puntuación Final", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setRowHeight(45);
        tablaResultados.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaResultados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaResultados.getTableHeader().setBackground(new Color(230, 235, 240));
        tablaResultados.setShowGrid(false);
        tablaResultados.setIntercellSpacing(new Dimension(0, 0));

        // Centrar el contenido de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaResultados.getColumnCount(); i++) {
            tablaResultados.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Leyenda y Botones) ---
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);

        JLabel lblLeyenda = new JLabel("<html><b>CÁLCULO DE PUNTUACIÓN DE LA MESA:</b><br>"
                + "• Cartas numéricas valen su propio número. • Cartas de acción valen 20 puntos. • Comodines valen 50 puntos.</html>");
        lblLeyenda.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLeyenda.setForeground(new Color(120, 130, 140));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setOpaque(false);

        btnVolverJugar = new JButton("Volver a jugar");
        btnVolverJugar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolverJugar.setBackground(new Color(15, 23, 42));
        btnVolverJugar.setForeground(Color.WHITE);
        btnVolverJugar.setFocusPainted(false);

        btnSalirLobby = new JButton("Salir al Lobby");
        btnSalirLobby.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalirLobby.setBackground(Color.WHITE);
        btnSalirLobby.setForeground(new Color(15, 23, 42));
        btnSalirLobby.setFocusPainted(false);

        panelBotones.add(btnVolverJugar);
        panelBotones.add(btnSalirLobby);

        panelInferior.add(lblLeyenda, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Calcula puntos y llena la tabla según el estado final de las manos
    public void mostrarResultados(List<Jugador> jugadores) {
        modeloTabla.setRowCount(0);

        List<Jugador> ranking = new ArrayList<>(jugadores);
        Collections.sort(ranking, Comparator.comparingInt(j -> j.getMano().size()));

        for (int i = 0; i < ranking.size(); i++) {
            Jugador j = ranking.get(i);
            int cartasRestantes = j.getMano().size();
            int puntos = calcularPuntosMano(j.getMano());
            
            String posicion = (i + 1) + "°";
            String estado = (i == 0) ? "GANADOR" : "FINALIZADO";

            modeloTabla.addRow(new Object[]{
                posicion,
                j.getNombre(),
                cartasRestantes + " cartas",
                puntos + " pts",
                estado
            });
        }
    }

    private int calcularPuntosMano(List<Carta> mano) {
        int suma = 0;
        for (Carta c : mano) {
            if (c.getTipo() == Carta.Tipo.NUMERO) {
                suma += c.getNumero();
            } else if (c.getTipo() == Carta.Tipo.REVERSA || c.getTipo() == Carta.Tipo.SALTA || c.getTipo() == Carta.Tipo.TOMA_DOS) {
                suma += 20;
            } else if (c.getTipo() == Carta.Tipo.CAMBIO_COLOR || c.getTipo() == Carta.Tipo.TOMA_CUATRO) {
                suma += 50;
            }
        }
        return suma;
    }

    public void addBtnVolverJugarListener(ActionListener l) {
        btnVolverJugar.addActionListener(l);
    }

    public void addBtnSalirLobbyListener(ActionListener l) {
        btnSalirLobby.addActionListener(l);
    }
}