package br.csi.sistema_escolar.model.horario;

import br.csi.sistema_escolar.model.disciplina.Disciplina;
import br.csi.sistema_escolar.model.professor.Professor;
import br.csi.sistema_escolar.model.turma.Turma;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.UUID;

@Entity
@Table(name = "Horario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_turma", "id_professor", "id_disciplina"})
})
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Entidade que representa um horário de aula no sistema")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do horário", example = "1")
    @Column(nullable = false)
    private Long id;

    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "A turma a que esse horário pertence")
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "Que professor leciona nesse horário")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "id_disciplina", nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "Que disciplina é lecionada nesse horário")
    private Disciplina disciplina;

    @Column(nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "O dia a que esse horário pertence")
    @Pattern(regexp = "^(Segunda-feira|Terça-feira|Quarta-feira|Quinta-feira|Sexta-feira|Sábado|Domingo)$", message = "O dia deve ser um dia da semana válido")
    private String dia;

    @Column(nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "Horario em que a disciplina é lecionada")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "O horário deve estar no formato HH:MM-HH:MM")
    private String hora;
}
