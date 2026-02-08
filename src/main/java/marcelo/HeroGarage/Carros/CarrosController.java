package marcelo.HeroGarage.Carros;

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
@RequestMapping("/carros")
public class CarrosController {

    private final CarrosService carrosService;

    public CarrosController(CarrosService carrosService) {
        this.carrosService = carrosService;
    }

    @PostMapping("/lote")
    @Operation(summary = "Adcionar vários carros",description = "Essa rota adiciona vários carros de uma vez")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carros criados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> criarLote(@RequestBody List<CarrosDTO> carros) {
        List<CarrosDTO> carrosCriados = carrosService.criarLote(carros);
        List<Long> ids = carrosCriados.stream().map(CarrosDTO::getId).toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Carros adicionados com sucesso! IDs: " + ids);
    }

    @PostMapping("/criar")
    @Operation(summary = "Adcionar somente um carro",description = "Essa rota adiciona um carro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> criar(@RequestBody CarrosDTO carro) {
        CarrosDTO carroCriado = carrosService.criar(carro);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Carro criado com sucesso! ID: " + carroCriado.getId());
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos os carros", description = "Retorna uma lista com todos os carros cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de carros retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<CarrosDTO>> listarTodos() {
        return ResponseEntity.ok(carrosService.listarTodos());
    }

    @GetMapping("listar/{id}")
    @Operation(summary = "Buscar carro por Id", description = "Retorna um carro específico pelo seu Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CarrosDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carrosService.buscarPorId(id));
    }

    @PatchMapping("atualizar/{id}")
    @Operation(summary = "Atualizar carro", description = "Atualiza os dados de um carro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> atualizar(
            @Parameter(description = "Id do carro a ser atualizado")
            @PathVariable Long id,
            @Parameter(description = "Dados do carro a ser atualizado")
            @RequestBody CarrosDTO carro) {
        CarrosDTO carroAtualizado = carrosService.atualizar(carro, id);
        return ResponseEntity.ok("Carro atualizado com sucesso! ID: " + carroAtualizado.getId());
    }

    @DeleteMapping("deletar/{id}")
    @Operation(summary = "Deletar carro", description = "Remove um carro do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        carrosService.deletar(id);
        return ResponseEntity.ok("Carro deletado com sucesso!");
    }
}
