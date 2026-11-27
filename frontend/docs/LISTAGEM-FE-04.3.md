# FE-04.3 — Filtros da listagem de usuários

## Contrato consultado

`UsuarioController.listarUsuarios` exige POST `/usuario/usuarioList` com `filtro`. Aceita `listar todos`, `nome`, `email` e `nivel`. Os três últimos exigem `valor` como string não vazia. Para nível, o backend converte o valor com `Long.parseLong`.

## Uso

Após FE-04.5, estes métodos pertencem a `usuarioAdminService` (importar de `src/services/usuarioAdminService.js`).

```js
await usuarioAdminService.listar(); // { filtro: "listar todos" }
await usuarioAdminService.listar({ filtro: "nome", valor: "Maria" });
await usuarioAdminService.listar({ filtro: "email", valor: "maria@example.com" });
await usuarioAdminService.listar({ filtro: "nivel", valor: "1" });
```

O serviço normaliza o filtro e remove espaços externos do valor. Rejeita filtros desconhecidos, valores ausentes/não textuais e IDs de nível que não sejam inteiros positivos dentro do limite de Long. Entradas inválidas são rejeitadas antes do HTTP.

Retorno preservado: `{ usuarios: [...] }` no sucesso com conteúdo; `null` em 204. Falhas HTTP propagam `ApiError`.

Não há página de listagem na versão oficial; a alteração está no serviço disponível para os futuros consumidores.

## Validação

Testes com respostas controladas: os quatro filtros, payload e credenciais, normalização, limites de Long, rejeição sem envio de requisição, 204 e erros 401/403/500. Aprovados.

Lint e build aprovados. Consulta real ao banco ainda não validada. Backend apenas consultado.



