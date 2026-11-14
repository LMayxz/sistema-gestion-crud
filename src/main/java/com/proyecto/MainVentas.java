package com.proyecto;

import com.proyecto.controller.VentaController;
import com.proyecto.repository.json.ProductoJsonRepository;
import com.proyecto.repository.json.UsuarioJsonRepository;
import com.proyecto.repository.json.VentaJsonRepository;
import com.proyecto.repository.mysql.ProductoMySQLRepository;
import com.proyecto.repository.mysql.UsuarioMySQLRepository;
import com.proyecto.repository.mysql.VentaMySQLRepository;
import com.proyecto.service.VentaService;
import com.proyecto.service.VentaService.Persistencia;
import com.proyecto.view.VentaView;

import javax.swing.SwingUtilities;

/** Punto de entrada solo para el módulo de Ventas (el Main del grupo puede abrir esta misma ventana). */
public class MainVentas {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Persistencia mysql = new Persistencia(
                    new VentaMySQLRepository(), new UsuarioMySQLRepository(), new ProductoMySQLRepository());
            Persistencia json = new Persistencia(
                    new VentaJsonRepository(), new UsuarioJsonRepository(), new ProductoJsonRepository());

            VentaService service = new VentaService(mysql);
            VentaView view = new VentaView();
            new VentaController(view, service, mysql, json);
            view.setVisible(true);
        });
    }
}
