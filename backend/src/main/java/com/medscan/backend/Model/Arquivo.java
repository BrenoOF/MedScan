package com.medscan.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "arquivo",
        indexes = {
                @Index(name = "idx_tipoEntidade", columnList = "tipoEntidade"),
                @Index(name = "idx_idEntidade", columnList = "idEntidade"),
                @Index(name = "idx_dataUpload", columnList = "dataUpload")
        }
)
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nomeOriginal", nullable = false, length = 255)
    private String nomeOriginal;

    @Column(name = "nomeArquivo", nullable = false, length = 255, unique = true)
    private String nomeArquivo;

    @Column(name = "caminhoArquivo", nullable = false, length = 500)
    private String caminhoArquivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoArquivo", nullable = false, columnDefinition = "ENUM('FOTO_PERFIL', 'DOCUMENTO_AGENDAMENTO', 'LAUDO_EXAME', 'RECEITA_MEDICA', 'OUTRO')")
    private TipoArquivo tipoArquivo;

    @Column(name = "mimeType", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "tamanhoBytes", nullable = false)
    private Long tamanhoBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoEntidade", nullable = false, columnDefinition = "ENUM('PACIENTE', 'AGENDAMENTO', 'ATENDENTE', 'USUARIO')")
    private TipoEntidade tipoEntidade;

    @Column(name = "idEntidade", nullable = false)
    private Long idEntidade;

    @Column(name = "dataUpload", nullable = false)
    private LocalDateTime dataUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadPorUsuarioId", nullable = false)
    private Usuario uploadPorUsuario;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    public enum TipoArquivo {
        FOTO_PERFIL("Foto de Perfil"),
        DOCUMENTO_AGENDAMENTO("Documento de Agendamento"),
        LAUDO_EXAME("Laudo de Exame"),
        RECEITA_MEDICA("Receita Médica"),
        OUTRO("Outro");

        private final String descricao;

        TipoArquivo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum TipoEntidade {
        PACIENTE("Paciente"),
        AGENDAMENTO("Agendamento"),
        ATENDENTE("Atendente"),
        USUARIO("Usuário");

        private final String descricao;

        TipoEntidade(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
