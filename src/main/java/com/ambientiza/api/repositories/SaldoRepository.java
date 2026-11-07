package com.ambientiza.api.repositories;

import com.ambientiza.api.models.SaldoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaldoRepository extends JpaRepository<SaldoModel, Integer> {
}
