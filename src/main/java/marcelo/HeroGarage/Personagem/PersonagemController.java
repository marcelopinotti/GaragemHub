package marcelo.HeroGarage.Personagem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import marcelo.HeroGarage.exception.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
public class PersonagemController {
    private final PersonagemService personagemService;

    public PersonagemController(PersonagemService personagemService) {
        this.personagemService = personagemService;
    }

    @PostMapping("/lote")
    @Operation(summary = "Adcionar vários personagens",description = "Essa rota adiciona vários personagens de uma vez")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personagens criados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> criarLote(@RequestBody List<PersonagemDTO> personagens) {
        List<PersonagemDTO> personagensCriados = personagemService.criarLote(personagens);
        List<Long> ids = personagensCriados.stream()
                .map(PersonagemDTO::getId)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Personagens adicionados com sucesso! IDs: " + ids);
    }

    @PostMapping("/criar")
    @Operation(summary = "Adcionar somente um personagem",description = "Essa rota adiciona um personagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> criar(@RequestBody PersonagemDTO personagem) {
        PersonagemDTO personagemCriado = personagemService.criar(personagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Personagem criado com sucesso! ID: " + personagemCriado.getId());
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos os personagens", description = "Retorna uma lista com todos os personagens cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de personagens retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<PersonagemDTO>> listarTodos() {
        return ResponseEntity.ok(personagemService.listarTodos());
    }

    @GetMapping("listar/{id}")
    @Operation(summary = "Buscar personagem por Id", description = "Retorna um personagem específico pelo seu Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PersonagemDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personagemService.buscarPorId(id));
    }

    @PatchMapping("atualizar/{id}")
    @Operation(summary = "Atualizar personagem", description = "Atualiza os dados de um personagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> atualizar(
            @Parameter(description = "Id do personagem a ser atualizado")
            @PathVariable Long id,
            @Parameter(description = "Dados do personagem a ser atualizado")
            @RequestBody PersonagemDTO personagem) {
        PersonagemDTO personagemAtualizado = personagemService.atualizar(personagem, id);
        return ResponseEntity.ok("Personagem atualizado com sucesso! ID: " + personagemAtualizado.getId());
    }

    @DeleteMapping("deletar/{id}")
    @Operation(summary = "Deletar personagem", description = "Remove um personagem do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        personagemService.deletar(id);
        return ResponseEntity.ok("Personagem deletado com sucesso!");
    }
}

