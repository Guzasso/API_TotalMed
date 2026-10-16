package com.api.totalmed.controller;

import com.api.totalmed.Enum.Status;
import com.api.totalmed.dto.FichaInicialRequestDTO;
import com.api.totalmed.dto.RecusarFichaDTO;
import com.api.totalmed.model.FichaInicial;
import com.api.totalmed.service.FichaInicialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fichas")
public class FichaInicialController {

    @Autowired
    private FichaInicialService fichaInicialService;

//region Paciente
    @PostMapping("/pacientes/{pacienteId}")
    public ResponseEntity<FichaInicial> criar(@PathVariable Long pacienteId,
                                              @RequestBody @Valid FichaInicialRequestDTO dto) {
        FichaInicial ficha = fichaInicialService.criar(pacienteId, dto);
        return ResponseEntity.created(URI.create("/fichas/" + ficha.getId())).body(ficha);
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<FichaInicial>> porPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(fichaInicialService.listarPorPaciente(pacienteId));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<FichaInicial> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(fichaInicialService.cancelar(id));
    }
//endregion

//region Recepcao (admin)
    // GET /fichas  ou  GET /fichas?status=PENDENTE
    @GetMapping
    public ResponseEntity<List<FichaInicial>> listar(@RequestParam(required = false) Status status) {
        return ResponseEntity.ok(fichaInicialService.listar(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaInicial> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(fichaInicialService.buscarPorId(id));
    }

    @PutMapping("/{id}/aceitar")
    public ResponseEntity<FichaInicial> aceitar(@PathVariable Long id) {
        return ResponseEntity.ok(fichaInicialService.aceitar(id));
    }

    @PutMapping("/{id}/recusar")
    public ResponseEntity<FichaInicial> recusar(@PathVariable Long id,
                                                @RequestBody @Valid RecusarFichaDTO dto) {
        return ResponseEntity.ok(fichaInicialService.recusar(id, dto.getJustificativa()));
    }

    // Na chegada do paciente: recepcao digita o codigo e ele entra na fila
    @PostMapping("/validar/{codigo}")
    public ResponseEntity<FichaInicial> validar(@PathVariable String codigo) {
        return ResponseEntity.ok(fichaInicialService.validarCodigo(codigo));
    }

    @PutMapping("/expirar")
    public ResponseEntity<String> expirar() {
        return ResponseEntity.ok(fichaInicialService.expirarVencidas() + " ficha(s) expirada(s)");
    }
//endregion
}
