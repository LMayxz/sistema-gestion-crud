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

/** PLACEHOLDER: la versión oficial es la de Kevin. */
public class ProductoJsonRepository implements IRepository<Producto, Integer> {

    private static final String FILE_PATH = "productos.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private void guardarLista(List<Producto> lista) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            gson.toJson(lista, w);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en " + FILE_PATH, e);
        }
    }

    @Override
    public List<Producto> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type t = new TypeToken<ArrayList<Producto>>() {}.getType();
            List<Producto> lista = gson.fromJson(r, t);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + FILE_PATH, e);
        }
    }

    @Override
    public void guardar(Producto p) {
        List<Producto> lista = listarTodos();
        p.setId(lista.stream().mapToInt(Producto::getId).max().orElse(0) + 1);
        lista.add(p);
        guardarLista(lista);
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return listarTodos().stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void actualizar(Producto nuevo) {
        List<Producto> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == nuevo.getId()) {
                lista.set(i, nuevo);
                guardarLista(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        List<Producto> lista = listarTodos();
        if (lista.removeIf(p -> p.getId() == id)) {
            guardarLista(lista);
        }
    }
}
