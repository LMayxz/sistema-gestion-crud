package com.proyecto.view;

import com.proyecto.model.Producto;
import com.proyecto.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentaView extends JFrame {

    public JComboBox<String> cbPersistencia;
    public JTextField txtId;
    public JComboBox<Usuario> cbUsuario;
    public JComboBox<Producto> cbProducto;
    public JTextField txtCantidad;
    public JTextField txtTotal;

    public JButton btnGuardar;
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnLimpiar;

    public JTable tablaVentas;
    public DefaultTableModel modeloTabla;

    public VentaView() {
        setTitle("Gestión de Ventas - Unidad 3");
        setSize(850, 540);
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

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Venta"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));

        cbUsuario = new JComboBox<>();
        cbProducto = new JComboBox<>();
        txtCantidad = new JTextField();

        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(235, 235, 235));

        panelForm.add(new JLabel("ID (Automático):"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Cliente:"));
        panelForm.add(cbUsuario);
        panelForm.add(new JLabel("Producto:"));
        panelForm.add(cbProducto);
        panelForm.add(new JLabel("Cantidad:"));
        panelForm.add(txtCantidad);
        panelForm.add(new JLabel("Total (calculado):"));
        panelForm.add(txtTotal);

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
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Cliente", "Producto", "Cantidad", "Total", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVentas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }
}
