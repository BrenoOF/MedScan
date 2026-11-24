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
        name = "unidadesaude",
        indexes = {
                @Index(name = "idx_endereco", columnList = "enderecoid")
        })
public class UnidadeSaude {

    @Id
    @Column(name = "cnes", nullable = false)
    private Long cnes;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enderecoid", nullable = false, unique = true)
    private Endereco endereco;

    @OneToMany(mappedBy = "unidadeSaude", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Atendente> atendenteList = new ArrayList<>();

    @OneToMany(mappedBy = "unidadeSaude", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoServico> tipoServicoList = new ArrayList<>();

    @OneToMany(mappedBy = "unidadeSaude", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoUnidadeSaude> tipoUnidadeSaudeList = new ArrayList<>();
}