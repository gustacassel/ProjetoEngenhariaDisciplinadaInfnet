package com.infnet.pb.repository;

import com.infnet.pb.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Necessário para verificar duplicidade antes de salvar (Fail Early)
    Optional<Cliente> findByEmail(String email);
}