package com.proyecto.controller;

import com.proyecto.model.Producto;
import com.proyecto.repository.json.ProductoJsonRepository;
import com.proyecto.repository.mysql.ProductoMySQLRepository;
import com.proyecto.service.ProductoService;
import com.proyecto.view.ProductoView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductoController {

    private final ProductoView view;
    private final ProductoService service;
    private final ProductoMySQLRepository mysqlRepo;
    private final ProductoJsonRepository jsonRepo;

    public ProductoController(ProductoView view, ProductoService service,
                              ProductoMySQLRepository mysqlRepo, ProductoJsonRepository jsonRepo) {
        this.view = view;
        this.service = service;
        this.mysqlRepo = mysqlRepo;
        this.jsonRepo = jsonRepo;

        inicializarEventos();
        cargarTabla();
    }

    private void inicializarEventos() {
        view.cbPersistencia.addActionListener(e -> cambiarPersistencia());
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnLimpiar.addActionListener(e -> limpiarFormulario());

        view.tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = view.tablaProductos.getSelectedRow();
                if (fila >= 0) {
                    view.txtId.setText(view.modeloTabla.getValueAt(fila, 0).toString());
                    view.txtNombre.setText(view.modeloTabla.getValueAt(fila, 1).toString());
                    view.txtPrecio.setText(view.modeloTabla.getValueAt(fila, 2).toString());
                    view.txtStock.setText(view.modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void cambiarPersistencia() {
        int seleccion = view.cbPersistencia.getSelectedIndex();
        if (seleccion == 0) {
            service.setRepository(mysqlRepo);
        } else {
            service.setRepository(jsonRepo);
        }
        limpiarFormulario();
        cargarTabla();
    }

    private void guardar() {
        try {
            service.registrarProducto(
                    view.txtNombre.getText(),
                    view.txtPrecio.getText(),
                    view.txtStock.getText()
            );
            JOptionPane.showMessageDialog(view, "Producto registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            service.actualizarProducto(
                    id,
                    view.txtNombre.getText(),
                    view.txtPrecio.getText(),
                    view.txtStock.getText()
            );
            JOptionPane.showMessageDialog(view, "Producto actualizado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(view, "¿Eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.eliminarProducto(id);
                limpiarFormulario();
                cargarTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        try {
            view.modeloTabla.setRowCount(0);
            List<Producto> productos = service.obtenerTodos();
            for (Producto p : productos) {
                view.modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getStock()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "No se pudieron cargar los datos: " + ex.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        view.txtId.setText("");
        view.txtNombre.setText("");
        view.txtPrecio.setText("");
        view.txtStock.setText("");
        view.tablaProductos.clearSelection();
    }
}