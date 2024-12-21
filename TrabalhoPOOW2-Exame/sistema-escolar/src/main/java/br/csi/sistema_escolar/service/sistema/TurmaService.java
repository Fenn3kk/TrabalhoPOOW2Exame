package br.csi.sistema_escolar.service.sistema;

import br.csi.sistema_escolar.model.aluno.Aluno;
import br.csi.sistema_escolar.model.aluno.AlunoRepository;
import br.csi.sistema_escolar.model.turma.Turma;
import br.csi.sistema_escolar.model.turma.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    @Autowired
    public TurmaService(TurmaRepository turmaRepository, AlunoRepository alunoRepository) {
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
    }

    public void salvarTurma(Turma turma) {
        this.turmaRepository.save(turma);
    }

    public List<Turma> listarTurmas() {
        return this.turmaRepository.findAll();
    }

    public Turma getTurma(Long id) {
        return this.turmaRepository.findById(id).orElse(null);
    }

    public void excluirTurma(Long id) {
        this.turmaRepository.deleteById(id);
    }

    public void atualizarTurma(Turma turma) {
        Turma turmaExistente = this.getTurma(turma.getId());
        if (turmaExistente != null) {
            turmaExistente.setNome(turma.getNome());
            turmaExistente.setAlunos(turma.getAlunos());
            this.turmaRepository.save(turmaExistente);
        }
    }

    public void atualizarUuidTurma(UUID uuid, Turma turma) {
        Turma turmaExistente = this.turmaRepository.findTurmaByUuid(uuid);
        if (turmaExistente != null) {
            turmaExistente.setNome(turma.getNome());
            turmaExistente.setAlunos(turma.getAlunos());
            this.turmaRepository.save(turmaExistente);
        }
    }

    public Turma getTurmaByUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            return this.turmaRepository.findTurmaByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void deletarTurmaByUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            this.turmaRepository.deleteTurmaByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
        }
    }

    public List<Aluno> listarAlunosTurma(UUID turmaUUID) {
        Turma turma = turmaRepository.findTurmaByUuid(turmaUUID);
        if (turma != null) {
            return turma.getAlunos();
        }
        return Collections.emptyList();
    }

    public Turma adicionarAlunoTurma(UUID turmaUUID, UUID alunoUUID) {
        Turma turma = turmaRepository.findTurmaByUuid(turmaUUID);
        Aluno aluno = alunoRepository.findAlunoByUuid(alunoUUID);

        if (turma != null && aluno != null) {
            if (!turma.getAlunos().contains(aluno)) {
                turma.getAlunos().add(aluno);
                return turmaRepository.save(turma);
            }
        }
        return null;
    }

    public Turma removerAlunoTurma(UUID turmaUUID, UUID alunoUUID) {
        Turma turma = turmaRepository.findTurmaByUuid(turmaUUID);
        Aluno aluno = alunoRepository.findAlunoByUuid(alunoUUID);

        if (turma != null && aluno != null) {
            if (turma.getAlunos().contains(aluno)) {
                turma.getAlunos().remove(aluno);
                return turmaRepository.save(turma);
            }
        }
        return null;
    }
}
