package marcelo.HeroGarage.Personagem;


import marcelo.HeroGarage.Carros.CarrosDTO;
import marcelo.HeroGarage.Carros.CarrosModel;
import marcelo.HeroGarage.Carros.CarrosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/personagens/ui")
public class PersonagemControllerUi {

    private final PersonagemService personagemService;
    private final CarrosService carrosService;

    public PersonagemControllerUi(PersonagemService personagemService, CarrosService carrosService) {
        this.personagemService = personagemService;
        this.carrosService = carrosService;
    }

    @GetMapping("/listar")
    public String listarTodos(Model model) {
        List<PersonagemDTO> personagens = personagemService.listarTodos();
        model.addAttribute("personagens", personagens);
        return "listarPersonagem";
    }

    @GetMapping("/listar/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        PersonagemDTO personagem = personagemService.buscarPorId(id);
        model.addAttribute("personagem", personagem);
        return "detalhesPersonagem";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        PersonagemDTO personagem = personagemService.buscarPorId(id);
        if (personagem.getCarros() != null) {
            List<Long> ids = personagem.getCarros().stream()
                    .map(CarrosModel::getId)
                    .filter(Objects::nonNull)
                    .toList();
            personagem.setCarrosId(ids);
        }
        model.addAttribute("personagem", personagem);
        List<CarrosDTO> carrosDisponiveis = carrosService.listarTodos().stream()
                .filter(carro -> carro.getPersonagem() == null
                        || Objects.equals(carro.getPersonagem().getId(), personagem.getId()))
                .collect(Collectors.toList());
        model.addAttribute("carrosDisponiveis", carrosDisponiveis);
        return "editarPersonagem";
    }

    @PostMapping("/editar/{id}")
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute("personagem") PersonagemDTO personagem) {
        personagemService.atualizar(personagem, id);
        return "redirect:/personagens/ui/listar";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        personagemService.deletar(id);
        return "redirect:/personagens/ui/listar";
    }
    @GetMapping("/adicionar")
    public String mostrarFormularioAdicionar(Model model) {
        model.addAttribute("personagem", new PersonagemDTO());
        List<CarrosDTO> carrosDisponiveis = carrosService.listarTodos().stream()
                .filter(carro -> carro.getPersonagem() == null)
                .collect(Collectors.toList());
        model.addAttribute("carrosDisponiveis", carrosDisponiveis);
        return "adicionarPersonagem";
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute PersonagemDTO personagem, RedirectAttributes redirectAttributes) {
        personagemService.criar(personagem);
        redirectAttributes.addFlashAttribute("mensagem", "Personagem criado com sucesso!");
        return "redirect:/personagens/ui/listar";
    }
}
