package Hospital.view.forms;

import Hospital.estructural.Sesion;
import Hospital.model.Paciente;
import Hospital.view.VMain;
import Hospital.view.forms.components.SearchBar;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class VPacientes extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;
    private JTextField searchField;
    private JPopupMenu popupSugerencias;

    public VPacientes(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Gestión de Pacientes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "DNI", "Teléfono"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setBackground(new Color(30, 30, 45));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(50, 50, 70));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(new Color(45, 45, 60));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(30, 30, 45));
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        sur.setOpaque(false);

        // Campo de búsqueda mejorado
        searchField = new JTextField(20);
        popupSugerencias = new JPopupMenu();

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                mostrarSugerencias(query);
                buscar(query);
            }
        });

        TextButton btnAgregar = new TextButton("+ Agregar", new Color(59, 130, 246));
        TextButton btnClonar  = new TextButton("Clonar",    new Color(139, 92, 246));
        TextButton btnEliminar = new TextButton("Eliminar", new Color(220, 53, 69));

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnClonar.addActionListener(e -> clonarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        sur.add(new JLabel("Buscar:"));
        sur.add(searchField);
        sur.add(btnAgregar);
        sur.add(btnClonar);
        sur.add(btnEliminar);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();
        for (Paciente p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getApellido(), p.getDni(), p.getTelefono()
            });
        }
    }

    private void buscar(String query) {
        modelo.setRowCount(0);
        List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();

        if (query.isEmpty()) {
            refrescar();
            return;
        }

        if (query.matches("\\d+")) {
            // Buscar por ID si es número
            for (Paciente p : lista) {
                if (String.valueOf(p.getId()).contains(query)) {
                    modelo.addRow(new Object[]{
                            p.getId(), p.getNombre(), p.getApellido(), p.getDni(), p.getTelefono()
                    });
                }
            }
        } else {
            // Buscar por nombre/apellido si es texto
            for (Paciente p : lista) {
                if (p.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                        p.getApellido().toLowerCase().contains(query.toLowerCase())) {
                    modelo.addRow(new Object[]{
                            p.getId(), p.getNombre(), p.getApellido(), p.getDni(), p.getTelefono()
                    });
                }
            }
        }
    }

    private void mostrarSugerencias(String query) {
        popupSugerencias.removeAll();
        if (query.isEmpty()) return;

        List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();
        for (Paciente p : lista) {
            if (query.matches("\\d+")) {
                if (String.valueOf(p.getId()).contains(query)) {
                    JMenuItem item = new JMenuItem("ID: " + p.getId() + " - " + p.getNombre());
                    item.addActionListener(e -> searchField.setText(String.valueOf(p.getId())));
                    popupSugerencias.add(item);
                }
            } else {
                if (p.getNombre().toLowerCase().startsWith(query.toLowerCase()) ||
                        p.getApellido().toLowerCase().startsWith(query.toLowerCase())) {
                    JMenuItem item = new JMenuItem(p.getNombre() + " " + p.getApellido());
                    item.addActionListener(e -> searchField.setText(p.getNombre()));
                    popupSugerencias.add(item);
                }
            }
        }

        if (popupSugerencias.getComponentCount() > 0) {
            popupSugerencias.show(searchField, 0, searchField.getHeight());
        }
    }

    private void dialogoAgregar() {
        JTextField fId       = new JTextField();
        JTextField fNombre   = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fDni      = new JTextField();
        JTextField fTelefono = new JTextField();

        Object[] campos = {
                "ID:",       fId,
                "Nombre:",   fNombre,
                "Apellido:", fApellido,
                "DNI:",      fDni,
                "Teléfono:", fTelefono
        };

        int result = JOptionPane.showConfirmDialog(
                this, campos, "Nuevo Paciente", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                Sesion.getInstancia().getProxy().registrarPaciente(
                        Integer.parseInt(fId.getText()),
                        fNombre.getText(), fApellido.getText(),
                        fDni.getText(), fTelefono.getText(), ""
                );
                refrescar();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
            }
        }
    }

    private void clonarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un paciente.");
            return;
        }
        List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();
        Paciente original = lista.get(fila);
        Sesion.getInstancia().getProxy().clonarPaciente(original);
        refrescar();
        JOptionPane.showMessageDialog(this, "Paciente clonado correctamente.");
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un paciente.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this, "¿Eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();
            Paciente p = lista.get(fila);
            Sesion.getInstancia().getProxy().eliminarPaciente(p.getId());
            refrescar();
        }
    }
}
