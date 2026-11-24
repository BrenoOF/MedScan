package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "atendente",
        indexes = {
                @Index(name = "idx_usuario", columnList = "usuarioid"),
                @Index(name = "idx_unidadesaude", columnList = "unidadesaudeid")
        }
)
public class Atendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidadesaudeid", nullable = false)
    private UnidadeSaude unidadeSaude;

    @Column(name = "fotoPerfilPath", length = 500)
    private String fotoPerfilPath;

    @OneToMany(mappedBy = "atendente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Agendamento> agendamentoList = new ArrayList<>();
}