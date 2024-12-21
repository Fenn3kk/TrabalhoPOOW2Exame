package br.csi.sistema_escolar.model.turma;

import br.csi.sistema_escolar.model.aluno.Aluno;
import br.csi.sistema_escolar.model.horario.Horario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Turma")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Entidade que representa uma turma no sistema")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da turma", example = "1")
    @Column(nullable = false)
    private Long id;

    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    @NonNull
    @NotNull
    @Schema(description = "Nome da turma", example = "102")
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "turma_alunos",
            joinColumns = @JoinColumn(name = "id_turma", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_aluno", nullable = false)
    )
    @Schema(description = "Alunos que pertencem a essa turma")
    private List<Aluno> alunos;

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER)
    @Schema(description = "Horários de ensino dessa turma")
    private List<Horario> horarios;
}
