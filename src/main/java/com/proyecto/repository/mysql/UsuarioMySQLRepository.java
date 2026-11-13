package com.proyecto.repository.mysql;

import com.proyecto.config.ConexionMySQL;
import com.proyecto.model.Usuario;
import com.proyecto.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMySQLRepository implements IRepository<Usuario, Integer> {

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, rol) VALUES (?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getRol());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, rol FROM usuarios";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios en MySQL: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("rol")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario en MySQL: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getRol());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario en MySQL: " + e.getMessage(), e);
        }
    }
}