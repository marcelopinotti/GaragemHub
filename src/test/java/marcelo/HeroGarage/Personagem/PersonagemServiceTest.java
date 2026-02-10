package marcelo.HeroGarage.Personagem;

import marcelo.HeroGarage.Carros.CarrosModel;
import marcelo.HeroGarage.Carros.CarrosRepository;
import marcelo.HeroGarage.exception.IllegalArgumentException;
import marcelo.HeroGarage.exception.PersonagemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonagemServiceTest {

    @Mock
    private PersonagemRepository personagemRepository;

    @Mock
    private PersonagemMapper personagemMapper;

    @Mock
    private CarrosRepository carrosRepository;

    @InjectMocks
    private PersonagemService personagemService;

    private PersonagemModel personagemModel;
    private PersonagemDTO personagemDTO;

    @BeforeEach
    void setUp() {
        personagemModel = PersonagemModel.builder()
                .id(1L)
                .nome("Batman")
                .desenho("DC")
                .idade(35)
                .genero("Masculino")
                .foto("https://example.com/batman.png")
                .carros(new ArrayList<>())
                .build();

        personagemDTO = new PersonagemDTO();
        personagemDTO.setId(1L);
        personagemDTO.setNome("Batman");
        personagemDTO.setDesenho("DC");
        personagemDTO.setIdade(35);
        personagemDTO.setGenero("Masculino");
        personagemDTO.setFoto("https://example.com/batman.png");
        personagemDTO.setCarros(new ArrayList<>());
        personagemDTO.setCarrosId(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve criar um personagem com sucesso")
    void deveCriarPersonagem() {
        when(personagemMapper.map(any(PersonagemDTO.class))).thenReturn(personagemModel);
        when(personagemRepository.save(any(PersonagemModel.class))).thenReturn(personagemModel);
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(personagemDTO);

        PersonagemDTO result = personagemService.criar(personagemDTO);

        assertNotNull(result);
        assertEquals(personagemDTO.getNome(), result.getNome());
        verify(personagemRepository).save(any(PersonagemModel.class));
    }

    @Test
    @DisplayName("Deve criar lote de personagens com sucesso")
    void deveCriarLotePersonagens() {
        List<PersonagemDTO> dtos = List.of(personagemDTO);
        List<PersonagemModel> models = List.of(personagemModel);

        when(personagemMapper.map(any(PersonagemDTO.class))).thenReturn(personagemModel);
        when(personagemRepository.saveAll(anyList())).thenReturn(models);
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(personagemDTO);

        List<PersonagemDTO> result = personagemService.criarLote(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personagemRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve listar todos os personagens")
    void deveListarTodosPersonagens() {
        when(personagemRepository.findAll()).thenReturn(List.of(personagemModel));
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(personagemDTO);

        List<PersonagemDTO> result = personagemService.listarTodos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve buscar personagem por ID com sucesso")
    void deveBuscarPorId() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagemModel));
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(personagemDTO);

        PersonagemDTO result = personagemService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonagemNotFoundException.class, () -> personagemService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Deve atualizar personagem com sucesso")
    void deveAtualizarPersonagem() {
        PersonagemDTO dadosParaAtualizar = new PersonagemDTO();
        dadosParaAtualizar.setId(1L);
        dadosParaAtualizar.setNome("Bruce Wayne");
        dadosParaAtualizar.setDesenho("DC");
        dadosParaAtualizar.setIdade(36);
        dadosParaAtualizar.setGenero("Masculino");
        dadosParaAtualizar.setCarrosId(new ArrayList<>());

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagemModel));
        when(personagemRepository.save(any(PersonagemModel.class))).thenReturn(personagemModel);
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(dadosParaAtualizar);

        PersonagemDTO resultado = personagemService.atualizar(dadosParaAtualizar, 1L);

        assertNotNull(resultado);
        assertEquals("Bruce Wayne", resultado.getNome());
        verify(personagemRepository).save(any(PersonagemModel.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar com idade invalida")
    void deveLancarExcecaoIdadeInvalida() {
        PersonagemDTO updateDTO = new PersonagemDTO();
        updateDTO.setIdade(-1);

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagemModel));

        assertThrows(IllegalArgumentException.class, () -> personagemService.atualizar(updateDTO, 1L));
    }

    @Test
    @DisplayName("Deve deletar personagem com sucesso")
    void deveDeletarPersonagem() {
        when(personagemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(personagemRepository).deleteById(1L);

        assertDoesNotThrow(() -> personagemService.deletar(1L));
        verify(personagemRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar ID inexistente")
    void deveLancarExcecaoAoDeletarIdInexistente() {
        when(personagemRepository.existsById(1L)).thenReturn(false);

        assertThrows(PersonagemNotFoundException.class, () -> personagemService.deletar(1L));
        verify(personagemRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve atualizar carros relacionados com sucesso")
    void deveAtualizarCarrosRelacionados() {
        CarrosModel carro = CarrosModel.builder().id(10L).nome("Batmobile").build();

        PersonagemDTO updateDTO = new PersonagemDTO();
        updateDTO.setCarrosId(List.of(10L));

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagemModel));
        when(carrosRepository.findAllById(List.of(10L))).thenReturn(List.of(carro));
        when(personagemRepository.save(any(PersonagemModel.class))).thenReturn(personagemModel);
        when(personagemMapper.map(any(PersonagemModel.class))).thenReturn(personagemDTO);

        personagemService.atualizar(updateDTO, 1L);

        verify(carrosRepository).findAllById(List.of(10L));
        assertEquals(1, personagemModel.getCarros().size());
        assertEquals(personagemModel, carro.getPersonagem());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar atribuir carro que ja tem outro dono")
    void deveLancarExcecaoCarroComOutroDono() {
        PersonagemModel outroDono = PersonagemModel.builder().id(99L).nome("Outro").build();
        CarrosModel carroComDono = CarrosModel.builder().id(10L).nome("Batmobile").personagem(outroDono).build();

        PersonagemDTO updateDTO = new PersonagemDTO();
        updateDTO.setCarrosId(List.of(10L));

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagemModel));
        when(carrosRepository.findAllById(List.of(10L))).thenReturn(List.of(carroComDono));

        assertThrows(IllegalArgumentException.class, () -> personagemService.atualizar(updateDTO, 1L));
    }
}