package marcelo.HeroGarage.Carros;


import org.springframework.stereotype.Component;

@Component
public class CarrosMapper {
    public CarrosModel map(CarrosDTO carrosDTO){
        CarrosModel carrosModel = new CarrosModel();
        carrosModel.setNome(carrosDTO.getNome());
        carrosModel.setId(carrosDTO.getId());
        carrosModel.setMarca(carrosDTO.getMarca());
        carrosModel.setModelo(carrosDTO.getModelo());
        carrosModel.setAno(carrosDTO.getAno());
        carrosModel.setPersonagem(carrosDTO.getPersonagem());
        carrosModel.setCambio(carrosDTO.getCambio());
        carrosModel.setCor(carrosDTO.getCor());
        carrosModel.setFoto((carrosDTO.getFoto()));
        return carrosModel;
    }
    public CarrosDTO map(CarrosModel carrosModel) {
        CarrosDTO carrosDTO = new CarrosDTO();
        carrosDTO.setId(carrosModel.getId());
        carrosDTO.setNome(carrosModel.getNome());
        carrosDTO.setMarca(carrosModel.getMarca());
        carrosDTO.setModelo(carrosModel.getModelo());
        carrosDTO.setAno(carrosModel.getAno());
        carrosDTO.setPersonagem(carrosModel.getPersonagem());
        carrosDTO.setCambio(carrosModel.getCambio());
        carrosDTO.setCor(carrosModel.getCor());
        carrosDTO.setFoto((carrosModel.getFoto()));
        return carrosDTO;
    }

}
