package br.csi.sistema_escolar.controller.sistema;

import br.csi.sistema_escolar.model.aluno.Aluno;
import br.csi.sistema_escolar.service.sistema.AlunoService;
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
@RequestMapping("/alunos")
@Tag(name = "Alunos", description = "Path relacionado a operações de alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos registrados.")
    public ResponseEntity<List<Aluno>> listarAlunos() {
        List<Aluno> alunos = this.service.listarAlunos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna um aluno correspondente ao ID fornecido")
    public ResponseEntity<Aluno> buscarAlunoPorID(@PathVariable Long id) {
        Aluno aluno = this.service.getAluno(id);
        return aluno != null ? ResponseEntity.ok(aluno) : ResponseEntity.notFound().build();
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Buscar aluno por UUID", description = "Retorna um aluno correspondente ao UUID fornecido")
    public ResponseEntity<Aluno> buscarAlunoPorUUID(@PathVariable String uuid) {
        Aluno aluno = this.service.getAlunoUUID(uuid);
        return aluno != null ? ResponseEntity.ok(aluno) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar um novo aluno", description = "Cria um novo aluno e o adiciona à lista")
    public ResponseEntity<Aluno> salvarAluno(@RequestBody Aluno aluno) {
        this.service.salvarAluno(aluno);
        URI uri = URI.create("/alunos/" + aluno.getUuid());
        return ResponseEntity.created(uri).body(aluno);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um aluno existente por ID", description = "Atualiza as informações de um aluno existente.")
    public ResponseEntity<Aluno> atualizarAlunoPorID(@PathVariable Long id, @RequestBody Aluno aluno) {
        Aluno alunoExistente = this.service.getAluno(id);
        if (alunoExistente != null) {
            aluno.setId(id);
            this.service.atualizarAluno(aluno);
            return ResponseEntity.ok(aluno);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/uuid/{uuid}")
    @Operation(summary = "Atualizar um aluno por UUID", description = "Atualiza as informações de um aluno existente identificado pelo UUID.")
    public ResponseEntity<Aluno> atualizarAlunoPorUUID(@PathVariable String uuid, @RequestBody Aluno aluno) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Aluno alunoExistente = this.service.getAlunoUUID(uuid);
            if (alunoExistente != null) {
                aluno.setUuid(uuidFormatado);
                this.service.atualizarUuidAluno(uuidFormatado, aluno);
                return ResponseEntity.ok(aluno);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um aluno por ID", description = "Remove um aluno pelo seu ID.")
    public ResponseEntity<Void> deletarAlunoPorID(@PathVariable Long id) {
        Aluno alunoExistente = this.service.getAluno(id);
        if (alunoExistente != null) {
            this.service.excluirAluno(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/uuid/{uuid}")
    @Operation(summary = "Deletar um aluno por UUID", description = "Remove um aluno pelo seu UUID.")
    public ResponseEntity<Void> deletarAlunoPorUUID(@PathVariable String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Aluno alunoExistente = this.service.getAlunoUUID(uuid);
            if (alunoExistente != null) {
                this.service.deletarUuidAluno(String.valueOf(uuidFormatado));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
