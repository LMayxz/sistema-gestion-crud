package com.proyecto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    // Ajusta el nombre de la BD, usuario y contraseña según tu servidor local (XAMPP/Workbench)
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_gestion_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // En XAMPP suele ser vacío ""; en Workbench coloca tu clave

    private ConexionMySQL() {
        // Constructor privado para evitar instanciación innecesaria
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}