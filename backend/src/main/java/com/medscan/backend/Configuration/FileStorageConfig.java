package com.medscan.backend.Configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${medscan.upload.dir}")
    private String uploadDir;

    /**
     * Cria os diretórios necessários para armazenamento de arquivos
     * na inicialização da aplicação
     */
    @PostConstruct
    public void init() {
        try {
            // Criar diretório principal
            Path mainDir = Paths.get(uploadDir);
            Files.createDirectories(mainDir);

            // Criar subdiretórios por tipo de entidade
            Files.createDirectories(mainDir.resolve("paciente"));
            Files.createDirectories(mainDir.resolve("agendamento"));
            Files.createDirectories(mainDir.resolve("atendente"));
            Files.createDirectories(mainDir.resolve("usuario"));

            System.out.println("Diretórios de upload criados com sucesso em: " + uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar os diretórios de upload", e);
        }
    }
}
