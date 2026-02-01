package marcelo.HeroGarage.Carros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marcelo.HeroGarage.Personagem.PersonagemModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrosDTO {


    private Long id;
    private String nome;
    private String marca;
    private String modelo;
    private int ano;
    private PersonagemModel personagem;
    private String cambio;
    private String cor;
    private String foto;
}
