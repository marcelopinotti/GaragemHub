package marcelo.HeroGarage.Carros;

import marcelo.HeroGarage.Personagem.PersonagemModel;
import marcelo.HeroGarage.Personagem.PersonagemRepository;
import marcelo.HeroGarage.exception.CarroNotFoundException;
import marcelo.HeroGarage.exception.IllegalArgumentException;
import marcelo.HeroGarage.exception.PersonagemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrosServiceTest {

    @Mock
    private CarrosRepository carrosRepository;

    @Mock
    private CarrosMapper carrosMapper;

    @Mock
    private PersonagemRepository personagemRepository;

    @InjectMocks
    private CarrosService carrosService;

    private CarrosModel carroModel;
    private CarrosDTO carroDTO;

    @BeforeEach
    void setUp() {
        carroModel = CarrosModel.builder()
                .id(1L)
                .nome("Batmobile")
                .marca("Wayne Tech")
                .modelo("Tumbler")
                .ano(2022)
                .cor("Preto")
                .cambio("Automatico")
                .foto("https://example.com/batmobile.png")
                .build();

        carroDTO = new CarrosDTO();
        carroDTO.setId(1L);
        carroDTO.setNome("Batmobile");
        carroDTO.setMarca("Wayne Tech");
        carroDTO.setModelo("Tumbler");
        carroDTO.setAno(2022);
        carroDTO.setCor("Preto");
        carroDTO.setCambio("Automatico");
        carroDTO.setFoto("https://example.com/batmobile.png");
        carroDTO.setPersonagemId(null);
    }

    @Test
    @DisplayName("Deve criar um carro com sucesso")
    void deveCriarCarro() {
        when(carrosMapper.map(any(CarrosDTO.class))).thenReturn(carroModel);
        when(carrosRepository.save(any(CarrosModel.class))).thenReturn(carroModel);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(carroDTO);

        CarrosDTO result = carrosService.criar(carroDTO);

        assertNotNull(result);
        assertEquals(carroDTO.getNome(), result.getNome());
        verify(carrosRepository).save(any(CarrosModel.class));
    }

    @Test
    @DisplayName("Deve criar um carro com personagem associado")
    void deveCriarCarroComPersonagem() {
        PersonagemModel personagem = PersonagemModel.builder().id(1L).nome("Batman").desenho("DC").build();

        CarrosDTO dtoComPersonagem = new CarrosDTO();
        dtoComPersonagem.setNome("Batmobile");
        dtoComPersonagem.setMarca("Wayne Tech");
        dtoComPersonagem.setModelo("Tumbler");
        dtoComPersonagem.setAno(2022);
        dtoComPersonagem.setPersonagemId(1L);

        when(carrosMapper.map(any(CarrosDTO.class))).thenReturn(carroModel);
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagem));
        when(carrosRepository.save(any(CarrosModel.class))).thenReturn(carroModel);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(dtoComPersonagem);

        CarrosDTO result = carrosService.criar(dtoComPersonagem);

        assertNotNull(result);
        verify(personagemRepository).findById(1L);
        verify(carrosRepository).save(any(CarrosModel.class));
    }

    @Test
    @DisplayName("Deve criar lote de carros com sucesso")
    void deveCriarLoteCarros() {
        List<CarrosDTO> dtos = List.of(carroDTO);
        List<CarrosModel> models = List.of(carroModel);

        when(carrosMapper.map(any(CarrosDTO.class))).thenReturn(carroModel);
        when(carrosRepository.saveAll(anyList())).thenReturn(models);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(carroDTO);

        List<CarrosDTO> result = carrosService.criarLote(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carrosRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve listar todos os carros")
    void deveListarTodosCarros() {
        when(carrosRepository.findAll()).thenReturn(List.of(carroModel));
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(carroDTO);

        List<CarrosDTO> result = carrosService.listarTodos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve buscar carro por ID com sucesso")
    void deveBuscarPorId() {
        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(carroDTO);

        CarrosDTO result = carrosService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(carrosRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarroNotFoundException.class, () -> carrosService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Deve atualizar carro com sucesso")
    void deveAtualizarCarro() {
        CarrosDTO dadosParaAtualizar = new CarrosDTO();
        dadosParaAtualizar.setId(1L);
        dadosParaAtualizar.setNome("Novo Batmobile");
        dadosParaAtualizar.setMarca("Wayne Tech");
        dadosParaAtualizar.setModelo("Tumbler V2");
        dadosParaAtualizar.setAno(2023);
        dadosParaAtualizar.setCor("Preto");
        dadosParaAtualizar.setCambio("Automatico");

        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));
        when(carrosRepository.save(any(CarrosModel.class))).thenReturn(carroModel);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(dadosParaAtualizar);

        CarrosDTO resultado = carrosService.atualizar(dadosParaAtualizar, 1L);

        assertNotNull(resultado);
        assertEquals("Novo Batmobile", resultado.getNome());
        assertEquals(2023, resultado.getAno());
        verify(carrosRepository).save(any(CarrosModel.class));
    }

    @Test
    @DisplayName("Deve atualizar carro atribuindo personagem")
    void deveAtualizarCarroComPersonagem() {
        PersonagemModel personagem = PersonagemModel.builder().id(1L).nome("Batman").desenho("DC").build();

        CarrosDTO dadosParaAtualizar = new CarrosDTO();
        dadosParaAtualizar.setNome("Batmobile");
        dadosParaAtualizar.setPersonagemId(1L);

        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(personagem));
        when(carrosRepository.save(any(CarrosModel.class))).thenReturn(carroModel);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(dadosParaAtualizar);

        CarrosDTO resultado = carrosService.atualizar(dadosParaAtualizar, 1L);

        assertNotNull(resultado);
        verify(personagemRepository).findById(1L);
        assertEquals(personagem, carroModel.getPersonagem());
    }

    @Test
    @DisplayName("Deve remover personagem do carro ao passar personagemId = 0")
    void deveRemoverPersonagemDoCarro() {
        PersonagemModel personagem = PersonagemModel.builder().id(1L).nome("Batman").build();
        carroModel.setPersonagem(personagem);

        CarrosDTO dadosParaAtualizar = new CarrosDTO();
        dadosParaAtualizar.setPersonagemId(0L);

        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));
        when(carrosRepository.save(any(CarrosModel.class))).thenReturn(carroModel);
        when(carrosMapper.map(any(CarrosModel.class))).thenReturn(dadosParaAtualizar);

        carrosService.atualizar(dadosParaAtualizar, 1L);

        assertNull(carroModel.getPersonagem());
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar com personagemId inexistente")
    void deveLancarExcecaoPersonagemInexistente() {
        CarrosDTO dadosParaAtualizar = new CarrosDTO();
        dadosParaAtualizar.setPersonagemId(999L);

        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));
        when(personagemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PersonagemNotFoundException.class, () -> carrosService.atualizar(dadosParaAtualizar, 1L));
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar com ano invalido")
    void deveLancarExcecaoAnoInvalido() {
        CarrosDTO updateDTO = new CarrosDTO();
        updateDTO.setAno(-1);

        when(carrosRepository.findById(1L)).thenReturn(Optional.of(carroModel));

        assertThrows(IllegalArgumentException.class, () -> carrosService.atualizar(updateDTO, 1L));
    }

    @Test
    @DisplayName("Deve deletar carro com sucesso")
    void deveDeletarCarro() {
        when(carrosRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carrosRepository).deleteById(1L);

        assertDoesNotThrow(() -> carrosService.deletar(1L));
        verify(carrosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar ID inexistente")
    void deveLancarExcecaoAoDeletarIdInexistente() {
        when(carrosRepository.existsById(1L)).thenReturn(false);

        assertThrows(CarroNotFoundException.class, () -> carrosService.deletar(1L));
        verify(carrosRepository, never()).deleteById(anyLong());
    }
}
