package com.proyecto.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioView extends JFrame {

    public JComboBox<String> cbPersistencia;
    public JTextField txtId;
    public JTextField txtNombre;
    public JTextField txtCorreo;
    public JComboBox<String> cbRol;

    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnLimpiar;

    public JTable tablaUsuarios;
    public DefaultTableModel modeloTabla;

    public UsuarioView() {
        setTitle("Gestión de Usuarios - Unidad 3");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior: Selector de Persistencia + Formulario
        JPanel panelNorte = new JPanel(new BorderLayout(5, 5));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel panelModo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelModo.add(new JLabel("Modo de Persistencia:"));
        cbPersistencia = new JComboBox<>(new String[]{"MySQL (Base de Datos)", "JSON (Archivo Local)"});
        panelModo.add(cbPersistencia);
        panelNorte.add(panelModo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        cbRol = new JComboBox<>(new String[]{"Administrador", "Vendedor", "Cliente"});

        panelForm.add(new JLabel("ID (Automático):"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nombre Completo:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Correo Electrónico:"));
        panelForm.add(txtCorreo);
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(cbRol);

        panelNorte.add(panelForm, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        // Evento con cuadro de advertencia antes de borrar los campos
        btnLimpiar.addActionListener(e -> confirmarYLimpiarCampos());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // Panel Central: Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Correo", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void confirmarYLimpiarCampos() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¡Cuidado! Se van a borrar todos los campos del formulario.\n¿Deseas continuar?",
                "Advertencia - Limpiar Campos",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            limpiarCampos();
        }
    }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        cbRol.setSelectedIndex(0);
        if (tablaUsuarios != null) {
            tablaUsuarios.clearSelection();
        }
        txtNombre.requestFocus();
    }
}