package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "agendamento",
        indexes = {
                @Index(name = "idx_tipoServico", columnList = "tiposervicoid"),
                @Index(name = "idx_diaAgendamento", columnList = "diaAgendamento"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_pacienteid", columnList = "pacienteid")
        }
)
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacienteid", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendenteid", nullable = false)
    private Atendente atendente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tiposervicoid")
    private TipoServico tipoServico;

    @Temporal(TemporalType.DATE)
    @Column(name = "diaCadastro", nullable = false)
    private LocalDate diaCadastro;

    @Temporal(TemporalType.TIME)
    @Column(name = "horaCadastro", nullable = false)
    private LocalTime horaCadastro;

    @Temporal(TemporalType.DATE)
    @Column(name = "diaAgendamento", nullable = false)
    private LocalDate diaAgendamento;

    @Temporal(TemporalType.TIME)
    @Column(name = "horaAgendamento", nullable = false)
    private LocalTime horaAgendamento;

    @Column(name = "escalador", nullable = false, length = 2)
    private Integer escalaDor;

    @Column(name = "sintomas", nullable = false, columnDefinition = "TEXT")
    private String sintomas;

    @Convert(converter = EnumConverters.ClassificacaoRiscoConverter.class)
    @Column(
            name = "classificacaorisco",
            columnDefinition = "ENUM('Não Urgente', 'Pouco Urgente', 'Urgente', 'Muito Urgente', 'Emergência')"
    )
    private ClassificacaoRisco classificacaoRisco;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "buscaativaid", nullable = false, unique = true)
    private BuscaAtiva buscaAtiva;

    @Convert(converter = EnumConverters.StatusConverter.class)
    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "ENUM('Pendente', 'Agendado', 'Rejeitado')"
    )
    private Status status;

    public enum Status {
        PENDENTE,
        AGENDADO,
        REJEITADO
    }

    public enum ClassificacaoRisco {
        NAO_URGENTE,
        POUCO_URGENTE,
        URGENTE,
        MUITO_URGENTE,
        EMERGENCIA
    }
}