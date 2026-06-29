package Hospital.view;

import Hospital.comportamiento.CitaObserver;
import Hospital.estructural.Sesion;
import Hospital.model.Cita;
import Hospital.view.forms.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VMain extends JFrame implements CitaObserver {

    private JPanel contenedor;
    private CardLayout cardLayout;

    private VHome      vHome;
    private VPacientes vPacientes;
    private VMedicos   vMedicos;
    private VCitas     vCitas;
    private VReporte   vReporte;
    private VConfi     vConfi;

    private JLabel lblUserName;
    private JLabel lblUserRole;
    private JButton btnActivo;

    public static final Color AZUL_OSCURO = new Color(10, 34, 64);
    public static final Color AZUL_MEDIO  = new Color(29, 111, 164);
    public static final Color AZUL_CLARO  = new Color(232, 244, 253);
    public static final Color FONDO       = new Color(245, 247, 250);
    public static final Color BLANCO      = Color.WHITE;
    public static final Color TEXTO_DARK  = new Color(15, 30, 50);
    public static final Color TEXTO_GRAY  = new Color(100, 116, 139);
    public static final Color BORDE       = new Color(220, 230, 240);
    public static final Color VERDE       = new Color(16, 185, 129);
    public static final Color ROJO        = new Color(220, 53, 69);
    public static final Color NARANJA     = new Color(245, 158, 11);
    public static final Color MORADO      = new Color(124, 58, 237);

    public VMain() {
        setTitle("Hospital — Sistema de Gestión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        contenedor.setBackground(FONDO);

        vHome      = new VHome(this);
        vPacientes = new VPacientes(this);
        vMedicos   = new VMedicos(this);
        vCitas     = new VCitas(this);
        vReporte   = new VReporte(this);
        vConfi     = new VConfi(this);

        contenedor.add(vHome,      "HOME");
        contenedor.add(vPacientes, "PACIENTES");
        contenedor.add(vMedicos,   "MEDICOS");
        contenedor.add(vCitas,     "CITAS");
        contenedor.add(vReporte,   "REPORTE");
        contenedor.add(vConfi,     "CONFI");

        add(crearSidebar(), BorderLayout.WEST);
        add(crearHeader(),  BorderLayout.NORTH);
        add(contenedor,     BorderLayout.CENTER);

        mostrar("HOME");
        Sesion.getInstancia().registrarObserverUI(this);
        refrescarPermisos();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLANCO);
        header.setPreferredSize(new Dimension(0, 54));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDE));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        left.setOpaque(false);
        JLabel breadcrumb = new JLabel("Sistema de Gestión Hospitalaria");
        breadcrumb.setForeground(TEXTO_GRAY);
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        left.add(breadcrumb);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        right.setOpaque(false);

        JPanel userChip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        userChip.setBackground(AZUL_CLARO);
        userChip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 220, 245), 1),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));

        JLabel iconUser = new JLabel("\uD83D\uDC64");
        iconUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        lblUserName = new JLabel(Sesion.getInstancia().getUsuarioActivo().getNombre());
        lblUserName.setForeground(AZUL_OSCURO);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblUserRole = new JLabel("(" + Sesion.getInstancia().getUsuarioActivo().getRol() + ")");
        lblUserRole.setForeground(AZUL_MEDIO);
        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        userChip.add(iconUser);
        userChip.add(lblUserName);
        userChip.add(lblUserRole);
        right.add(userChip);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(AZUL_OSCURO);

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(210, 72));

        JPanel iconBox = new JPanel(new BorderLayout());
        iconBox.setPreferredSize(new Dimension(38, 38));
        iconBox.setBackground(AZUL_MEDIO);
        JLabel iconLbl = new JLabel("+", SwingConstants.CENTER);
        iconLbl.setForeground(Color.WHITE);
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconBox.add(iconLbl, BorderLayout.CENTER);

        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);
        JLabel t1 = new JLabel("Hospital");
        t1.setForeground(Color.WHITE);
        t1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel t2 = new JLabel("Sistema de Gestion");
        t2.setForeground(new Color(123, 163, 200));
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        textBox.add(t1);
        textBox.add(t2);

        logoPanel.add(iconBox);
        logoPanel.add(textBox);
        sidebar.add(logoPanel);

        addSeparador(sidebar);
        sidebar.add(Box.createVerticalStrut(6));

        JLabel secLabel = new JLabel("  MENU PRINCIPAL");
        secLabel.setForeground(new Color(123, 163, 200));
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        secLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        secLabel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 0));
        sidebar.add(secLabel);

        sidebar.add(botonNav("  Inicio",        "HOME"));
        sidebar.add(botonNav("  Pacientes",     "PACIENTES"));
        sidebar.add(botonNav("  Medicos",       "MEDICOS"));
        sidebar.add(botonNav("  Citas",         "CITAS"));
        sidebar.add(botonNav("  Reportes",      "REPORTE"));

        sidebar.add(Box.createVerticalGlue());
        addSeparador(sidebar);
        sidebar.add(botonNav("  Configuracion", "CONFI"));

        // Card usuario al fondo
        JPanel userCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        userCard.setMaximumSize(new Dimension(210, 60));
        userCard.setBackground(new Color(255, 255, 255, 20));

        JLabel avatarCircle = new JLabel("A");
        avatarCircle.setPreferredSize(new Dimension(32, 32));
        avatarCircle.setHorizontalAlignment(SwingConstants.CENTER);
        avatarCircle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatarCircle.setForeground(Color.WHITE);
        avatarCircle.setOpaque(true);
        avatarCircle.setBackground(AZUL_MEDIO);

        JPanel uInfo = new JPanel();
        uInfo.setLayout(new BoxLayout(uInfo, BoxLayout.Y_AXIS));
        uInfo.setOpaque(false);
        JLabel uName = new JLabel(Sesion.getInstancia().getUsuarioActivo().getNombre());
        uName.setForeground(Color.WHITE);
        uName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel uRole = new JLabel(Sesion.getInstancia().getUsuarioActivo().getRol().name());
        uRole.setForeground(new Color(123, 163, 200));
        uRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        uInfo.add(uName);
        uInfo.add(uRole);

        userCard.add(avatarCircle);
        userCard.add(uInfo);
        sidebar.add(userCard);

        return sidebar;
    }

    private void addSeparador(JPanel panel) {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 30));
        sep.setMaximumSize(new Dimension(210, 1));
        panel.add(sep);
    }

    private JButton botonNav(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setPreferredSize(new Dimension(210, 42));
        btn.setMinimumSize(new Dimension(210, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(AZUL_OSCURO);
        btn.setForeground(new Color(168, 196, 220));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != btnActivo) {
                    btn.setBackground(new Color(29, 111, 164, 50));
                    btn.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnActivo) {
                    btn.setBackground(AZUL_OSCURO);
                    btn.setForeground(new Color(168, 196, 220));
                }
            }
        });

        btn.addActionListener(e -> {
            if (btnActivo != null) {
                btnActivo.setBackground(AZUL_OSCURO);
                btnActivo.setForeground(new Color(168, 196, 220));
                btnActivo.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
            }
            btn.setBackground(new Color(29, 111, 164, 80));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 3, 0, 0, new Color(59, 157, 224)),
                    BorderFactory.createEmptyBorder(0, 15, 0, 0)
            ));
            btnActivo = btn;
            mostrar(panel);
        });

        return btn;
    }

    public void mostrar(String panel) {
        cardLayout.show(contenedor, panel);
    }

    public void actualizarHeaderUsuario() {
        lblUserName.setText(Sesion.getInstancia().getUsuarioActivo().getNombre());
        lblUserRole.setText("(" + Sesion.getInstancia().getUsuarioActivo().getRol() + ")");
    }

    @Override
    public void actualizar(Cita cita) {
        vHome.refrescar();
        vCitas.refrescar();
        vPacientes.refrescar();
        vMedicos.refrescar();
        vReporte.refrescar();
        refrescarPermisos();
    }

    public void refrescarPermisos() {
        vPacientes.aplicarPermisos();
        vMedicos.aplicarPermisos();
        vCitas.aplicarPermisos();
    }

    public Sesion getSesion() {
        return Sesion.getInstancia();
    }
}