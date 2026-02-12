package marcelo.HeroGarage.Carros;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import marcelo.HeroGarage.exception.CarroNotFoundException;
import marcelo.HeroGarage.Personagem.PersonagemModel;
import marcelo.HeroGarage.Personagem.PersonagemRepository;
import marcelo.HeroGarage.exception.PersonagemNotFoundException;

@Service
public class CarrosService {

    private final CarrosRepository carrosRepository;
    private final CarrosMapper carrosMapper;
    private final PersonagemRepository personagemRepository;

    public CarrosService(CarrosRepository carrosRepository, CarrosMapper carrosMapper, PersonagemRepository personagemRepository) {
        this.carrosRepository = carrosRepository;
        this.carrosMapper = carrosMapper;
        this.personagemRepository = personagemRepository;
    }

    public CarrosDTO criar(CarrosDTO carrosDTO) {
        CarrosModel carros = carrosMapper.map(carrosDTO);
        aplicarPersonagemDoFormulario(carrosDTO, carros);
        carros = carrosRepository.save(carros);
        return carrosMapper.map(carros);
    }

    public List<CarrosDTO> criarLote(List<CarrosDTO> carros) {
        List<CarrosModel> carrosModel = carros.stream()
                .map(carrosMapper::map)
                .toList();
        return carrosRepository.saveAll(carrosModel).stream()
                .map(carrosMapper::map)
                .toList();
    }

    public List<CarrosDTO> listarTodos() {
        return carrosRepository.findAll().stream()
                .map(carrosMapper::map)
                .toList();
    }

    public CarrosDTO buscarPorId(Long id) {
        return carrosRepository.findById(id)
                .map(carrosMapper::map)
                .orElseThrow(() -> new CarroNotFoundException("Carro não encontrado com o id: " + id));
    }
    public CarrosDTO atualizar(CarrosDTO carrosDTO, Long id){
        CarrosModel carroExistente = carrosRepository.findById(id)
                .orElseThrow(() -> new CarroNotFoundException("Carro não encontrado com o id: " + id));

        atribuirSeNaoNulo(carrosDTO.getNome(), carroExistente::setNome);
        atribuirSeNaoNulo(carrosDTO.getMarca(), carroExistente::setMarca);
        atribuirSeNaoNulo(carrosDTO.getModelo(), carroExistente::setModelo);
        validarEAplicarAno(carrosDTO.getAno(), carroExistente);
        aplicarPersonagemDoFormulario(carrosDTO, carroExistente);
        atribuirSeNaoNulo(carrosDTO.getCambio(), carroExistente::setCambio);
        atribuirSeNaoNulo(carrosDTO.getCor(), carroExistente::setCor);

        CarrosModel carroSalvo = carrosRepository.save(carroExistente);
        return carrosMapper.map(carroSalvo);
    }
    private void validarEAplicarAno(Integer ano, CarrosModel carro) {
        if (ano == null || ano <= 0) return;
        carro.setAno(ano);
    }

    private static <T> void atribuirSeNaoNulo(T valor, Consumer<T> setter) {
        if (valor != null) {
            setter.accept(valor);
        }
    }

    private void aplicarPersonagemDoFormulario(CarrosDTO carrosDTO, CarrosModel carro) {
        Long personagemId = carrosDTO.getPersonagemId();

        if (personagemId == null) return;

        PersonagemModel personagem = personagemId == 0L
                ? null
                : personagemRepository.findById(personagemId)
                        .orElseThrow(() -> new PersonagemNotFoundException("Personagem não encontrado com o id: " + personagemId));

        carro.setPersonagem(personagem);
    }

    public void deletar(Long id){
        if (!carrosRepository.existsById(id)) {
            throw new CarroNotFoundException("Carro não encontrado com o id: " + id);
        }
        carrosRepository.deleteById(id);
    }
}
