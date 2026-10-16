package com.api.totalmed.controller;

import com.api.totalmed.dto.LoginDTO;
import com.api.totalmed.dto.LoginRespostaDTO;
import com.api.totalmed.exception.UnauthorizedException;
import com.api.totalmed.model.Usuario;
import com.api.totalmed.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<LoginRespostaDTO> login(@RequestBody @Valid LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        if (!usuario.isAtivo()) {
            throw new UnauthorizedException("Conta desativada");
        }


        return ResponseEntity.ok(new LoginRespostaDTO(usuario.getId(), usuario.getNome(), usuario.getRole()));
    }

}
