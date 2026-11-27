# FE-04.5 — Operações de conta e administrativas

## Separação no frontend

- `usuarioService`: login e cadastro público; consulta da própria sessão e logout.
- `usuarioAdminService`: listagem e exclusão de usuários. Métodos movidos sem duplicação; não havia consumidores dessas operações na versão oficial.
- O menu deixou de oferecer �?oAdmin• indiscriminadamente. Não há página administrativa registrada na versão oficial.

## Contrato observado no backend

`UsuarioService` exige `GERENCIARUSUARIO` para criação administrativa, listagem completa, exclusão e alteração. A alteração não possui endpoint no controller.

As buscas por nome/email/nível não têm a mesma anotação de autorização no serviço recebido. No frontend elas permanecem agrupadas como gerenciamento, não como ações da própria conta. Essa separação não constitui proteção no servidor.

`UsuarioResponse` fornece nível de acesso com ID/descrição, mas não as authorities efetivas. Não há contrato confirmado que permita deduzir `GERENCIARUSUARIO` a partir desses campos.

## Limite da entrega

Separação das operações e remoção da oferta administrativa indiscriminada concluídas. Exibição dinâmica de ações para usuários autorizados depende do contrato de permissões e da gestão de sessão (FE-07/FE-08). Não foi implementado um controle de acesso completo nem alterado o backend.



