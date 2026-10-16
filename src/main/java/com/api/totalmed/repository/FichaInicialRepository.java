package com.api.totalmed.repository;

import com.api.totalmed.Enum.Status;
import com.api.totalmed.model.FichaInicial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FichaInicialRepository extends JpaRepository<FichaInicial, Long> {
    List<FichaInicial> findByPacienteId(Long pacienteId);
    List<FichaInicial> findByStatus(Status status);
    Optional<FichaInicial> findByCodigo(String codigo);
    long countByStatus(Status status);
    boolean existsByPacienteIdAndClinicaIdAndStatusIn(Long pacienteId, Long clinicaId, List<Status> status);
    List<FichaInicial> findByStatusInAndDataPreferencialBefore(List<Status> status, LocalDateTime data);
    List<FichaInicial> findByPacienteIdAndStatusIn(Long pacienteId, List<Status> status);

    List<FichaInicial> findByClinicaId(Long id);
}
