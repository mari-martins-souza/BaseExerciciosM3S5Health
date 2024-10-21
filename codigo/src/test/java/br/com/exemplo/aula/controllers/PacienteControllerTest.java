package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.services.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarPacientes() throws Exception {
        PacienteResponseDTO paciente1 = new PacienteResponseDTO(1L, "João", "123.456.789-00", 25);
        PacienteResponseDTO paciente2 = new PacienteResponseDTO(2L, "Maria", "987.654.321-00", 30);

        Mockito.when(pacienteService.listarPacientes()).thenReturn(Arrays.asList(paciente1, paciente2));

        mockMvc.perform(get("/pacientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Maria"));
    }

    @Test
    public void testSalvarPaciente() throws Exception {
        PacienteRequestDTO request = new PacienteRequestDTO("Carlos", "123.456.789-00", 45);
        PacienteResponseDTO response = new PacienteResponseDTO(3L, "Carlos", "123.456.789-00", 45);

        Mockito.when(pacienteService.salvarPaciente(any(PacienteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    public void testBuscarPacientePorId() throws Exception {
        PacienteResponseDTO response = new PacienteResponseDTO(1L, "João", "123.456.789-00", 25);

        Mockito.when(pacienteService.buscarPaciente(anyLong())).thenReturn(response);

        mockMvc.perform(get("/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    public void testAtualizarPaciente() throws Exception {
        PacienteRequestDTO request = new PacienteRequestDTO("Carlos", "123.456.789-00", 45);
        PacienteResponseDTO response = new PacienteResponseDTO(1L, "Carlos", "123.456.789-00", 45);

        Mockito.when(pacienteService.atualizarPaciente(anyLong(), any(PacienteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    public void testRemoverPaciente() throws Exception {
        Mockito.doNothing().when(pacienteService).removerPaciente(anyLong());

        mockMvc.perform(delete("/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
