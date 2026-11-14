package com.proyecto.controller;

import com.proyecto.model.Producto;
import com.proyecto.model.Usuario;
import com.proyecto.model.Venta;
import com.proyecto.service.VentaService;
import com.proyecto.service.VentaService.Persistencia;
import com.proyecto.view.VentaView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentaController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final VentaView view;
    private final VentaService service;
    private final Persistencia mysql;
    private final Persistencia json;

    public VentaController(VentaView view, VentaService service, Persistencia mysql, Persistencia json) {
        this.view = view;
        this.service = service;
        this.mysql = mysql;
        this.json = json;

        inicializarEventos();
        recargarTodo();
    }

    private void inicializarEventos() {
        view.cbPersistencia.addActionListener(e -> cambiarPersistencia());
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnLimpiar.addActionListener(e -> limpiarFormulario());

        view.cbProducto.addActionListener(e -> actualizarTotalVista());
        view.txtCantidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTotalVista(); }
            public void removeUpdate(DocumentEvent e) { actualizarTotalVista(); }
            public void changedUpdate(DocumentEvent e) { actualizarTotalVista(); }
        });

        view.tablaVentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = view.tablaVentas.getSelectedRow();
                if (fila >= 0) {
                    int id = Integer.parseInt(view.modeloTabla.getValueAt(fila, 0).toString());
                    service.buscarPorId(id).ifPresent(v -> cargarFormulario(v));
                }
            }
        });
    }

    private void cambiarPersistencia() {
        service.setPersistencia(view.cbPersistencia.getSelectedIndex() == 0 ? mysql : json);
        recargarTodo();
    }

    private void guardar() {
        try {
            service.registrarVenta(usuarioSeleccionado(), productoSeleccionado(), leerCantidad());
            JOptionPane.showMessageDialog(view, "Venta registrada correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            service.actualizarVenta(id, usuarioSeleccionado(), productoSeleccionado(), leerCantidad());
            JOptionPane.showMessageDialog(view, "Venta actualizada correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            if (id <= 0) {
                throw new IllegalArgumentException("Selecciona una venta de la tabla para eliminar.");
            }
            int confirm = JOptionPane.showConfirmDialog(view, "¿Eliminar esta venta?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.eliminarVenta(id);
                JOptionPane.showMessageDialog(view, "Venta eliminada correctamente.");
                limpiarFormulario();
                cargarTabla();
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    // ---------- carga de datos en la vista ----------

    private void recargarTodo() {
        cargarCombos();
        limpiarFormulario();
        cargarTabla();
    }

    private void cargarCombos() {
        try {
            view.cbUsuario.removeAllItems();
            for (Usuario u : service.obtenerUsuarios()) {
                view.cbUsuario.addItem(u);
            }
            view.cbProducto.removeAllItems();
            for (Producto p : service.obtenerProductos()) {
                view.cbProducto.addItem(p);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "No se pudieron cargar clientes/productos: " + ex.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        try {
            view.modeloTabla.setRowCount(0);
            Map<Integer, String> clientes = new HashMap<>();
            for (Usuario u : service.obtenerUsuarios()) {
                clientes.put(u.getId(), u.getNombre());
            }
            Map<Integer, String> productos = new HashMap<>();
            for (Producto p : service.obtenerProductos()) {
                productos.put(p.getId(), p.getNombre());
            }
            List<Venta> ventas = service.obtenerTodas();
            for (Venta v : ventas) {
                view.modeloTabla.addRow(new Object[]{
                        v.getId(),
                        clientes.getOrDefault(v.getUsuarioId(), "ID " + v.getUsuarioId()),
                        productos.getOrDefault(v.getProductoId(), "ID " + v.getProductoId()),
                        v.getCantidad(),
                        String.format("%.2f", v.getTotal()),
                        v.getFecha().format(FORMATO_FECHA)});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "No se pudieron cargar los datos: " + ex.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFormulario(Venta v) {
        view.txtId.setText(String.valueOf(v.getId()));
        seleccionarUsuario(v.getUsuarioId());
        seleccionarProducto(v.getProductoId());
        view.txtCantidad.setText(String.valueOf(v.getCantidad()));
        view.txtTotal.setText(String.format("%.2f", v.getTotal()));
    }

    private void limpiarFormulario() {
        view.txtId.setText("");
        view.txtCantidad.setText("");
        view.txtTotal.setText("");
        if (view.cbUsuario.getItemCount() > 0) {
            view.cbUsuario.setSelectedIndex(0);
        }
        if (view.cbProducto.getItemCount() > 0) {
            view.cbProducto.setSelectedIndex(0);
        }
        view.tablaVentas.clearSelection();
    }

    private void actualizarTotalVista() {
        Producto p = (Producto) view.cbProducto.getSelectedItem();
        try {
            int cantidad = Integer.parseInt(view.txtCantidad.getText().trim());
            if (p != null && cantidad > 0) {
                view.txtTotal.setText(String.format("%.2f", service.calcularTotal(p, cantidad)));
                return;
            }
        } catch (NumberFormatException ignorada) {
            // mientras el usuario escribe, un número incompleto simplemente deja el total vacío
        }
        view.txtTotal.setText("");
    }

    // ---------- utilidades ----------

    private int usuarioSeleccionado() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        return u == null ? 0 : u.getId();
    }

    private int productoSeleccionado() {
        Producto p = (Producto) view.cbProducto.getSelectedItem();
        return p == null ? 0 : p.getId();
    }

    private int leerCantidad() {
        try {
            return Integer.parseInt(view.txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad debe ser un número entero.");
        }
    }

    private void seleccionarUsuario(int id) {
        for (int i = 0; i < view.cbUsuario.getItemCount(); i++) {
            if (view.cbUsuario.getItemAt(i).getId() == id) {
                view.cbUsuario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarProducto(int id) {
        for (int i = 0; i < view.cbProducto.getItemCount(); i++) {
            if (view.cbProducto.getItemAt(i).getId() == id) {
                view.cbProducto.setSelectedIndex(i);
                return;
            }
        }
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
