package com.ambientiza.api.services;

import com.ambientiza.api.dtos.usuarios.Cadastro;
import com.ambientiza.api.dtos.usuarios.ListarNomes;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void createUser(Cadastro cadastro) {

    }

    public List<ListarNomes> findAll() {
        return usuarioRepository.findAll().stream()
                .map(f -> new ListarNomes(f.getNome()))
                .toList();
    }
}
