package com.ambientiza.api.security;

import com.ambientiza.api.enums.Papel;

public record UsuarioAutenticado(Long idUsuario, String email, Papel papel) {
}
