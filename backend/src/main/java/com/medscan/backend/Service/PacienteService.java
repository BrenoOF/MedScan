package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.PacienteRequest;
import com.medscan.backend.DTO.Response.EnderecoResponse;
import com.medscan.backend.DTO.Response.NivelAcessoResponse;
import com.medscan.backend.DTO.Response.PacienteResponse;
import com.medscan.backend.DTO.Response.UsuarioResponse;
import com.medscan.backend.Model.Endereco;
import com.medscan.backend.Model.Paciente;
import com.medscan.backend.Model.Usuario;
import com.medscan.backend.Repository.JPA.EnderecoRepository;
import com.medscan.backend.Repository.JPA.PacienteRepository;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;

    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Endereco endereco = enderecoRepository.findById(request.enderecoId())
                .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado."));

        Paciente paciente = new Paciente();
        paciente.setCpf(request.cpf());
        paciente.setDataNascimento(request.dataNascimento());

        if (request.genero() != null) {
            paciente.setGenero(Paciente.Genero.valueOf(request.genero().toUpperCase()));
        }

        paciente.setUsuario(usuario);
        paciente.setEndereco(endereco);

        paciente = pacienteRepository.save(paciente);

        return toResponse(paciente);
    }

    public PacienteResponse buscarPerfilLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findUsuarioByEmailEqualsIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não autenticado."));

        Paciente paciente = pacienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil de paciente não encontrado."));

        return toResponse(paciente);
    }

    private PacienteResponse toResponse(Paciente paciente) {
        NivelAcessoResponse nivelAcessoResponse = null;
        if (paciente.getUsuario() != null && paciente.getUsuario().getNivelAcesso() != null) {
            nivelAcessoResponse = new NivelAcessoResponse(
                    paciente.getUsuario().getNivelAcesso().getId(),
                    paciente.getUsuario().getNivelAcesso().getDescricao()
            );
        }

        UsuarioResponse usuarioResponse = null;
        if (paciente.getUsuario() != null) {
            usuarioResponse = new UsuarioResponse(
                    paciente.getUsuario().getId(),
                    paciente.getUsuario().getNome(),
                    paciente.getUsuario().getEmail(),
                    paciente.getUsuario().getTelefone(),
                    nivelAcessoResponse
            );
        }

        EnderecoResponse enderecoResponse = null;
        if (paciente.getEndereco() != null) {
            enderecoResponse = new EnderecoResponse(
                    paciente.getEndereco().getId(),
                    paciente.getEndereco().getLogradouro(),
                    paciente.getEndereco().getNumero(),
                    paciente.getEndereco().getBairro(),
                    paciente.getEndereco().getCidade(),
                    paciente.getEndereco().getUf(),
                    paciente.getEndereco().getCep(),
                    paciente.getEndereco().getComplemento()
            );
        }

        return new PacienteResponse(
                paciente.getId(),
                usuarioResponse,
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getGenero() != null ? paciente.getGenero().name() : null,
                enderecoResponse,
                Collections.emptyList()
        );
    }
}