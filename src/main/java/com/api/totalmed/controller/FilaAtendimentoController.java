package com.api.totalmed.controller;

import com.api.totalmed.model.FilaAtendimento;
import com.api.totalmed.service.FilaAtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fila")
public class FilaAtendimentoController {

    @Autowired
    private FilaAtendimentoService filaAtendimentoService;

    // Quem entra na fila e feito em POST /fichas/validar/{codigo}
    @GetMapping
    public ResponseEntity<List<FilaAtendimento>> listar() {
        return ResponseEntity.ok(filaAtendimentoService.listarFila());
    }

    @PutMapping("/{id}/atendido")
    public ResponseEntity<FilaAtendimento> marcarAtendido(@PathVariable Long id) {
        return ResponseEntity.ok(filaAtendimentoService.marcarAtendido(id));
    }
}
