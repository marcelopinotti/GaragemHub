package marcelo.HeroGarage.Carros;




import marcelo.HeroGarage.Personagem.PersonagemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/carros/ui")
public class CarrosControllerUi {
    private final CarrosService carrosService;
    private final PersonagemService personagemService;
    public CarrosControllerUi(CarrosService carrosService, PersonagemService personagemService) {
        this.carrosService = carrosService;
        this.personagemService = personagemService;
    }

    @GetMapping("/listar")
    public String listarTodos(Model model) {
        List<CarrosDTO> carros = carrosService.listarTodos();
        model.addAttribute("carros", carros);
        return "listarCarros";
    }
    @GetMapping("/listar/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        CarrosDTO carros = carrosService.buscarPorId(id);
        model.addAttribute("carros", carros);
        return "detalhesCarros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        CarrosDTO carros = carrosService.buscarPorId(id);
        if (carros.getPersonagem() != null) {
            carros.setPersonagemId(carros.getPersonagem().getId());
        }
        model.addAttribute("carros", carros);
        model.addAttribute("personagensDisponiveis", personagemService.listarTodos());
        return "editarCarros";
    }

    @PostMapping("/editar/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute("carros") CarrosDTO carros) {
        carrosService.atualizar(carros, id);
        return "redirect:/carros/ui/listar";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        carrosService.deletar(id);
        return "redirect:/carros/ui/listar";
    }
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        model.addAttribute("carros", new CarrosDTO());
        model.addAttribute("personagensDisponiveis", personagemService.listarTodos());
        return "adicionarCarros";
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute CarrosDTO carros, RedirectAttributes redirectAttributes) {
        carrosService.criar(carros);
        redirectAttributes.addFlashAttribute("mensagem", "Carro criado com sucesso!");
        return "redirect:/carros/ui/listar";
    }
}
