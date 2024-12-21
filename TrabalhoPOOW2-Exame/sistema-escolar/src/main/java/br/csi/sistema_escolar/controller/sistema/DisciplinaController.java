package br.csi.sistema_escolar.controller.sistema;

import br.csi.sistema_escolar.model.disciplina.Disciplina;
import br.csi.sistema_escolar.service.sistema.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/disciplinas")
@Tag(name = "Disciplinas", description = "Path relacionado a operações de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas as disciplinas", description = "Retorna uma lista de todas as disciplinas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplinas encontradas"),
            @ApiResponse(responseCode = "404", description = "Disciplinas não encontradas")
    })
    public ResponseEntity<List<Disciplina>> listarDisciplinas() {
        List<Disciplina> disciplinas = this.service.listarDisciplinas();
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar disciplina por ID", description = "Retorna uma disciplina correspondente ao ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Disciplina> buscarDisciplinaPorID(@PathVariable Long id) {
        Disciplina disciplina = this.service.getDisciplina(id);
        return disciplina != null ? ResponseEntity.ok(disciplina) : ResponseEntity.notFound().build();
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Buscar disciplina por UUID", description = "Retorna uma disciplina correspondente ao UUID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Disciplina> buscarDisciplinaPorUUID(@PathVariable String uuid) {
        Disciplina disciplina = this.service.getDisciplinaUUID(uuid);
        return disciplina != null ? ResponseEntity.ok(disciplina) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova disciplina", description = "Cria uma nova disciplina e a adiciona à lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    })
    public ResponseEntity<Disciplina> salvarDisciplina(@RequestBody Disciplina disciplina) {
        this.service.salvarDisciplina(disciplina);
        URI uri = URI.create("/disciplinas" + disciplina.getUuid());
        return ResponseEntity.created(uri).body(disciplina);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma disciplina existente por ID", description = "Atualiza as informações de uma disciplina existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Disciplina> atualizarDisciplinaPorID(@PathVariable Long id, @RequestBody Disciplina disciplina) {
        Disciplina disciplinaExistente = this.service.getDisciplina(id);
        if (disciplinaExistente != null) {
            disciplina.setId(id);
            this.service.atualizarDisciplina(disciplina);
            return ResponseEntity.ok(disciplina);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/uuid/{uuid}")
    @Operation(summary = "Atualizar uma disciplina por UUID", description = "Atualiza as informações de uma disciplina existente identificada pelo UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Disciplina> atualizarDisciplinaPorUUID(@PathVariable String uuid, @RequestBody Disciplina disciplina) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Disciplina disciplinaExistente = this.service.getDisciplinaUUID(uuid);
            if (disciplinaExistente != null) {
                disciplina.setUuid(uuidFormatado);
                this.service.atualizarUuidDisciplina(uuidFormatado, disciplina);
                return ResponseEntity.ok(disciplina);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma disciplina por ID", description = "Remove uma disciplina pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Void> deletarDisciplinaPorID(@PathVariable Long id) {
        Disciplina disciplinaExistente = this.service.getDisciplina(id);
        if (disciplinaExistente != null) {
            this.service.excluirDisciplina(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/uuid/{uuid}")
    @Operation(summary = "Deletar uma disciplina por UUID", description = "Remove uma disciplina pelo seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Void> deletarDisciplinaPorUUID(@PathVariable String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Disciplina disciplinaExistente = this.service.getDisciplinaUUID(uuid);
            if (disciplinaExistente != null) {
                this.service.deletarUuidDisciplina(String.valueOf(uuidFormatado));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
