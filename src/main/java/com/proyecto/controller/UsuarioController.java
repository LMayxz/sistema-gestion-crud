package com.proyecto.controller;

import com.proyecto.model.Usuario;
import com.proyecto.repository.json.UsuarioJsonRepository;
import com.proyecto.repository.mysql.UsuarioMySQLRepository;
import com.proyecto.service.UsuarioService;
import com.proyecto.view.UsuarioView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UsuarioController {

    private final UsuarioView view;
    private final UsuarioService service;
    private final UsuarioMySQLRepository mysqlRepo;
    private final UsuarioJsonRepository jsonRepo;

    public UsuarioController(UsuarioView view, UsuarioService service,
                             UsuarioMySQLRepository mysqlRepo, UsuarioJsonRepository jsonRepo) {
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

        view.tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = view.tablaUsuarios.getSelectedRow();
                if (fila >= 0) {
                    view.txtId.setText(view.modeloTabla.getValueAt(fila, 0).toString());
                    view.txtNombre.setText(view.modeloTabla.getValueAt(fila, 1).toString());
                    view.txtCorreo.setText(view.modeloTabla.getValueAt(fila, 2).toString());
                    view.cbRol.setSelectedItem(view.modeloTabla.getValueAt(fila, 3).toString());
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
            service.registrarUsuario(
                    view.txtNombre.getText(),
                    view.txtCorreo.getText(),
                    view.cbRol.getSelectedItem().toString()
            );
            JOptionPane.showMessageDialog(view, "Usuario registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            service.actualizarUsuario(
                    id,
                    view.txtNombre.getText(),
                    view.txtCorreo.getText(),
                    view.cbRol.getSelectedItem().toString()
            );
            JOptionPane.showMessageDialog(view, "Usuario actualizado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            int id = view.txtId.getText().isEmpty() ? 0 : Integer.parseInt(view.txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(view, "¿Eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.eliminarUsuario(id);
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
            List<Usuario> usuarios = service.obtenerTodos();
            for (Usuario u : usuarios) {
                view.modeloTabla.addRow(new Object[]{u.getId(), u.getNombre(), u.getCorreo(), u.getRol()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "No se pudieron cargar los datos: " + ex.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        view.txtId.setText("");
        view.txtNombre.setText("");
        view.txtCorreo.setText("");
        view.cbRol.setSelectedIndex(0);
        view.tablaUsuarios.clearSelection();
    }
}