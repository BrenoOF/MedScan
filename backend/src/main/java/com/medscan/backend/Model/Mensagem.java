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
        name = "mensagem",
        indexes = {
                @Index(name = "idx_conversa", columnList = "conversaid"),
                @Index(name = "idx_usuario", columnList = "usuarioid"),
                @Index(name = "idx_diaCadastro", columnList = "diaCadastro"),
                @Index(name = "idx_remetentetipo", columnList = "remetentetipo")
        }
)
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversaid", nullable = false)
    @ToString.Exclude
    private Conversa conversa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false)
    private Usuario usuario;

    @Convert(converter = EnumConverters.RemetenteTipoConverter.class)
    @Column(
            name = "remetentetipo",
            nullable = false,
            columnDefinition = "ENUM('Paciente', 'Atendente')"
    )
    private RemetenteTipo remetenteTipo;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Temporal(TemporalType.DATE)
    @Column(name = "diaCadastro", nullable = false)
    private LocalDate diaCadastro;

    @Temporal(TemporalType.TIME)
    @Column(name = "horaCadastro", nullable = false)
    private LocalTime horaCadastro;

    @Column(name = "lida", nullable = false)
    private Boolean lida = false;

    public enum RemetenteTipo {
        PACIENTE,
        ATENDENTE
    }
}