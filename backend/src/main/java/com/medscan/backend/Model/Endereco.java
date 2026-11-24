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
        name = "endereco",
        indexes = {
                @Index(name = "idx_cep", columnList = "cep")
        })
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "logradouro", nullable = false, length = 100)
    private String logradouro;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "bairro", nullable = false, length = 50)
    private String bairro;

    @Column(name = "cidade", nullable = false, length = 50)
    private String cidade;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "complemento", columnDefinition = "TEXT")
    private String complemento;

    @OneToOne(mappedBy = "endereco", cascade = CascadeType.ALL)
    @ToString.Exclude
    private UnidadeSaude unidadeSaude;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Paciente> pacienteList = new ArrayList<>();
}