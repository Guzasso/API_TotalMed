package com.api.totalmed.repository;

import com.api.totalmed.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository  extends JpaRepository<Admin, Long> {

    Optional<Admin> findByCpf(String cpf);
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByNome(String nome);
    Optional<Admin> findByRg(String rg);
}

