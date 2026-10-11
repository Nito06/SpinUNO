package vista;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DialogoRuleta extends JDialog {

    private JList<String> listaOpciones;
    private DefaultListModel<String> modeloLista;
    private JLabel lblDescripcion;
    private JButton btnAplicar;
    private int opcionFinal = -1;

    private final String[] OPCIONES = {
        "1. Casi UNO (Quedarse con 1 carta)",
        "2. Descartar por número",
        "3. Descartar por color",
        "4. Robar hasta Azul/Rojo",
        "5. Guerra de cartas",
        "6. Mostrar la mano a todos",
        "7. Intercambio de manos",
        "8. Puntuación más baja"
    };

    private final String[] DESCRIPCIONES = {
        "El jugador activo descarta sus cartas hasta quedarse solo con 1 en la mano.",
        "El jugador puede descartar todas las cartas de su mano que tengan el mismo número.",
        "El jugador puede descartar todas las cartas que coincidan con el color activo en la mesa.",
        "El jugador debe robar cartas del mazo continuamente hasta que le salga una azul o roja.",
        "Todos muestran su carta más alta; el jugador con el menor valor debe robar 2 cartas.",
        "El jugador muestra todas las cartas de su mano a los demás participantes.",
        "Todos los jugadores pasan su mano de cartas al jugador ubicado a su izquierda.",
        "El jugador con menor puntuación acumulada en mano hace que todos los demás roben 2 cartas."
    };

    public DialogoRuleta(Frame parent) {
        super(parent, "¡GIRO SPIN DE LA RULETA!", true);
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 247, 250));

        // --- ENCABEZADO ---
        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEncabezado.setBackground(new Color(220, 38, 38));
        
        JLabel lblTitulo = new JLabel("¡GIRO SPIN DE LA RULETA!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo);
        add(panelEncabezado, BorderLayout.NORTH);

        // --- LISTA DE OPCIONES ---
        modeloLista = new DefaultListModel<>();
        for (String opcion : OPCIONES) {
            modeloLista.addElement(opcion);
        }

        listaOpciones = new JList<>(modeloLista);
        listaOpciones.setFont(new Font("Arial", Font.BOLD, 13));
        listaOpciones.setSelectionBackground(new Color(220, 38, 38));
        listaOpciones.setSelectionForeground(Color.WHITE);
        listaOpciones.setFixedCellHeight(30);

        JScrollPane scroll = new JScrollPane(listaOpciones);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados Posibles de la Ruleta"));
        add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Detalle y Botón) ---
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        lblDescripcion = new JLabel("Girando la ruleta...", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(70, 80, 95));

        btnAplicar = new JButton("Aplicar / Listo");
        btnAplicar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAplicar.setBackground(new Color(15, 23, 42));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setFocusPainted(false);
        btnAplicar.setEnabled(false);
        btnAplicar.addActionListener(e -> dispose());

        panelInferior.add(lblDescripcion, BorderLayout.CENTER);
        panelInferior.add(btnAplicar, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Método que inicia la animación del giro y devuelve el índice de la opción ganadora (0 a 7)
    public int ejecutarGiro() {
        Random random = new Random();
        opcionFinal = random.nextInt(8);

        // Timer para simular la animación de giro bajando la velocidad paulatinamente
        final int[] ticks = {0};
        final int limiteTicks = 20 + random.nextInt(10);

        Timer timer = new Timer(70, null);
        timer.addActionListener(e -> {
            int indiceActual = ticks[0] % 8;
            listaOpciones.setSelectedIndex(indiceActual);
            listaOpciones.ensureIndexIsVisible(indiceActual);
            ticks[0]++;

            if (ticks[0] >= limiteTicks) {
                timer.stop();
                listaOpciones.setSelectedIndex(opcionFinal);
                lblDescripcion.setText("<html><center><b>Efecto Ganador:</b><br>" + DESCRIPCIONES[opcionFinal] + "</center></html>");
                btnAplicar.setEnabled(true);
            }
        });

        timer.start();
        setVisible(true); // Se bloquea hasta presionar "Aplicar / Listo"

        return opcionFinal;
    }
}