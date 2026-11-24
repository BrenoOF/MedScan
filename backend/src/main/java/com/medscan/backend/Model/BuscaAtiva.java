package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class BuscaAtiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "buscaAtiva")
    @ToString.Exclude
    private Agendamento agendamento;

    @Column(name = "descricao", nullable = false)
    private String descricao;
}