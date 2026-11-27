# FE-03.1 — Cliente HTTP centralizado

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/`  
**Critério de aceitação:** as requisições seguem um padrão único.

## Implementação

`src/services/api.js` é o único ponto autorizado a executar `fetch`.

O `apiFetch(path, options)` centraliza:

- montagem da URL com `/MedScan`;
- `credentials: "include"` em todas as requisições;
- header `Accept: "application/json"`;
- preservação dos headers específicos enviados pelo serviço;
- encaminhamento dos demais parâmetros (`method`, `body`, `signal`, entre outros).

Os serviços de domínio, como `usuarioService.js`, informam apenas o endpoint e os dados da operação. Componentes e páginas não executam HTTP diretamente.

## Proteção contra regressão

O `.oxlintrc.json` bloqueia o uso direto de `fetch` com `no-restricted-globals`. A única exceção é `src/services/api.js`.

## Verificação

- Busca no código: `fetch(` aparece somente em `src/services/api.js`.
- `npm run lint`: �o.
- `npm run build`: �o.
- `usuarioService.js` continua utilizando `apiFetch` em suas sete operações.

O tratamento detalhado de corpos, status HTTP e CSRF será evoluído nos requisitos FE-03.3, FE-03.4 e FE-03.5, sem duplicar a camada de transporte.

## Critério de aceitação

**As requisições seguem um padrão único.** �o.



