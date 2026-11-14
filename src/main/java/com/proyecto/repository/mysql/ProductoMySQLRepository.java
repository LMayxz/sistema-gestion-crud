package com.proyecto.repository.mysql;

import com.proyecto.config.ConexionMySQL;
import com.proyecto.model.Producto;
import com.proyecto.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** PLACEHOLDER: la versión oficial es la de Kevin. */
public class ProductoMySQLRepository implements IRepository<Producto, Integer> {

    @Override
    public void guardar(Producto p) {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection c = ConexionMySQL.obtenerConexion();
             PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, p.getNombre());
            s.setDouble(2, p.getPrecio());
            s.setInt(3, p.getStock());
            s.executeUpdate();
            try (ResultSet rs = s.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        try (Connection c = ConexionMySQL.obtenerConexion();
             PreparedStatement s = c.prepareStatement("SELECT id, nombre, precio, stock FROM productos");
             ResultSet rs = s.executeQuery()) {
            while (rs.next()) {
                lista.add(new Producto(rs.getInt("id"), rs.getString("nombre"),
                        rs.getDouble("precio"), rs.getInt("stock")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos en MySQL: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        try (Connection c = ConexionMySQL.obtenerConexion();
             PreparedStatement s = c.prepareStatement("SELECT id, nombre, precio, stock FROM productos WHERE id = ?")) {
            s.setInt(1, id);
            try (ResultSet rs = s.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Producto(rs.getInt("id"), rs.getString("nombre"),
                            rs.getDouble("precio"), rs.getInt("stock")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto en MySQL: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id = ?";
        try (Connection c = ConexionMySQL.obtenerConexion(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, p.getNombre());
            s.setDouble(2, p.getPrecio());
            s.setInt(3, p.getStock());
            s.setInt(4, p.getId());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        try (Connection c = ConexionMySQL.obtenerConexion();
             PreparedStatement s = c.prepareStatement("DELETE FROM productos WHERE id = ?")) {
            s.setInt(1, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto en MySQL: " + e.getMessage(), e);
        }
    }
}
