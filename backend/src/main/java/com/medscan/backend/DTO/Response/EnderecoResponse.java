package com.medscan.backend.DTO.Response;

public record EnderecoResponse(
        Long id,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String cep,
        String complemento
) {
}
