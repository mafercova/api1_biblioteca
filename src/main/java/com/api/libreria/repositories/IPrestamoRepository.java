package com.api.libreria.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libreria.models.PrestamoModel;

public interface IPrestamoRepository extends JpaRepository<PrestamoModel, Long> {
    List<PrestamoModel> findByFechaDevolucionIsNull();

    Optional<PrestamoModel> findBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(Long socioId, Long isbn);
}