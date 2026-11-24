package com.medscan.backend.Controller;

import com.medscan.backend.Model.Arquivo;
import com.medscan.backend.Service.ArquivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    /**
     * Upload de arquivo
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Arquivo> uploadArquivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoArquivo") Arquivo.TipoArquivo tipoArquivo,
            @RequestParam("tipoEntidade") Arquivo.TipoEntidade tipoEntidade,
            @RequestParam("idEntidade") Long idEntidade,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam(value = "descricao", required = false) String descricao
    ) {
        try {
            Arquivo arquivo = arquivoService.uploadArquivo(
                    file,
                    tipoArquivo,
                    tipoEntidade,
                    idEntidade,
            usuarioId,
                    descricao
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(arquivo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Download de arquivo
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadArquivo(@PathVariable Long id) {
        try {
            Arquivo arquivo = arquivoService.obterArquivoPorId(id);
            Resource resource = arquivoService.downloadArquivo(id);

        return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(arquivo.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + arquivo.getNomeOriginal() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Visualizar arquivo (inline no navegador)
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> visualizarArquivo(@PathVariable Long id) {
        try {
            Arquivo arquivo = arquivoService.obterArquivoPorId(id);
            Resource resource = arquivoService.downloadArquivo(id);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(arquivo.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.getNomeOriginal() + "\"")
            .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obter informações de um arquivo específico
     */
    @GetMapping("/{id}")
    public ResponseEntity<Arquivo> obterArquivo(@PathVariable Long id) {
        try {
            Arquivo arquivo = arquivoService.obterArquivoPorId(id);
            return ResponseEntity.ok(arquivo);
        } catch (IllegalArgumentException e) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Listar todos os arquivos de uma entidade
     */
    @GetMapping("/entidade/{tipoEntidade}/{idEntidade}")
    public ResponseEntity<List<Arquivo>> listarArquivosPorEntidade(
            @PathVariable Arquivo.TipoEntidade tipoEntidade,
            @PathVariable Long idEntidade
    ) {
        List<Arquivo> arquivos = arquivoService.listarArquivosPorEntidade(tipoEntidade, idEntidade);
        return ResponseEntity.ok(arquivos);
    }

    /**
     * Listar arquivos de uma entidade por tipo
     */
    @GetMapping("/entidade/{tipoEntidade}/{idEntidade}/tipo/{tipoArquivo}")
    public ResponseEntity<List<Arquivo>> listarArquivosPorTipoEEntidade(
            @PathVariable Arquivo.TipoArquivo tipoArquivo,
            @PathVariable Arquivo.TipoEntidade tipoEntidade,
            @PathVariable Long idEntidade
    ) {
        List<Arquivo> arquivos = arquivoService.listarArquivosPorTipoEEntidade(
                tipoArquivo,
                tipoEntidade,
                idEntidade
        );
        return ResponseEntity.ok(arquivos);
    }

    /**
     * Excluir arquivo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirArquivo(@PathVariable Long id) {
        try {
            arquivoService.excluirArquivo(id);
            return ResponseEntity.noContent().build();
     } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obter foto de perfil de paciente
     */
    @GetMapping("/paciente/{pacienteId}/foto-perfil")
    public ResponseEntity<Resource> obterFotoPerfilPaciente(@PathVariable Long pacienteId) {
        try {
            Arquivo arquivo = arquivoService.obterFotoPerfilPaciente(pacienteId);
            if (arquivo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = arquivoService.downloadArquivo(arquivo.getId());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(arquivo.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.getNomeOriginal() + "\"")
                  .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
