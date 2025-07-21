package com.pettrack.citas.controllers;

import com.pettrack.citas.models.Cita;
import com.pettrack.citas.services.CitaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CitaControllerTest {

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaController citaController;

    @Test
    public void testCrearCita() {
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        when(citaService.crearCita(any(Cita.class))).thenReturn(cita);

        ResponseEntity<Cita> response = citaController.crearCita(cita);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(citaService).crearCita(cita);
    }

    @Test
    public void testListarCitas() {
        List<Cita> citas = Arrays.asList(
                new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(2L, 1L, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaService.listarCitas()).thenReturn(citas);

        ResponseEntity<List<Cita>> response = citaController.listarCitas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerCita() {
        Long id = 1L;
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        when(citaService.obtenerCitaPorId(id)).thenReturn(cita);

        ResponseEntity<Cita> response = citaController.obtenerCita(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarCita() {
        Long id = 1L;
        Cita cita = new Cita(1L, 1L, LocalDateTime.now(), "Consulta", "Programada");
        when(citaService.actualizarCita(id, cita)).thenReturn(cita);

        ResponseEntity<Cita> response = citaController.actualizarCita(id, cita);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testEliminarCita() {
        Long id = 1L;
        doNothing().when(citaService).eliminarCita(id);

        ResponseEntity<Void> response = citaController.eliminarCita(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(citaService).eliminarCita(id);
    }

    @Test
    public void testGetCitasPorVeterinario() {
        Long idUsuario = 1L;
        List<Cita> citas = Arrays.asList(
                new Cita(1L, idUsuario, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(2L, idUsuario, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaService.obtenerCitasPorVeterinario(idUsuario)).thenReturn(citas);

        ResponseEntity<List<Cita>> response = citaController.getCitasPorVeterinario(idUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCitasPorMascota() {
        Long idMascota = 1L;
        List<Cita> citas = Arrays.asList(
                new Cita(idMascota, 1L, LocalDateTime.now(), "Consulta", "Programada"),
                new Cita(idMascota, 1L, LocalDateTime.now().plusDays(1), "Vacuna", "Programada"));
        when(citaService.obtenerCitasPorMascota(idMascota)).thenReturn(citas);

        ResponseEntity<List<Cita>> response = citaController.getCitasPorMascota(idMascota);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testHandleRuntimeExceptionConflict() {
        RuntimeException ex = new RuntimeException("El veterinario ya tiene una cita en esa fecha y hora.");

        ResponseEntity<String> response = citaController.handleRuntimeException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(ex.getMessage(), response.getBody());
    }

    @Test
    public void testHandleRuntimeExceptionInternalError() {
        RuntimeException ex = new RuntimeException("Error genérico");

        ResponseEntity<String> response = citaController.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ex.getMessage(), response.getBody());
    }

    @Test
    public void testConstructor() {
        CitaService mockService = mock(CitaService.class);
        CitaController controller = new CitaController(mockService);
        assertNotNull(controller);
    }
}