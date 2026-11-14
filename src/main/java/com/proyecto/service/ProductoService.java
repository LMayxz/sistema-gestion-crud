package com.proyecto.service;

import com.proyecto.model.Producto;
import com.proyecto.repository.IRepository;

import java.util.List;

public class ProductoService {

    private IRepository<Producto, Integer> repository;

    public ProductoService(IRepository<Producto, Integer> repository) {
        this.repository = repository;
    }

    public void setRepository(IRepository<Producto, Integer> repository) {
        this.repository = repository;
    }

    public void registrarProducto(String nombre, String precioStr, String stockStr) throws Exception {
        Producto p = validarYConstruir(0, nombre, precioStr, stockStr);
        repository.guardar(p);
    }

    public void actualizarProducto(int id, String nombre, String precioStr, String stockStr) throws Exception {
        if (id <= 0) {
            throw new Exception("Seleccione un producto de la tabla para actualizar.");
        }
        Producto p = validarYConstruir(id, nombre, precioStr, stockStr);
        repository.actualizar(p);
    }

    public void eliminarProducto(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("Seleccione un producto de la tabla para eliminar.");
        }
        repository.eliminar(id);
    }

    public List<Producto> obtenerTodos() {
        return repository.listarTodos();
    }

    private Producto validarYConstruir(int id, String nombre, String precioStr, String stockStr) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }
        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("El precio debe ser un valor numérico válido.");
        }
        try {
            stock = Integer.parseInt(stockStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("El stock debe ser un número entero válido.");
        }
        if (precio <= 0) {
            throw new Exception("El precio debe ser mayor a 0.");
        }
        if (stock < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }
        return new Producto(id, nombre.trim(), precio, stock);
    }
}