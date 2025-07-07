package com.pettrack.citas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pettrack.citas.models.Cita;
import com.pettrack.citas.services.CitaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cita getCitaEjemplo() {
        return new Cita(
            1L,               // idMascota
            2L,               // idUsuario
            LocalDateTime.of(2025, 7, 5, 14, 0),
            "Chequeo general",
            "Pendiente"
        );
    }

    @Test
    void crearCita_deberiaRetornarCitaCreada() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(10L);

        when(citaService.crearCita(any(Cita.class))).thenReturn(cita);

        mockMvc.perform(post("/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(10))
                .andExpect(jsonPath("$.motivo").value("Chequeo general"))
                .andExpect(jsonPath("$.estado").value("Pendiente"));
    }

    @Test
    void listarCitas_deberiaRetornarLista() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(1L);

        when(citaService.listarCitas()).thenReturn(List.of(cita));

        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idMascota").value(1))
                .andExpect(jsonPath("$[0].idUsuario").value(2));
    }

    @Test
    void obtenerCitaPorId_deberiaRetornarCita() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(5L);

        when(citaService.obtenerCitaPorId(5L)).thenReturn(cita);

        mockMvc.perform(get("/citas/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(5));
    }

    @Test
    void actualizarCita_deberiaRetornarCitaActualizada() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(10L);
        cita.setMotivo("Actualizada");

        when(citaService.actualizarCita(eq(10L), any(Cita.class))).thenReturn(cita);

        mockMvc.perform(put("/citas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Actualizada"));
    }

    @Test
    void eliminarCita_deberiaRetornarNoContent() throws Exception {
        doNothing().when(citaService).eliminarCita(7L);

        mockMvc.perform(delete("/citas/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerCitasPorVeterinario_deberiaRetornarLista() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(1L);

        when(citaService.obtenerCitasPorVeterinario(2L)).thenReturn(List.of(cita));

        mockMvc.perform(get("/citas/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(2));
    }

    @Test
    void obtenerCitasPorMascota_deberiaRetornarLista() throws Exception {
        Cita cita = getCitaEjemplo();
        cita.setIdCita(2L);

        when(citaService.obtenerCitasPorMascota(1L)).thenReturn(List.of(cita));

        mockMvc.perform(get("/citas/mascota/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMascota").value(1));
    }

    @Test
    void crearCitaDuplicada_deberiaRetornarConflict() throws Exception {
        Cita cita = getCitaEjemplo();

        when(citaService.crearCita(any(Cita.class)))
                .thenThrow(new RuntimeException("El veterinario ya tiene una cita en esa fecha y hora."));

        mockMvc.perform(post("/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isConflict())  // <-- Cambiado de isInternalServerError() a isConflict()
                .andExpect(content().string("El veterinario ya tiene una cita en esa fecha y hora."));
    }
    
    
}
