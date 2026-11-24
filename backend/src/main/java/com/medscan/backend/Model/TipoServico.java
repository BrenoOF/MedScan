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
        name = "tiposervico",
        indexes = {
                @Index(name = "idx_unidadesaude", columnList = "unidadesaudeid"),
                @Index(name = "idx_tipoagendamento", columnList = "tipoagendamentoid")
        })
public class TipoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidadesaudeid", nullable = false)
    private UnidadeSaude unidadeSaude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipoagendamentoid", nullable = false)
    private TipoAgendamento tipoAgendamento;

    @OneToMany(mappedBy = "tipoServico", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Agendamento> agendamentoList = new ArrayList<>();
}
