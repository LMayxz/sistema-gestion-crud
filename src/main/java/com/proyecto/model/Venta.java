package com.proyecto.model;

import java.time.LocalDateTime;

/** Entidad Venta. Equivale a la tabla ventas de MySQL (id, usuario_id, producto_id, cantidad, total, fecha). */
public class Venta {
    private int id;
    private int usuarioId;
    private int productoId;
    private int cantidad;
    private double total;
    private LocalDateTime fecha;

    public Venta() {
    }

    public Venta(int id, int usuarioId, int productoId, int cantidad, double total, LocalDateTime fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }

    public Venta(int usuarioId, int productoId, int cantidad, double total, LocalDateTime fecha) {
        this(0, usuarioId, productoId, cantidad, total, fecha);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "Venta #" + id + " - total " + total;
    }
}
