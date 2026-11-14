package com.proyecto.repository.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.proyecto.model.Venta;
import com.proyecto.repository.IRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaJsonRepository implements IRepository<Venta, Integer> {

    private static final String FILE_PATH = "ventas.json";
    private final Gson gson;

    public VentaJsonRepository() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (fecha, tipo, ctx) -> new JsonPrimitive(fecha.toString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, tipo, ctx) -> LocalDateTime.parse(json.getAsString()))
                .create();
    }

    private void guardarLista(List<Venta> lista) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en " + FILE_PATH, e);
        }
    }

    @Override
    public List<Venta> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Venta>>() {}.getType();
            List<Venta> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + FILE_PATH, e);
        }
    }

    @Override
    public void guardar(Venta venta) {
        List<Venta> lista = listarTodos();
        int nuevoId = lista.stream().mapToInt(Venta::getId).max().orElse(0) + 1;
        venta.setId(nuevoId);
        lista.add(venta);
        guardarLista(lista);
    }

    @Override
    public Optional<Venta> buscarPorId(Integer id) {
        return listarTodos().stream().filter(v -> v.getId() == id).findFirst();
    }

    @Override
    public void actualizar(Venta ventaActualizada) {
        List<Venta> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == ventaActualizada.getId()) {
                lista.set(i, ventaActualizada);
                guardarLista(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        List<Venta> lista = listarTodos();
        if (lista.removeIf(v -> v.getId() == id)) {
            guardarLista(lista);
        }
    }
}
