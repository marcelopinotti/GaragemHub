package marcelo.HeroGarage.Personagem;


import marcelo.HeroGarage.Carros.CarrosModel;
import marcelo.HeroGarage.Carros.CarrosRepository;
import org.springframework.stereotype.Service;
import marcelo.HeroGarage.exception.IllegalArgumentException;
import marcelo.HeroGarage.exception.PersonagemNotFoundException;

import java.util.List;
import java.util.function.Consumer;

@Service
public class PersonagemService {
    private final PersonagemRepository personagemRepository;
    private final PersonagemMapper personagemMapper;
    private final CarrosRepository carrosRepository;

    public PersonagemService(PersonagemRepository personagemRepository, PersonagemMapper personagemMapper, CarrosRepository carrosRepository) {
        this.personagemRepository = personagemRepository;
        this.personagemMapper = personagemMapper;
        this.carrosRepository = carrosRepository;
    }

    public PersonagemDTO criar(PersonagemDTO personagemDTO) {
        PersonagemModel personagem = personagemMapper.map(personagemDTO);
        personagem = personagemRepository.save(personagem);
        return personagemMapper.map(personagem);
    }

    public List<PersonagemDTO> criarLote(List<PersonagemDTO> personagens) {
        List<PersonagemModel> personagensModel = personagens.stream()
                .map(personagemMapper::map)
                .toList();
        return personagemRepository.saveAll(personagensModel).stream()
                .map(personagemMapper::map)
                .toList();
    }

    public List<PersonagemDTO> listarTodos(){
        return personagemRepository.findAll().stream()
                .map(personagemMapper::map)
                .toList();
    }

    public PersonagemDTO buscarPorId(Long id){
        return personagemRepository.findById(id)
                .map(personagemMapper::map)
                .orElseThrow(() -> new PersonagemNotFoundException("Personagem não encontrado com o id: " + id));
    }

    public PersonagemDTO atualizar(PersonagemDTO personagemDTO, Long id){
        PersonagemModel personagemExistente = personagemRepository.findById(id)
                .orElseThrow(() -> new PersonagemNotFoundException("Personagem não encontrado com o id: " + id));
        atribuirSeNaoNulo(personagemDTO.getNome(), personagemExistente::setNome);
        atribuirSeNaoNulo(personagemDTO.getDesenho(), personagemExistente::setDesenho);
        atribuirSeNaoNulo(personagemDTO.getGenero(), personagemExistente::setGenero);
        atribuirSeNaoNulo(personagemDTO.getFoto(), personagemExistente::setFoto);
        validarEAplicarIdade(personagemDTO.getIdade(), personagemExistente);
        atualizarCarrosRelacionados(personagemDTO.getCarros(), personagemExistente);

        PersonagemModel personagemSalvo = personagemRepository.save(personagemExistente);
        return personagemMapper.map(personagemSalvo);
    }
    private void validarEAplicarIdade(Integer idade, PersonagemModel personagem) {
        if (idade == null) return;
        if (idade <= 0) {
            throw new IllegalArgumentException("Idade inválida: " + idade);
        }
        personagem.setIdade(idade);
    }

    private void atualizarCarrosRelacionados(List<CarrosModel> novosCarros, PersonagemModel personagem) {
        if (novosCarros == null) return;

        List<Long> ids = novosCarros.stream()
                .map(CarrosModel::getId)
                .filter(java.util.Objects::nonNull)
                .toList();

        List<CarrosModel> carros = carrosRepository.findAllById(ids);
        carros.forEach(carro -> carro.setPersonagem(personagem));
        personagem.setCarros(carros);
    }

    private static <T> void atribuirSeNaoNulo(T valor, Consumer<T> setter) {
        if (valor != null) {
            setter.accept(valor);
        }
    }

    public void deletar(Long id){
        if (!personagemRepository.existsById(id)) {
            throw new PersonagemNotFoundException("Personagem não encontrado com o id: " + id);
        }
        personagemRepository.deleteById(id);
    }

}
