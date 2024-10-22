package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.ConsultaRequestDTO;
import br.com.exemplo.aula.controllers.dto.ConsultaResponseDTO;
import br.com.exemplo.aula.entities.Consulta;
import br.com.exemplo.aula.repositories.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConsultaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private EntityManager entityManager;

    private Consulta consulta;

    @BeforeEach
    void setUp() {
        consulta = new Consulta();
        consulta.setPacienteNome("João Silva");
        consulta.setMedicoNome("Dr. Carlos");
        consulta.setDescricao("Consulta de rotina");
        consultaRepository.save(consulta);
    }

    @Test
    void buscarConsultaPorId_deveRetornarConsulta() throws Exception {
        mockMvc.perform(get("/consultas/{id}", consulta.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(consulta.getId()))
                .andExpect(jsonPath("$.pacienteNome").value("João Silva"))
                .andExpect(jsonPath("$.medicoNome").value("Dr. Carlos"));
    }

    @Test
    void deletarConsultaPorId_deveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/consultas/{id}", consulta.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/consultas/{id}", consulta.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void salvarConsulta_deveCriarNovaConsulta() throws Exception {
        ConsultaRequestDTO novaConsulta = new ConsultaRequestDTO();
        novaConsulta.setPacienteNome("Maria Oliveira");
        novaConsulta.setMedicoNome("Dra. Ana");
        novaConsulta.setDescricao("Consulta inicial");

        mockMvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pacienteNome\":\"Maria Oliveira\",\"medicoNome\":\"Dra. Ana\",\"descricao\":\"Consulta inicial\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
