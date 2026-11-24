package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "paciente",
        indexes = {
                @Index(name = "idx_usuario", columnList = "usuarioid"),
                @Index(name = "idx_dataNascimento", columnList = "datanascimento"),
                @Index(name = "idx_endereco", columnList = "enderecoid"),
                @Index(name = "idx_genero", columnList = "genero")
        })
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "datanascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false, columnDefinition = "ENUM('Masculino', 'Feminino', 'Outro')")
    private Genero genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enderecoid", nullable = false)
    private Endereco endereco;

    @Column(name = "fotoPerfilPath", length = 500)
    private String fotoPerfilPath;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Agendamento> agendamentoList = new ArrayList<>();

    public enum Genero {
        Masculino, Feminino, Outro
    }
}