package com.api.totalmed.service;

import com.api.totalmed.Enum.Role;
import com.api.totalmed.Enum.Status;
import com.api.totalmed.dto.PatchAdminDTO;
import com.api.totalmed.dto.PostAdminDTO;
import com.api.totalmed.dto.PutAdminDTO;
import com.api.totalmed.dto.RelatorioAdminDTO;
import com.api.totalmed.exception.BusinessException;
import com.api.totalmed.exception.ResourceNotFoundException;
import com.api.totalmed.model.Admin;
import com.api.totalmed.model.Paciente;
import com.api.totalmed.repository.AdminRepository;
import com.api.totalmed.repository.FichaInicialRepository;
import com.api.totalmed.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired private AdminRepository adminRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private FichaInicialRepository fichaInicialRepository;
    @Autowired private UsuarioService usuarioService;
    @Autowired private PasswordEncoder passwordEncoder;

//region CRUD - ADMIN

    //region Post
    public Admin cadastrar(PostAdminDTO dto) {
        usuarioService.validarUnicidade(dto.getEmail(), dto.getCpf(), dto.getRg(), null);

        Admin admin = new Admin();
        admin.setNome(dto.getNome());
        admin.setEmail(dto.getEmail());
        admin.setCpf(dto.getCpf());
        admin.setRg(dto.getRg());
        admin.setDataNascimento(dto.getDataNascimento());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setTell(dto.getTell());
        admin.setRole(Role.ADMIN);
        return adminRepository.save(admin);
    }
    //endregion

    //region Get
    public Admin buscarPorId(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));
    }
    //endregion

    //region Put
    public Admin redefinir(Long id, PutAdminDTO dto) {
        Admin admin = buscarPorId(id);
        usuarioService.validarUnicidade(dto.getEmail(), dto.getCpf(), dto.getRg(), id);

        admin.setNome(dto.getNome());
        admin.setEmail(dto.getEmail());
        admin.setCpf(dto.getCpf());
        admin.setRg(dto.getRg());
        admin.setTell(dto.getTell());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setDataNascimento(dto.getDataNascimento());
        return adminRepository.save(admin);
    }
    //endregion

    //region Patch
    public Admin atualizar(Long id, PatchAdminDTO dto) {
        Admin admin = buscarPorId(id);
        usuarioService.validarUnicidade(dto.getEmail(), null, null, id);

        if (dto.getEmail() != null) admin.setEmail(dto.getEmail());
        if (dto.getNome() != null) admin.setNome(dto.getNome());
        if (dto.getPassword() != null) admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getTell() != null) admin.setTell(dto.getTell());

        return adminRepository.save(admin);
    }
    //endregion

    //region Delete
    public void deletar(Long id) {
        Admin admin = buscarPorId(id);
        if (adminRepository.count() <= 1) {
            throw new BusinessException("Não é possível remover o último administrador");
        }
        adminRepository.delete(admin);
    }
    //endregion
//endregion

//region Listar
    public List<Admin> listarAdmin() { return adminRepository.findAll(); }
    public List<Paciente> listarPaciente() { return pacienteRepository.findByAtivoTrue(); }
//endregion

//region Relatorio
    public RelatorioAdminDTO gerarRelatorio() {
        return new RelatorioAdminDTO(
                pacienteRepository.countByAtivoTrue(),
                fichaInicialRepository.countByStatus(Status.PENDENTE),
                fichaInicialRepository.countByStatus(Status.ACEITO),
                fichaInicialRepository.countByStatus(Status.RECUSADO));
    }
//endregion
}
