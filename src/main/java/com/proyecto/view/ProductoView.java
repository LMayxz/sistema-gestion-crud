package com.proyecto.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductoView extends JFrame {

    public JComboBox<String> cbPersistencia;
    public JTextField txtId;
    public JTextField txtNombre;
    public JTextField txtPrecio;
    public JTextField txtStock;

    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnLimpiar;

    public JTable tablaProductos;
    public DefaultTableModel modeloTabla;

    public ProductoView() {
        setTitle("Gestión de Productos - Unidad 3");
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
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        panelForm.add(new JLabel("ID (Automático):"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nombre del Producto:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Precio ($):"));
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Stock (Unidades):"));
        panelForm.add(txtStock);

        panelNorte.add(panelForm, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // Panel Central: Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }
}