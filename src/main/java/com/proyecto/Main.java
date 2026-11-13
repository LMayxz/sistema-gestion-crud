package com.proyecto;

import com.proyecto.controller.UsuarioController;
import com.proyecto.repository.json.UsuarioJsonRepository;
import com.proyecto.repository.mysql.UsuarioMySQLRepository;
import com.proyecto.service.UsuarioService;
import com.proyecto.view.UsuarioView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioMySQLRepository mysqlRepo = new UsuarioMySQLRepository();
            UsuarioJsonRepository jsonRepo = new UsuarioJsonRepository();
            UsuarioService service = new UsuarioService(mysqlRepo);
            UsuarioView view = new UsuarioView();
            new UsuarioController(view, service, mysqlRepo, jsonRepo);
            view.setVisible(true);
        });
    }
}