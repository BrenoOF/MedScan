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
        name = "tipoagendamento",
        indexes = {
                @Index(name = "idx_descricao", columnList = "descricao")
        })
public class TipoAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;

    @OneToMany(mappedBy = "tipoAgendamento", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoServico> tipoServicoList = new ArrayList<>();
}