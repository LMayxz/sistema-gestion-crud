package com.proyecto.repository.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.proyecto.model.Producto;
import com.proyecto.repository.IRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoJsonRepository implements IRepository<Producto, Integer> {

    private final String archivoPath = "productos.json";
    private final Gson gson;

    public ProductoJsonRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private void guardarArchivo(List<Producto> lista) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(archivoPath), StandardCharsets.UTF_8)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir archivo JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardar(Producto producto) {
        List<Producto> lista = listarTodos();
        int maxId = lista.stream().mapToInt(Producto::getId).max().orElse(0);
        producto.setId(maxId + 1);
        lista.add(producto);
        guardarArchivo(lista);
    }

    @Override
    public List<Producto> listarTodos() {
        File file = new File(archivoPath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Producto>>() {}.getType();
            List<Producto> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer archivo JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return listarTodos().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public void actualizar(Producto producto) {
        List<Producto> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == producto.getId()) {
                lista.set(i, producto);
                guardarArchivo(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        List<Producto> lista = listarTodos();
        if (lista.removeIf(p -> p.getId() == id)) {
            guardarArchivo(lista);
        }
    }
}