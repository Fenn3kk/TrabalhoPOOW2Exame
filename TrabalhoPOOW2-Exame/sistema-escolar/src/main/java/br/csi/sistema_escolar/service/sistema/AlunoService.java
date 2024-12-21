package br.csi.sistema_escolar.service.sistema;

import br.csi.sistema_escolar.model.aluno.Aluno;
import br.csi.sistema_escolar.model.aluno.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public void salvarAluno(Aluno aluno) {
        this.repository.save(aluno);
    }

    public List<Aluno> listarAlunos() {
        return this.repository.findAll();
    }

    public Aluno getAluno(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public void excluirAluno(Long id) {
        this.repository.deleteById(id);
    }

    public void atualizarAluno(Aluno aluno) {
        Aluno alunoExistente = this.getAluno(aluno.getId());
        if (alunoExistente != null) {
            alunoExistente.setNome(aluno.getNome());
            alunoExistente.setEmail(aluno.getEmail());
            alunoExistente.setMatricula(aluno.getMatricula());
            this.repository.save(alunoExistente);
        }
    }

    public void atualizarUuidAluno(UUID uuid, Aluno aluno) {
        Aluno alunoExistente = this.repository.findAlunoByUuid(uuid);
        if (alunoExistente != null) {
            alunoExistente.setNome(aluno.getNome());
            alunoExistente.setEmail(aluno.getEmail());
            alunoExistente.setMatricula(aluno.getMatricula());
            this.repository.save(alunoExistente);
        }
    }

    public Aluno getAlunoUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            return this.repository.findAlunoByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
            return null; // Caso o UUID seja inválido
        }
    }

    public void deletarUuidAluno(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            this.repository.deleteAlunoByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
        }
    }
}
