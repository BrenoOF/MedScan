package com.medscan.backend.DTO.Request;

import com.medscan.backend.Model.Funcao;

public record NivelAcessoRequest(
        String descricao,
        Long funcaoId
) {
}
