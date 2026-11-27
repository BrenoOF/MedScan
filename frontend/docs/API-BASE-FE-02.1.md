# FE-02.1 — Endereço base único da API

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/` (oficial FE-01.1)  
**Critério de aceitação:** nenhum componente precisa repetir manualmente a URL completa do backend.

## Decisão

Foi criado o módulo único `src/services/api.js`, responsável por:

- definir o context path fixo `/MedScan`;
- montar a URL base a partir de `VITE_API_BASE_URL`;
- expor `apiUrl(path)` e `apiFetch(path, options)`;
- aplicar `credentials: "include"` por padrão em todas as requisições.

Nenhum componente React conhece a URL do backend. Eles continuam chamando apenas `usuarioService`.

## Regras de montagem da URL

| `VITE_API_BASE_URL` | URL final usada |
| --- | --- |
| vazio / não definido | `/MedScan` (mesmo origin, via proxy do Vite — FE-02.2) |
| `http://localhost:8080` | `http://localhost:8080/MedScan` |
| `http://localhost:8080/` | `http://localhost:8080/MedScan` |
| `http://localhost:8080/MedScan` | `http://localhost:8080/MedScan` (não duplica o context path) |

Exemplo de chamada: `apiFetch("/usuario/usuarioLogin", { method: "POST", ... })`.

## Arquivos alterados

| Arquivo | Alteração |
| --- | --- |
| `src/services/api.js` | **Novo** — único ponto de configuração da URL base e do `fetch` |
| `src/services/usuarioService.js` | Todas as 7 chamadas trocadas de `fetch(...)` para `apiFetch(...)`; `credentials: "include"` removido das chamadas (agora é padrão no wrapper) |
| `.env.example` | **Novo** — documenta `VITE_API_BASE_URL` sem expor valores sensíveis |

## Verificação

- `npm run lint`: �o. sem avisos/erros.
- `npm run build`: �o. sem erros; o bundle contém o context path `/MedScan`.
- Busca no `src/`: `fetch(` e `credentials:` aparecem **somente** em `src/services/api.js`.
- Nenhuma página/componente contém `http://...`, `/MedScan` ou caminho completo do backend.

## Próximos passos relacionados

- **FE-02.2**: configurar proxy Vite / variáveis de ambiente por ambiente — **concluído**, ver `PROXY-VITE-FE-02.2.md`.
- **FE-02.3**: eliminar URLs inconsistentes restantes (já não há nas páginas atuais).
- **FE-02.4**: verificar CORS/cookies com o Spring Boot.
- **FE-03**: padronizar corpo JSON, headers e tratamento de status dentro do wrapper.

## Critério de aceitação

**Nenhum componente precisa repetir manualmente a URL completa do backend.** �o.



