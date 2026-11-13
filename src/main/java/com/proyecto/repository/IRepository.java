package com.proyecto.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    void guardar(T entidad);
    List<T> listarTodos();
    Optional<T> buscarPorId(ID id);
    void actualizar(T entidad);
    void eliminar(ID id);
}