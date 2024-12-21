package br.csi.sistema_escolar.model.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    public Aluno findAlunoByUuid(UUID uuid);
    public void deleteAlunoByUuid(UUID uuid);
}
