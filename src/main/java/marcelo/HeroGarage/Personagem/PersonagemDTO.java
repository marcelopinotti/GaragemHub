package marcelo.HeroGarage.Personagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marcelo.HeroGarage.Carros.CarrosModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonagemDTO {

    private Long id;
    private String nome;
    private String desenho;
    private Integer idade;
    private String genero;
    private List<CarrosModel> carros;
    private String foto;
}
