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
@Table(name = "categorianivelacesso")

public class CategoriaNivelAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "categoriaNivelAcesso", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoNivelAcesso> tipoNivelAcessoList = new ArrayList<>();

    @OneToMany(mappedBy = "categoriaNivelAcesso", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ConfigNivelAcesso> configNivelAcessoList = new ArrayList<>();

}
