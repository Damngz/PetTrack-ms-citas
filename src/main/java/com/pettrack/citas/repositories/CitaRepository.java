package com.pettrack.citas.repositories;

import com.pettrack.citas.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
  Optional<Cita> findByIdUsuarioAndFecha(Long idUsuario, LocalDateTime fecha);
  List<Cita> findByIdUsuario(Long idUsuario);
  List<Cita> findByIdMascota(Long idMascota);
}
