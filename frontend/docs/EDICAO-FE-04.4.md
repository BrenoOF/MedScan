# FE-04.4 — Endpoint de edição inexistente

## Verificação

O `UsuarioController` do backend recebido não expõe `/usuario/usuarioEdit`. Na versão oficial, a única referência executável a essa URL estava em `usuarioService.editar`, sem consumidores nas páginas ou componentes.

## Alteração

Removido o método `editar` de `src/services/usuarioService.js`. A versão arquivada continua sendo referência histórica. A edição só deve ser reintroduzida após definição de endpoint, DTO e permissões pelo backend.

## Critério

Nenhuma tela oficial tenta executar edição real por um endpoint inexistente. Não resta referência a `usuarioEdit` ou chamada `.editar` em `src/`.

Backend apenas consultado.



