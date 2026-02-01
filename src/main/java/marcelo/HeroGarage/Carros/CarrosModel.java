package marcelo.HeroGarage.Carros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import marcelo.HeroGarage.Personagem.PersonagemModel;


@Entity
@Table(name = "tb_carros")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "personagem")
public class CarrosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,name = "nome_carro")
    private String nome;
    private String marca;
    private String modelo;
    private int ano;
    @Column(name = "cor")
    private String cor;
    private String cambio;
    private String foto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personagem_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // hibernateLazyInitializer para ignorar propriedades do Hibernate, e o handler para evitar problemas de serialização
    private PersonagemModel personagem;
}
