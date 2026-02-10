package marcelo.HeroGarage.Carros;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrosRepository extends JpaRepository<CarrosModel,Long> {
    List<CarrosModel> findByPersonagemId(Long personagemId);
}
