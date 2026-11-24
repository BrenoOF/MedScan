package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.AgendamentoRequest;
import com.medscan.backend.DTO.Response.AgendamentoResponse;
import com.medscan.backend.Model.*;
import com.medscan.backend.Repository.JPA.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final AtendenteRepository atendenteRepository;
    private final TipoServicoRepository tipoServicoRepository;
    private final BuscaAtivaRepository buscaAtivaRepository;


    @PreAuthorize("hasAuthority('CRIARAGENDAMENTO')")
    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoRequest request) {

        if (!buscaAtivaRepository.existsById(request.buscaAtivaId())) {
            throw new RuntimeException("Busca ativa não encontrada");
        }

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Atendente atendente = atendenteRepository.findById(request.atendenteId())
                .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));
        TipoServico tipoServico = tipoServicoRepository.findById(request.tipoServicoId())
                .orElseThrow(() -> new RuntimeException("Tipo de serviço não encontrado"));
        BuscaAtiva buscaAtiva = buscaAtivaRepository.getReferenceById(request.buscaAtivaId());

        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente(paciente);
        agendamento.setAtendente(atendente);
        agendamento.setTipoServico(tipoServico);
        agendamento.setBuscaAtiva(buscaAtiva);
        agendamento.setDiaAgendamento(request.diaAgendamento());
        agendamento.setHoraAgendamento(request.horaAgendamento());
        agendamento.setEscalaDor(request.escalaDor());
        agendamento.setSintomas(request.sintomas());
        agendamento.setClassificacaoRisco(Agendamento.ClassificacaoRisco.valueOf(request.classificacaoRisco()));
        agendamento.setDiaCadastro(LocalDate.now());
        agendamento.setHoraCadastro(LocalTime.now());
        agendamento.setStatus(Agendamento.Status.PENDENTE);

        return toResponse(agendamentoRepository.save(agendamento));
    }


    @PreAuthorize("hasAuthority('MUDARSTATUS')")
    @Transactional
    public AgendamentoResponse mudarStatus(Long id, String novoStatus) {

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setStatus(Agendamento.Status.valueOf(novoStatus));

        return toResponse(agendamentoRepository.save(agendamento));
    }


    @PreAuthorize("hasAuthority('VISUALIZARAGENDAMENTO')")
    public List<AgendamentoResponse> listarPorAtendente(Long atendenteId) {
        return agendamentoRepository.findAllByAtendenteId(atendenteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AgendamentoResponse toResponse(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                null, // PacienteResponse — mapeie conforme seu conversor já existente
                null, // AtendenteResponse
                null, // TipoServicoResponse
                a.getDiaCadastro(),
                a.getHoraCadastro(),
                a.getDiaAgendamento(),
                a.getHoraAgendamento(),
                a.getEscalaDor(),
                a.getSintomas(),
                a.getClassificacaoRisco().name(),
                null, // BuscaAtivaResponse
                a.getStatus().name()
        );
    }
}