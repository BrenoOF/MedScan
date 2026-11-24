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
        name = "nivelacesso",
        indexes = {
                @Index(name = "idx_descricao", columnList = "descricao")
        })
public class NivelAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;

    @OneToMany(mappedBy = "nivelAcesso", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Usuario> usuarioList = new ArrayList<>();

    @OneToMany(mappedBy = "nivelAcesso", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoNivelAcesso> tipoNivelAcessoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcaoid", nullable = false)
    private Funcao funcao;
}