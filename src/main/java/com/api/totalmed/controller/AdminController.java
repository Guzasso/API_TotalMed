package com.api.totalmed.controller;

import com.api.totalmed.dto.*;
import com.api.totalmed.model.Admin;
import com.api.totalmed.model.Paciente;
import com.api.totalmed.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

//region CRUD
    //region Post
    @PostMapping
    public ResponseEntity<Admin> cadastrar(@RequestBody @Valid PostAdminDTO dto) {
        return ResponseEntity.ok(adminService.cadastrar(dto));
    }
    //endregion

    //region Put
    @PutMapping("/{id}")
    public ResponseEntity<Admin> redefinir(@PathVariable Long id, @RequestBody @Valid PutAdminDTO dto) {
        Admin admin = adminService.redefinir(id, dto);
        return ResponseEntity.ok(admin);
    }
    //endregion

    //region Patch
    @PatchMapping("/{id}")
    public ResponseEntity<Admin> atualizar(@PathVariable Long id, @RequestBody @Valid PatchAdminDTO dto) {
        Admin admin = adminService.atualizar(id, dto);
        return ResponseEntity.ok(admin);
    }
    //endregion

    //region Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        adminService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    //endregion
//endregion

//region Consulta

    @GetMapping
    public ResponseEntity<List<Admin>> listarAdmin() { return ResponseEntity.ok(adminService.listarAdmin()); }
    @GetMapping("/{id}")
    public ResponseEntity<Admin> buscarAdmin(@PathVariable Long id) { return ResponseEntity.ok(adminService.buscarPorId(id)); }
    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> listarPaciente() { return ResponseEntity.ok(adminService.listarPaciente()); }
    @GetMapping("/relatorio")
    public ResponseEntity<RelatorioAdminDTO> relatorio() { return ResponseEntity.ok(adminService.gerarRelatorio()); }
//endregion
}
