package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NutricionistaServiceIntegrationTest {

    @Autowired
    private NutricionistaService nutricionistaService;

    @MockBean
    private NutricionistaRepository nutricionistaRepository;

    @Test
    public void testListarNutricionistas() {
        Nutricionista nutricionista1 = new Nutricionista(1L, "Maria Silva", "123", 5, "CRN123", "Esportiva");
        Nutricionista nutricionista2 = new Nutricionista(2L, "João Santos", "456", 8, "CRN456", "Clínica");

        when(nutricionistaRepository.findAll()).thenReturn(Arrays.asList(nutricionista1, nutricionista2));

        List<NutricionistaResponseDTO> resultado = nutricionistaService.listarNutricionistas();

        assertEquals(2, resultado.size());
        assertEquals("Maria Silva", resultado.get(0).getNome());
        assertEquals("João Santos", resultado.get(1).getNome());
    }

    @Test
    public void testSalvarNutricionista() {
        NutricionistaRequestDTO request = new NutricionistaRequestDTO("Carlos Souza", "789", 2, "CRN789", "Pediatria");

        Nutricionista nutricionistaSalvo = new Nutricionista(3L, "Carlos Souza", "789", 2, "CRN789", "Pediatria");

        when(nutricionistaRepository.findByNome("Carlos Souza")).thenReturn(Optional.empty());
        when(nutricionistaRepository.save(any(Nutricionista.class))).thenReturn(nutricionistaSalvo);

        NutricionistaResponseDTO response = nutricionistaService.salvarNutricionista(request);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("Carlos Souza", response.getNome());
        assertEquals("CRN789", response.getCrn());
    }
}
