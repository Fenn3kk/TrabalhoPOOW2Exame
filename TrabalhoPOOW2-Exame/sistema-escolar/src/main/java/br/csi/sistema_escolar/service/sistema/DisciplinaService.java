package br.csi.sistema_escolar.service.sistema;

import br.csi.sistema_escolar.model.disciplina.Disciplina;
import br.csi.sistema_escolar.model.disciplina.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DisciplinaService {

    private final DisciplinaRepository repository;

    public DisciplinaService(DisciplinaRepository repository) {
        this.repository = repository;
    }

    public void salvarDisciplina(Disciplina disciplina) {
        this.repository.save(disciplina);
    }

    public List<Disciplina> listarDisciplinas() {
        return this.repository.findAll();
    }

    public Disciplina getDisciplina(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public void excluirDisciplina(Long id) {
        this.repository.deleteById(id);
    }

    public void atualizarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = this.getDisciplina(disciplina.getId());
        if (disciplinaExistente != null) {
            disciplinaExistente.setNome(disciplina.getNome());
            this.repository.save(disciplinaExistente);
        }
    }

    public void atualizarUuidDisciplina(UUID uuid, Disciplina disciplina) {
        Disciplina disciplinaExistente = this.repository.findDisciplinaByUuid(uuid);
        if (disciplinaExistente != null) {
            disciplinaExistente.setNome(disciplina.getNome());
            this.repository.save(disciplinaExistente);
        }
    }

    public Disciplina getDisciplinaUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            return this.repository.findDisciplinaByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void deletarUuidDisciplina(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            this.repository.deleteDisciplinaByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
        }
    }
}
