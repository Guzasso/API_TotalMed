package com.api.totalmed.repository;

import com.api.totalmed.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByRg(String rg);
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
    List<Paciente> findByAtivoTrue();
    List<Paciente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    long countByAtivoTrue();
}
