package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.services.ConsultaService;
import br.com.exemplo.aula.controllers.dto.ConsultaResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConsultaController.class)
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    private ConsultaResponseDTO consultaResponseDTO;

    @BeforeEach
    void setUp() {
        consultaResponseDTO = new ConsultaResponseDTO();
        consultaResponseDTO.setId(1L);
        consultaResponseDTO.setPacienteNome("João Silva");
    }

    @Test
    void buscarConsultaPorId_deveRetornarConsulta() throws Exception {
        Long consultaId = 1L;

        when(consultaService.buscarConsulta(consultaId)).thenReturn(consultaResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/consultas/{id}", consultaId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pacienteNome").value("João Silva"));
    }

    @Test
    void deletarConsultaPorId_deveRetornarNoContent() throws Exception {
        Long consultaId = 1L;

        doNothing().when(consultaService).deletarConsulta(consultaId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/consultas/{id}", consultaId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
