package com.pettrack.citas.controllers;

import com.pettrack.citas.models.Cita;
import com.pettrack.citas.services.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/citas")
public class CitaController {
  private final CitaService citaService;

  public CitaController(CitaService citaService) {
    this.citaService = citaService;
  }

  @PostMapping
  public ResponseEntity<Cita> crearCita(@RequestBody Cita cita) {
    return ResponseEntity.ok(citaService.crearCita(cita));
  }

  @GetMapping
  public ResponseEntity<List<Cita>> listarCitas() {
    return ResponseEntity.ok(citaService.listarCitas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
    return ResponseEntity.ok(citaService.obtenerCitaPorId(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
    return ResponseEntity.ok(citaService.actualizarCita(id, cita));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
    citaService.eliminarCita(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/usuario/{idUsuario}")
  public ResponseEntity<List<Cita>> getCitasPorVeterinario(@PathVariable Long idUsuario) {
    return ResponseEntity.ok(citaService.obtenerCitasPorVeterinario(idUsuario));
  }

  @GetMapping("/mascota/{idMascota}")
  public ResponseEntity<List<Cita>> getCitasPorMascota(@PathVariable Long idMascota) {
    return ResponseEntity.ok(citaService.obtenerCitasPorMascota(idMascota));
  }
}
