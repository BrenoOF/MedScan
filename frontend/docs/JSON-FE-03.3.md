# FE-03.3 — Envio e leitura de JSON

**Data:** 26/11/2026

## Implementação

- `apiJson(path, options)` em `src/services/api.js` serializa a opção `json` e define `Content-Type: application/json` quando há JSON a enviar.
- `apiFetch` utiliza `Headers`, aceitando objetos, instâncias de `Headers` e listas de pares.
- Respostas de sucesso são lidas uma vez: 204 e corpos vazios retornam `null`; corpos preenchidos são interpretados como JSON. JSON inválido gera `SyntaxError`.
- Informar `json` e `body` simultaneamente gera `TypeError`.
- As sete operações de `usuarioService.js` foram migradas, preservando métodos, endpoints e campos. Consulta da sessão e logout continuam sem corpo.
- Falhas de rede são propagadas. Após o FE-03.4, falhas HTTP geram `ApiError` com status e corpo; ver `STATUS-HTTP-FE-03.4.md`.

## Validação

Verificação executada com Node e respostas controladas: serialização, headers, credenciais obrigatórias, GET sem corpo, 204, corpo vazio, JSON `null`, JSON inválido, erro HTTP, falha de rede e conflito entre `json` e `body`. Todos passaram.

`npm run lint` e `npm run build`: aprovados.

Padronização concluída no cliente. A validação dos DTOs e respostas contra a API real permanece dependente do backend.



