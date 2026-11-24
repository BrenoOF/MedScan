package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "conversa",
        indexes = {
                @Index(name = "idx_agendamento", columnList = "agendamentoid"),
                @Index(name = "idx_diaCadastro", columnList = "diaCadastro")
        })

public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "diaCadastro", nullable = false)
    private LocalDate diaCadastro;

    @Temporal(TemporalType.TIME)
    @Column(name = "horaCadastro", nullable = false)
    private LocalTime horaCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamentoid", nullable = false)
    private Agendamento agendamento;

    @OneToMany(mappedBy = "conversa", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Mensagem> mensagens = new ArrayList<>();

}
