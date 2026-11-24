package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipounidadesaude")
public class TipoUnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidadesaudeid", nullable = false)
    private UnidadeSaude unidadeSaude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaunidadesaudeid", nullable = false)
    private CategoriaUnidadeSaude categoriaUnidadeSaude;

}
