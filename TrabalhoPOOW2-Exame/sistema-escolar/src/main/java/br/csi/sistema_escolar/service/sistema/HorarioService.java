package br.csi.sistema_escolar.service.sistema;

import br.csi.sistema_escolar.model.horario.Horario;
import br.csi.sistema_escolar.model.horario.HorarioDTO;
import br.csi.sistema_escolar.model.horario.HorarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HorarioService {

    private final HorarioRepository repository;

    public HorarioService(HorarioRepository repository) {
        this.repository = repository;
    }

    public void salvarHorario(Horario horario) {
        this.repository.save(horario);
    }

    public List<HorarioDTO> listarHorarios() {
        return this.repository.findAll().stream().map(horario -> new HorarioDTO(
                horario.getId(),
                horario.getUuid(),
                new HorarioDTO.TurmaDTO(
                        horario.getTurma().getId(),
                        horario.getTurma().getUuid(),
                        horario.getTurma().getNome()
                ),
                new HorarioDTO.ProfessorDTO(
                        horario.getProfessor().getId(),
                        horario.getProfessor().getUuid(),
                        horario.getProfessor().getNome(),
                        horario.getProfessor().getEmail()
                ),
                new HorarioDTO.DisciplinaDTO(
                        horario.getDisciplina().getId(),
                        horario.getDisciplina().getUuid(),
                        horario.getDisciplina().getNome()
                ),
                horario.getDia(),
                horario.getHora()
        )).toList();
    }

    public Horario getHorario(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public Horario getHorarioByUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            return this.repository.findHorarioByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void excluirHorario(Long id) {
        this.repository.deleteById(id);
    }

    public void excluirHorarioByUUID(String uuid) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            this.repository.deleteHorarioByUuid(uuidFormatado);
        } catch (IllegalArgumentException e) {
        }
    }

    public void atualizarHorario(Horario horario) {
        Horario horarioExistente = this.getHorario(horario.getId());
        if (horarioExistente != null) {
            horarioExistente.setTurma(horario.getTurma());
            horarioExistente.setProfessor(horario.getProfessor());
            horarioExistente.setDisciplina(horario.getDisciplina());
            horarioExistente.setDia(horario.getDia());
            horarioExistente.setDia(horario.getDia());
            horarioExistente.setUuid(horario.getUuid());
            this.repository.save(horarioExistente);
        }
    }

    public void atualizarHorarioPorUUID(String uuid, Horario horario) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid); // Validação do UUID
            Horario horarioExistente = this.repository.findHorarioByUuid(uuidFormatado);
            if (horarioExistente != null) {
                horarioExistente.setTurma(horario.getTurma());
                horarioExistente.setProfessor(horario.getProfessor());
                horarioExistente.setDisciplina(horario.getDisciplina());
                horarioExistente.setDia(horario.getDia());
                horarioExistente.setDia(horario.getDia());
                horarioExistente.setUuid(horario.getUuid());
                this.repository.save(horarioExistente);
            }
        } catch (IllegalArgumentException e) {
        }
    }
}
