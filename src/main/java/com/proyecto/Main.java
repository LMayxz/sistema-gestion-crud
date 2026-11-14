package com.proyecto;

import com.proyecto.controller.ProductoController;
import com.proyecto.controller.UsuarioController;
import com.proyecto.repository.json.ProductoJsonRepository;
import com.proyecto.repository.json.UsuarioJsonRepository;
import com.proyecto.repository.mysql.ProductoMySQLRepository;
import com.proyecto.repository.mysql.UsuarioMySQLRepository;
import com.proyecto.service.ProductoService;
import com.proyecto.service.UsuarioService;
import com.proyecto.view.ProductoView;
import com.proyecto.view.UsuarioView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] opciones = {"Gestión de Usuarios", "Gestión de Productos", "Abrir Ambos"};
            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione el módulo CRUD a ejecutar:",
                    "Sistema de Gestión CRUD - Unidad 3",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[1]
            );

            if (seleccion == 0 || seleccion == 2) {
                UsuarioMySQLRepository uMysql = new UsuarioMySQLRepository();
                UsuarioJsonRepository uJson = new UsuarioJsonRepository();
                UsuarioService uService = new UsuarioService(uMysql);
                UsuarioView uView = new UsuarioView();
                new UsuarioController(uView, uService, uMysql, uJson);
                uView.setVisible(true);
            }

            if (seleccion == 1 || seleccion == 2) {
                ProductoMySQLRepository pMysql = new ProductoMySQLRepository();
                ProductoJsonRepository pJson = new ProductoJsonRepository();
                ProductoService pService = new ProductoService(pMysql);
                ProductoView pView = new ProductoView();
                new ProductoController(pView, pService, pMysql, pJson);
                pView.setVisible(true);
            }
        });
    }
}