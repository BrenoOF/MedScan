package com.medscan.backend.DTO.Request;

public record EnderecoRequest(
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String cep,
        String complemento
) {
}
