package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "configunidadesaude",
        indexes = {
                @Index(name = "idx_categoriaunidadesaude", columnList = "categoriaUnidadeSaudeid")
        }
)
public class ConfigUnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaUnidadeSaudeid", nullable = false)
    private CategoriaUnidadeSaude categoriaUnidadeSaude;

    @Column(name = "status", nullable = false)
    private Boolean status;

}
