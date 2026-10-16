package com.api.totalmed.service;

import com.api.totalmed.exception.ConflictException;
import com.api.totalmed.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Verifica e-mail, CPF e RG contra TODOS os usuarios (admin e paciente).
    // idAtual = null no cadastro; no PUT/PATCH e o id de quem esta sendo editado.
    public void validarUnicidade(String email, String cpf, String rg, Long idAtual) {
        if (email != null) {
            usuarioRepository.findByEmail(email).ifPresent(u -> {
                if (!u.getId().equals(idAtual)) throw new ConflictException("E-mail já cadastrado");
            });
        }
        if (cpf != null) {
            usuarioRepository.findByCpf(cpf).ifPresent(u -> {
                if (!u.getId().equals(idAtual)) throw new ConflictException("CPF já cadastrado");
            });
        }
        if (rg != null) {
            usuarioRepository.findByRg(rg).ifPresent(u -> {
                if (!u.getId().equals(idAtual)) throw new ConflictException("RG já cadastrado");
            });
        }
    }
}
