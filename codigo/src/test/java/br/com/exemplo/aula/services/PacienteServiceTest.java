package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.entities.Paciente;
import br.com.exemplo.aula.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void listarPacientes() {
        // Dados simulados
        Paciente paciente1 = new Paciente(1L, "João", "2000-01-01", "123.456.789-00", "123456789", "joao@example.com");
        Paciente paciente2 = new Paciente(2L, "Maria", "1990-02-02", "987.654.321-00", "987654321", "maria@example.com");
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(paciente1, paciente2));

        List<PacienteResponseDTO> pacientes = pacienteService.listarPacientes();

        assertEquals(2, pacientes.size());
        assertEquals("João", pacientes.get(0).getNome());
        assertEquals("Maria", pacientes.get(1).getNome());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPaciente() {
        Paciente paciente = new Paciente(1L, "João", "2000-01-01", "123.456.789-00", "123456789", "joao@example.com");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponseDTO pacienteDTO = pacienteService.buscarPaciente(1L);

        assertNotNull(pacienteDTO);
        assertEquals("João", pacienteDTO.getNome());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    void salvarPaciente() {
        PacienteRequestDTO request = new PacienteRequestDTO("João", "2000-01-01", "123.456.789-00", "123456789", "joao@example.com");

        Paciente paciente = new Paciente(null, request.getNome(), request.getDataNascimento(), request.getCpf(), request.getTelefone(), request.getEmail());
        Paciente pacienteSalvo = new Paciente(1L, request.getNome(), request.getDataNascimento(), request.getCpf(), request.getTelefone(), request.getEmail());

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteSalvo);

        PacienteResponseDTO response = pacienteService.salvarPaciente(request);

        assertNotNull(response);
        assertEquals("João", response.getNome());
        assertEquals(1L, response.getId());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void atualizarPaciente() {
        Paciente paciente = new Paciente(1L, "João", "2000-01-01", "123.456.789-00", "123456789", "joao@example.com");
        PacienteRequestDTO request = new PacienteRequestDTO("Carlos", "1995-05-05", "123.456.789-00", "987654321", "carlos@example.com");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponseDTO response = pacienteService.atualizarPaciente(1L, request);

        assertNotNull(response);
        assertEquals("Carlos", response.getNome());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void removerPaciente() {
        pacienteService.removerPaciente(1L);

        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}
