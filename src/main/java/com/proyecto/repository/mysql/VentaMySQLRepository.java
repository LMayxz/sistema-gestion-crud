package com.proyecto.repository.mysql;

import com.proyecto.config.ConexionMySQL;
import com.proyecto.model.Venta;
import com.proyecto.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaMySQLRepository implements IRepository<Venta, Integer> {

    private static final String COLUMNAS = "id, usuario_id, producto_id, cantidad, total, fecha";

    @Override
    public void guardar(Venta venta) {
        String sql = "INSERT INTO ventas (usuario_id, producto_id, cantidad, total, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, venta.getUsuarioId());
            stmt.setInt(2, venta.getProductoId());
            stmt.setInt(3, venta.getCantidad());
            stmt.setDouble(4, venta.getTotal());
            stmt.setTimestamp(5, Timestamp.valueOf(venta.getFecha()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venta.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar venta en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM ventas ORDER BY id";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ventas en MySQL: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Venta> buscarPorId(Integer id) {
        String sql = "SELECT " + COLUMNAS + " FROM ventas WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar venta en MySQL: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Venta venta) {
        String sql = "UPDATE ventas SET usuario_id = ?, producto_id = ?, cantidad = ?, total = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venta.getUsuarioId());
            stmt.setInt(2, venta.getProductoId());
            stmt.setInt(3, venta.getCantidad());
            stmt.setDouble(4, venta.getTotal());
            stmt.setInt(5, venta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar venta en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM ventas WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar venta en MySQL: " + e.getMessage(), e);
        }
    }

    private Venta mapear(ResultSet rs) throws SQLException {
        return new Venta(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getInt("producto_id"),
                rs.getInt("cantidad"),
                rs.getDouble("total"),
                rs.getTimestamp("fecha").toLocalDateTime());
    }
}
