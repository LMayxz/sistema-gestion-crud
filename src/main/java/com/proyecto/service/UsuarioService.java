package com.proyecto.service;

import com.proyecto.model.Usuario;
import com.proyecto.repository.IRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private IRepository<Usuario, Integer> repository;

    public UsuarioService(IRepository<Usuario, Integer> repository) {
        this.repository = repository;
    }

    public void setRepository(IRepository<Usuario, Integer> repository) {
        this.repository = repository;
    }

    public void registrarUsuario(String nombre, String correo, String rol) {
        validarDatos(nombre, correo, rol);
        Usuario nuevo = new Usuario(nombre.trim(), correo.trim(), rol.trim());
        repository.guardar(nuevo);
    }

    public List<Usuario> obtenerTodos() {
        return repository.listarTodos();
    }

    public Optional<Usuario> buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public void actualizarUsuario(int id, String nombre, String correo, String rol) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecciona un usuario válido de la tabla para actualizar.");
        }
        validarDatos(nombre, correo, rol);
        Usuario actualizado = new Usuario(id, nombre.trim(), correo.trim(), rol.trim());
        repository.actualizar(actualizado);
    }

    public void eliminarUsuario(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecciona un usuario válido de la tabla para eliminar.");
        }
        repository.eliminar(id);
    }

    private void validarDatos(String nombre, String correo, String rol) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (correo == null || !correo.trim().contains("@")) {
            throw new IllegalArgumentException("Ingresa un correo electrónico válido.");
        }
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }
    }
}