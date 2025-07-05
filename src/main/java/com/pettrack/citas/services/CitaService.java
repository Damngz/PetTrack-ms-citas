package com.pettrack.citas.services;

import com.pettrack.citas.models.Cita;
import com.pettrack.citas.repositories.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
  private final CitaRepository citaRepository;

  public CitaService(CitaRepository citaRepository) {
    this.citaRepository = citaRepository;
  }

  public Cita crearCita(Cita cita) {
    Optional<Cita> existente = citaRepository.findByIdUsuarioAndFecha(cita.getIdUsuario(), cita.getFecha());
    if (existente.isPresent()) {
      throw new RuntimeException("El veterinario ya tiene una cita en esa fecha y hora.");
    }
    return citaRepository.save(cita);
  }

  public List<Cita> listarCitas() {
    return citaRepository.findAll();
  }

  public Cita obtenerCitaPorId(Long id) {
    return citaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
  }

  public Cita actualizarCita(Long id, Cita nuevaCita) {
    Cita existente = obtenerCitaPorId(id);
    existente.setFecha(nuevaCita.getFecha());
    existente.setMotivo(nuevaCita.getMotivo());
    existente.setEstado(nuevaCita.getEstado());
    existente.setIdMascota(nuevaCita.getIdMascota());
    existente.setIdUsuario(nuevaCita.getIdUsuario());
    return citaRepository.save(existente);
  }

  public void eliminarCita(Long id) {
    citaRepository.deleteById(id);
  }

  public List<Cita> obtenerCitasPorVeterinario(Long idUsuario) {
    return citaRepository.findByIdUsuario(idUsuario);
  }

  public List<Cita> obtenerCitasPorMascota(Long idMascota) {
    return citaRepository.findByIdMascota(idMascota);
  }
}
