package com.medscan.backend.DTO.Response;

import java.util.List;

public record TipoServicoResponse(
        Long id,
        Long unidadeSaude,
        Long tipoAgendamento
) {
}
