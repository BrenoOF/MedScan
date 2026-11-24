package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "auditoria",
        indexes = {
                @Index(name = "idx_usuario", columnList = "usuarioid"),
                @Index(name = "idx_diaCadastro", columnList = "diaCadastro"),
                @Index(name = "idx_entidade", columnList = "entidade")
        })
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "acao", nullable = false, columnDefinition = "TEXT")
    private String acao;

    @Temporal(TemporalType.DATE)
    @Column(name = "diaCadastro", nullable = false)
    private LocalDate diaCadastro;

    @Temporal(TemporalType.TIME)
    @Column(name = "horaCadastro", nullable = false)
    private LocalTime horaCadastro;

    @Column(name = "entidade", nullable = false, length = 100)
    private String entidade;

    @Column(name = "identidade", nullable = false)
    private Long idEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false)
    private Usuario usuario;
}