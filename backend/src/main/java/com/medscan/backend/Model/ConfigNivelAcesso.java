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
        name = "confignivelacesso",
        indexes = {
                @Index(name = "idx_categorianivelacesso", columnList = "categoriaNivelAcessoid")
        }
)
public class ConfigNivelAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaNivelAcessoid", nullable = false)
    private CategoriaNivelAcesso categoriaNivelAcesso;

    @Column(name = "status", nullable = false)
    private Boolean status;
}