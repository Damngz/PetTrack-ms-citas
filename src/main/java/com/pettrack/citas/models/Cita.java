package com.pettrack.citas.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "id_usuario", "fecha" })
})
public class Cita {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_cita")
  private Long idCita;

  @Column(name = "id_mascota", nullable = false)
  private Long idMascota;

  @Column(name = "id_usuario", nullable = false)
  private Long idUsuario;

  @Column(name = "fecha", nullable = false)
  private LocalDateTime fecha;

  @Column(name = "motivo")
  private String motivo;

  @Column(name = "estado")
  private String estado;

  public Cita() {
  }

  public Cita(Long idMascota, Long idUsuario, LocalDateTime fecha, String motivo, String estado) {
    this.idMascota = idMascota;
    this.idUsuario = idUsuario;
    this.fecha = fecha;
    this.motivo = motivo;
    this.estado = estado;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdMascota() {
    return idMascota;
  }

  public void setIdMascota(Long idMascota) {
    this.idMascota = idMascota;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }
}
