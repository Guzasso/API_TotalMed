package com.api.totalmed.controller;

import com.api.totalmed.model.Clinica;
import com.api.totalmed.service.ClinicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clinicas")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

    @PostMapping
    public ResponseEntity<Clinica> cadastrar(@RequestBody @Valid Clinica clinica) {
        Clinica salva = clinicaService.cadastrar(clinica);
        return ResponseEntity.created(URI.create("/clinicas/" + salva.getId())).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<Clinica>> listar() {
        return ResponseEntity.ok(clinicaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinica> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clinicaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clinicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
