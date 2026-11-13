package com.proyecto.repository.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.proyecto.model.Usuario;
import com.proyecto.repository.IRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioJsonRepository implements IRepository<Usuario, Integer> {

    private static final String FILE_PATH = "usuarios.json";
    private final Gson gson;

    public UsuarioJsonRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private void guardarLista(List<Usuario> lista) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en " + FILE_PATH, e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Usuario>>() {}.getType();
            List<Usuario> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + FILE_PATH, e);
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        List<Usuario> lista = listarTodos();
        int nuevoId = lista.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        usuario.setId(nuevoId);
        lista.add(usuario);
        guardarLista(lista);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return listarTodos().stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public void actualizar(Usuario usuarioActualizado) {
        List<Usuario> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == usuarioActualizado.getId()) {
                lista.set(i, usuarioActualizado);
                guardarLista(lista);
                return;
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        List<Usuario> lista = listarTodos();
        if (lista.removeIf(u -> u.getId() == id)) {
            guardarLista(lista);
        }
    }
}