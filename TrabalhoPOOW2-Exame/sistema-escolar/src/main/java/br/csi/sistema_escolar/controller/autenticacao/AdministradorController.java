package br.csi.sistema_escolar.controller.autenticacao;

import br.csi.sistema_escolar.model.administrador.Administrador;
import br.csi.sistema_escolar.model.administrador.DadosAdministrador;
import br.csi.sistema_escolar.service.autenticacao.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/administradores")
@Tag(name = "Administradores", description = "Operações relacionadas a administradores")
public class AdministradorController {

    private final AdministradorService service;

    public AdministradorController(AdministradorService service) {
        this.service = service;
    }

    @Operation(summary = "Listar administradores", description = "Retorna todos os administradores cadastrados.")
    @GetMapping
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        List<Administrador> administradores = service.listar();
        return ResponseEntity.ok(administradores);
    }

    @Operation(summary = "Obter dados detalhados", description = "Retorna dados específicos dos administradores.")
    @GetMapping("/dados")
    public ResponseEntity<List<DadosAdministrador>> listarDadosAdministradores() {
        List<DadosAdministrador> dados = service.findAllAdministradores();
        return ResponseEntity.ok(dados);
    }

    @Operation(summary = "Buscar por UUID", description = "Busca um administrador específico pelo UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador encontrado"),
            @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<Administrador> buscarAdministradorporUUID(@PathVariable String uuid) {
        return service.getAdministradorUUID(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar administrador", description = "Cadastra um novo administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Administrador.class))),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<Administrador> criarAdministrador(
            @RequestBody @Valid Administrador administrador,
            UriComponentsBuilder uriBuilder) {
        service.salvar(administrador);
        URI uri = uriBuilder.path("/administradores/{uuid}").buildAndExpand(administrador.getUuid()).toUri();
        return ResponseEntity.created(uri).body(administrador);
    }

    @Operation(summary = "Atualizar administrador", description = "Atualiza as informações de um administrador existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrador atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Administrador.class))),
            @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<Administrador> atualizarAdministradorPorUUID(@PathVariable String uuid, @RequestBody Administrador administrador) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Optional<Administrador> administradorExistente = this.service.getAdministradorUUID(uuid);
            if (administradorExistente.isPresent()) {
                administrador.setUuid(uuidFormatado);
                this.service.atualizarAdministradorPorUUID(String.valueOf(uuidFormatado), administrador);
                return ResponseEntity.ok(administrador);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Excluir administrador", description = "Remove um administrador pelo UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrador excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    })
    @DeleteMapping("/{uuid}")
    @Transactional
    public ResponseEntity<Void> excluirAdministrador(@PathVariable String uuid) {
        service.deletarUUID(uuid);
        return ResponseEntity.noContent().build();
    }
}
