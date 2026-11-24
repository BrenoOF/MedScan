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
@Table(name = "categoriaunidadesaude")
public class CategoriaUnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "categoriaUnidadeSaude", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TipoUnidadeSaude> tipoUnidadeSaudeList = new ArrayList<>();

    @OneToMany(mappedBy = "categoriaUnidadeSaude", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ConfigUnidadeSaude> configUnidadeSaudeList = new ArrayList<>();

}
