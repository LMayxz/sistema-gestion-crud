package com.proyecto.service;

import com.proyecto.model.Producto;
import com.proyecto.model.Usuario;
import com.proyecto.model.Venta;
import com.proyecto.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio de ventas. Valida que el cliente y el producto existan, que haya stock
 * y calcula el total (precio del producto x cantidad). No sabe si los datos están en MySQL o JSON.
 */
public class VentaService {

    /** Grupo de repositorios que deben cambiar juntos (todos en MySQL o todos en JSON). */
    public record Persistencia(IRepository<Venta, Integer> ventas,
                               IRepository<Usuario, Integer> usuarios,
                               IRepository<Producto, Integer> productos) {
    }

    private Persistencia persistencia;

    public VentaService(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    public void setPersistencia(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    public void registrarVenta(int usuarioId, int productoId, int cantidad) {
        Producto producto = validar(usuarioId, productoId, cantidad);
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock() + ".");
        }
        Venta nueva = new Venta(usuarioId, productoId, cantidad,
                calcularTotal(producto, cantidad), LocalDateTime.now());
        persistencia.ventas().guardar(nueva);
    }

    public void actualizarVenta(int id, int usuarioId, int productoId, int cantidad) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecciona una venta válida de la tabla para actualizar.");
        }
        Venta existente = persistencia.ventas().buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("La venta seleccionada ya no existe."));
        Producto producto = validar(usuarioId, productoId, cantidad);
        Venta actualizada = new Venta(id, usuarioId, productoId, cantidad,
                calcularTotal(producto, cantidad), existente.getFecha());
        persistencia.ventas().actualizar(actualizada);
    }

    public void eliminarVenta(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecciona una venta válida de la tabla para eliminar.");
        }
        persistencia.ventas().eliminar(id);
    }

    public List<Venta> obtenerTodas() {
        return persistencia.ventas().listarTodos();
    }

    public Optional<Venta> buscarPorId(int id) {
        return persistencia.ventas().buscarPorId(id);
    }

    public List<Usuario> obtenerUsuarios() {
        return persistencia.usuarios().listarTodos();
    }

    public List<Producto> obtenerProductos() {
        return persistencia.productos().listarTodos();
    }

    public double calcularTotal(Producto producto, int cantidad) {
        return Math.round(producto.getPrecio() * cantidad * 100.0) / 100.0;
    }

    private Producto validar(int usuarioId, int productoId, int cantidad) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("Selecciona un cliente.");
        }
        if (productoId <= 0) {
            throw new IllegalArgumentException("Selecciona un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        persistencia.usuarios().buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente seleccionado no existe."));
        return persistencia.productos().buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto seleccionado no existe."));
    }
}
