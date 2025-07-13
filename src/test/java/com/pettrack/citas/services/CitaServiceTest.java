package com.pettrack.citas.services;

import com.pettrack.citas.models.Cita;
import com.pettrack.citas.repositories.CitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {
    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    @Test
    public void testCrearCitaExitoso() {
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        when(citaRepository.findByIdUsuarioAndFecha(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(cita);

        assertNotNull(resultado);
        verify(citaRepository).save(cita);
    }

    @Test
    public void testCrearCitaConConflicto() {
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        when(citaRepository.findByIdUsuarioAndFecha(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(cita));

        assertThrows(RuntimeException.class, () -> citaService.crearCita(cita));
    }

    @Test
    public void testListarCitas() {
        List<Cita> citas = Arrays.asList(
                new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(2L, 1L, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaRepository.findAll()).thenReturn(citas);

        List<Cita> resultado = citaService.listarCitas();

        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerCitaPorIdExistente() {
        Long id = 1L;
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        cita.setIdCita(id); // Asegurarnos de establecer el ID
        when(citaRepository.findById(id)).thenReturn(Optional.of(cita));

        Cita resultado = citaService.obtenerCitaPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdCita());
    }

    @Test
    public void testObtenerCitaPorIdNoExistente() {
        Long id = 99L;
        when(citaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> citaService.obtenerCitaPorId(id));
    }

    @Test
    public void testActualizarCita() {
        Long id = 1L;
        Cita existente = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        Cita nuevaCita = new Cita(2L, 2L, LocalDateTime.now().plusDays(1), "Vacuna", "Cancelada");

        when(citaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(citaRepository.save(any(Cita.class))).thenReturn(nuevaCita);

        Cita resultado = citaService.actualizarCita(id, nuevaCita);

        assertNotNull(resultado);
        assertEquals(nuevaCita.getIdMascota(), resultado.getIdMascota());
        assertEquals(nuevaCita.getIdUsuario(), resultado.getIdUsuario());
        assertEquals(nuevaCita.getFecha(), resultado.getFecha());
        assertEquals(nuevaCita.getMotivo(), resultado.getMotivo());
        assertEquals(nuevaCita.getEstado(), resultado.getEstado());
    }

    @Test
    public void testEliminarCita() {
        Long id = 1L;
        doNothing().when(citaRepository).deleteById(id);

        citaService.eliminarCita(id);

        verify(citaRepository, times(1)).deleteById(id);
    }

    @Test
    public void testObtenerCitasPorVeterinario() {
        Long idUsuario = 1L;
        List<Cita> citas = Arrays.asList(
                new Cita(1L, idUsuario, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(2L, idUsuario, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaRepository.findByIdUsuario(idUsuario)).thenReturn(citas);

        List<Cita> resultado = citaService.obtenerCitasPorVeterinario(idUsuario);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(c -> c.getIdUsuario().equals(idUsuario)));
    }

    @Test
    public void testObtenerCitasPorMascota() {
        Long idMascota = 1L;
        List<Cita> citas = Arrays.asList(
                new Cita(idMascota, 1L, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(idMascota, 1L, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaRepository.findByIdMascota(idMascota)).thenReturn(citas);

        List<Cita> resultado = citaService.obtenerCitasPorMascota(idMascota);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(c -> c.getIdMascota().equals(idMascota)));
    }

    @Test
    public void testConstructor() {
        CitaRepository mockRepository = mock(CitaRepository.class);
        CitaService service = new CitaService(mockRepository);
        assertNotNull(service);
    }
}
