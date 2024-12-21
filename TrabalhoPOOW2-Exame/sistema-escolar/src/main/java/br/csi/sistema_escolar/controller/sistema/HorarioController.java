package br.csi.sistema_escolar.controller.sistema;

import br.csi.sistema_escolar.model.horario.Horario;
import br.csi.sistema_escolar.model.horario.HorarioDTO;
import br.csi.sistema_escolar.service.sistema.HorarioService;
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
@RequestMapping("/horarios")
@Tag(name = "Horários", description = "Path relacionado a operações de horários")
public class HorarioController {

    private final HorarioService service;

    public HorarioController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os horários", description = "Retorna uma lista de todos os horários registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horários encontrados"),
            @ApiResponse(responseCode = "404", description = "Horários não encontrados")
    })
    public ResponseEntity<List<HorarioDTO>> listarHorarios() {
        return ResponseEntity.ok(this.service.listarHorarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar horário por ID", description = "Retorna um horário correspondente ao ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário encontrado"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Horario> buscarHorarioPorId(@PathVariable Long id) {
        Horario horario = this.service.getHorario(id);
        return horario != null ? ResponseEntity.ok(horario) : ResponseEntity.notFound().build();
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Buscar horário por UUID", description = "Retorna um horário correspondente ao UUID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário encontrado"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Horario> buscarHorarioPorUuid(@PathVariable String uuid) {
        Horario horario = this.service.getHorarioByUUID(uuid);
        return horario != null ? ResponseEntity.ok(horario) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar um novo horário", description = "Cria um novo horário e o adiciona à lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    })
    public ResponseEntity<Horario> salvarHorario(@RequestBody Horario horario) {
        this.service.salvarHorario(horario);
        URI uri = URI.create("/horarios/" + horario.getUuid());
        return ResponseEntity.created(uri).body(horario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um horário existente por ID", description = "Atualiza as informações de um horário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Horario> atualizarHorarioPorId(@PathVariable Long id, @RequestBody Horario horario) {
        Horario horarioExistente = this.service.getHorario(id);
        if (horarioExistente != null) {
            horario.setId(id);
            this.service.atualizarHorario(horario);
            return ResponseEntity.ok(horario);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/uuid/{uuid}")
    @Operation(summary = "Atualizar um horário existente por UUID", description = "Atualiza as informações de um horário existente identificado pelo UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado"),
            @ApiResponse(responseCode = "400", description = "UUID inválido")
    })
    public ResponseEntity<Horario> atualizarHorarioPorUuid(@PathVariable String uuid, @RequestBody Horario horario) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Horario horarioExistente = this.service.getHorarioByUUID(uuid);
            if (horarioExistente != null) {
                horario.setUuid(uuidFormatado);
                this.service.atualizarHorarioPorUUID(String.valueOf(uuidFormatado), horario);
                return ResponseEntity.ok(horario);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um horário por ID", description = "Remove um horário pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Void> deletarHorarioPorId(@PathVariable Long id) {
        Horario horarioExistente = this.service.getHorario(id);
        if (horarioExistente != null) {
            this.service.excluirHorario(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/uuid/{uuid}")
    @Operation(summary = "Deletar um horário por UUID", description = "Remove um horário pelo seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Void> deletarHorarioPorUuid(@PathVariable String uuid) {
        Horario horarioExistente = this.service.getHorarioByUUID(uuid);
        if (horarioExistente != null) {
            this.service.excluirHorarioByUUID(uuid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
