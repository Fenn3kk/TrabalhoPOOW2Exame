package br.csi.sistema_escolar.model.horario;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class HorarioDTO {
    private Long id;
    private UUID uuid;
    private TurmaDTO turma;
    private ProfessorDTO professor;
    private DisciplinaDTO disciplina;
    private String dia;
    private String hora;

    @Data
    @AllArgsConstructor
    public static class TurmaDTO {
        private Long id;
        private UUID uuid;
        private String nome;
    }

    @Data
    @AllArgsConstructor
    public static class ProfessorDTO {
        private Long id;
        private UUID uuid;
        private String nome;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class DisciplinaDTO {
        private Long id;
        private UUID uuid;
        private String nome;
    }
}
