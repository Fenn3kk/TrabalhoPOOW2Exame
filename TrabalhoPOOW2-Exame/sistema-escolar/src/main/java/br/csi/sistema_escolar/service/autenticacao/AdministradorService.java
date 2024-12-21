package br.csi.sistema_escolar.service.autenticacao;

import br.csi.sistema_escolar.model.administrador.DadosAdministrador;
import br.csi.sistema_escolar.model.administrador.Administrador;
import br.csi.sistema_escolar.model.administrador.AdministradorRepository;
import br.csi.sistema_escolar.model.horario.Horario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdministradorService {

    private final AdministradorRepository repository;

    public AdministradorService(AdministradorRepository repository) {
        this.repository = repository;
    }

    public void salvar(Administrador administrador) {
        System.out.println(administrador.getNome());
        administrador.setSenha(new BCryptPasswordEncoder().encode(administrador.getSenha()));
        this.repository.save(administrador);
    }

    public List<Administrador> listar() {
        return this.repository.findAll();
    }

    public Optional<Administrador> getAdministradorUUID(String uuid) {
        UUID uuidFormatado = UUID.fromString(uuid);
        return Optional.ofNullable(this.repository.findAdministradorByUuid(uuidFormatado));
    }

    public void atualizarAdministradorPorUUID(String uuid, Administrador administrador) {
        try {
            UUID uuidFormatado = UUID.fromString(uuid);
            Administrador administradorExistente = this.repository.findAdministradorByUuid(uuidFormatado);
            if (administradorExistente != null) {
                administradorExistente.setNome(administrador.getNome());
                administradorExistente.setEmail(administrador.getEmail());
                administradorExistente.setSenha(new BCryptPasswordEncoder().encode(administrador.getSenha()));
                this.repository.save(administradorExistente);
            }
        } catch (IllegalArgumentException e) {
        }
    }


    public void deletarUUID(String uuid) {
        this.repository.deleteAdministradorByUuid(UUID.fromString(uuid));
    }

    public DadosAdministrador findAdministrador(Long id){
        Administrador administrador = this.repository.getReferenceById(id);
        return new DadosAdministrador(administrador);
    }

    public List<DadosAdministrador> findAllAdministradores(){
        return this.repository.findAll().stream().map(DadosAdministrador::new).toList();
    }
}
