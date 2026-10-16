package com.api.totalmed.controller;

import com.api.totalmed.dto.PatchPacienteDTO;
import com.api.totalmed.dto.PostPacienteDTO;
import com.api.totalmed.dto.PutPacienteDTO;
import com.api.totalmed.model.Paciente;
import com.api.totalmed.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

//region CRUD
    @PostMapping
    public ResponseEntity<Paciente> cadastrar(@RequestBody @Valid PostPacienteDTO dto) {
        Paciente paciente = pacienteService.cadastrar(dto);
        return ResponseEntity.created(URI.create("/pacientes/" + paciente.getId())).body(paciente);
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPaciente() {
        return ResponseEntity.ok(pacienteService.listarPaciente());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> redefinir(@PathVariable Long id, @RequestBody @Valid PutPacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.redefinir(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id, @RequestBody @Valid PatchPacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
//endregion

//region Consultas
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.buscarPorCpf(cpf));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Paciente>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(pacienteService.buscarPorNome(nome));
    }
//endregion
}
