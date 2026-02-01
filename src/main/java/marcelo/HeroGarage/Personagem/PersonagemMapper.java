package marcelo.HeroGarage.Personagem;

import org.springframework.stereotype.Component;

@Component
public class PersonagemMapper {

    public PersonagemDTO map(PersonagemModel personagemModel){
        PersonagemDTO personagemDTO = new PersonagemDTO();
        personagemDTO.setId(personagemModel.getId());
        personagemDTO.setNome(personagemModel.getNome());
        personagemDTO.setDesenho(personagemModel.getDesenho());
        personagemDTO.setIdade(personagemModel.getIdade());
        personagemDTO.setGenero(personagemModel.getGenero());
        personagemDTO.setCarros(personagemModel.getCarros());
        personagemDTO.setFoto((personagemModel.getFoto()));
        return personagemDTO;
    };
    public PersonagemModel map(PersonagemDTO personagemDTO){
        PersonagemModel personagemModel = new PersonagemModel();
        personagemModel.setId(personagemDTO.getId());
        personagemModel.setNome(personagemDTO.getNome());
        personagemModel.setDesenho(personagemDTO.getDesenho());
        personagemModel.setIdade(personagemDTO.getIdade());
        personagemModel.setGenero(personagemDTO.getGenero());
        personagemModel.setCarros(personagemDTO.getCarros());
        personagemModel.setFoto((personagemDTO.getFoto()));
        return personagemModel;
    };
}
