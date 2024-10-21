package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class NutricionistaServiceTest {

    @Mock
    private NutricionistaRepository nutricionistaRepository;

    @InjectMocks
    private NutricionistaService nutricionistaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarNutricionistas() {
        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setId(1L);
        nutricionista.setNome("Dr. Nutri");

        List<Nutricionista> nutricionistas = new ArrayList<>();
        nutricionistas.add(nutricionista);

        when(nutricionistaRepository.findAll()).thenReturn(nutricionistas);

        List<NutricionistaResponseDTO> result = nutricionistaService.listarNutricionistas();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNome()).isEqualTo("Dr. Nutri");
        verify(nutricionistaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarNutricionista() {
        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setId(1L);
        nutricionista.setNome("Dr. Nutri");

        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));

        NutricionistaResponseDTO result = nutricionistaService.buscarNutricionista(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Dr. Nutri");
        verify(nutricionistaRepository, times(1)).findById(1L);
    }

    @Test
    void testSalvarNutricionista() {
        NutricionistaRequestDTO request = new NutricionistaRequestDTO();
        request.setNome("Dr. Nutri");
        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setNome("Dr. Nutri");

        when(nutricionistaRepository.save(any(Nutricionista.class))).thenReturn(nutricionista);

        NutricionistaResponseDTO result = nutricionistaService.salvarNutricionista(request);

        assertThat(result.getNome()).isEqualTo("Dr. Nutri");
        verify(nutricionistaRepository, times(1)).save(any(Nutricionista.class));
    }

    @Test
    void testAtualizarNutricionista() {
        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setId(1L);
        nutricionista.setNome("Dr. Nutri");

        NutricionistaRequestDTO request = new NutricionistaRequestDTO();
        request.setNome("Dr. Novo Nutri");

        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));
        when(nutricionistaRepository.save(nutricionista)).thenReturn(nutricionista);

        NutricionistaResponseDTO result = nutricionistaService.atualizarNutricionista(1L, request);

        assertThat(result.getNome()).isEqualTo("Dr. Novo Nutri");
        verify(nutricionistaRepository, times(1)).findById(1L);
        verify(nutricionistaRepository, times(1)).save(nutricionista);
    }

    @Test
    void testRemoverNutricionista() {
        Long id = 1L;

        doNothing().when(nutricionistaRepository).deleteById(id);

        nutricionistaService.removerNutricionista(id);

        verify(nutricionistaRepository, times(1)).deleteById(id);
    }
}
