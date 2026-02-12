package marcelo.HeroGarage.Personagem;


import marcelo.HeroGarage.Carros.CarrosModel;
import marcelo.HeroGarage.Carros.CarrosRepository;
import org.springframework.stereotype.Service;
import marcelo.HeroGarage.exception.PersonagemNotFoundException;

import java.util.List;
import java.util.Objects;
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
        vincularCarros(personagemDTO.getCarrosId(), personagem);
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
        PersonagemModel personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new PersonagemNotFoundException("Personagem não encontrado com o id: " + id));

        atribuirSeNaoNulo(personagemDTO.getNome(), personagem::setNome);
        atribuirSeNaoNulo(personagemDTO.getDesenho(), personagem::setDesenho);
        atribuirSeNaoNulo(personagemDTO.getGenero(), personagem::setGenero);
        atribuirSeNaoNulo(personagemDTO.getFoto(), personagem::setFoto);

        if (personagemDTO.getIdade() != null && personagemDTO.getIdade() > 0) {
            personagem.setIdade(personagemDTO.getIdade());
        }

        vincularCarros(personagemDTO.getCarrosId(), personagem);
        personagemRepository.save(personagem);
        return personagemMapper.map(personagem);
    }

    public void deletar(Long id){
        if (!personagemRepository.existsById(id)) {
            throw new PersonagemNotFoundException("Personagem não encontrado com o id: " + id);
        }
        personagemRepository.deleteById(id);
    }
    private void vincularCarros(List<Long> carrosIds, PersonagemModel personagem) {
        if (carrosIds == null) return;

        carrosRepository.findByPersonagemId(personagem.getId())
                .forEach(c -> c.setPersonagem(null));

        if (carrosIds.isEmpty()) {
            personagem.setCarros(List.of());
            return;
        }

        List<CarrosModel> carros = carrosRepository.findAllById(carrosIds);
        for (CarrosModel carro : carros) {
            carro.setPersonagem(personagem);
        }
        personagem.setCarros(carros);
    }

    private static <T> void atribuirSeNaoNulo(T valor, Consumer<T> setter) {
        if (valor != null) {
            setter.accept(valor);
        }
    }
}
