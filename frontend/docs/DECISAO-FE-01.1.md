# FE-01.1 — Definição da versão oficial do frontend

**Data:** 26/11/2026  
**Status:** Concluído

## Versões comparadas

| Aspecto | Versão A — raiz (`apenas frontend/`) | Versão B — `React-Javascript/` |
| --- | --- | --- |
| Origem | Projeto Vite mantido na pasta raiz; `tentativa.zip` é cópia idêntica destes 33 arquivos | Projeto Replit com histórico Git até 11/09/2025 |
| Stack | React 19.2, Vite 8.2, PrimeReact 11.1, react-router-dom 7.18, Oxlint | React 18.3, Vite 7.1, PrimeReact 10.9, react-router-dom 7.9, TypeScript configurado mas não usado |
| Estrutura | `src/pages`, `src/components`, `src/services`, `public` | `src/pages`, `src/components`, `src/services`, `src/assets`, `public`, `search` |
| Páginas/rotas | 4 rotas: `/`, `/login`, `/cadastro`, `/agendamento` | 17 rotas, incluindo `/home`, `/welcome`, `/ubs`, `/admin` e CRUDs |
| Serviços HTTP | `usuarioService.js` com `/usuario/usuarioLogin`, `usuarioList`, `usuarioLogout`, `usuarioSession`, etc. e `credentials: "include"` | `healthService.js` inteiramente comentado; chamadas dispersas em componentes |
| Integração com backend | Parcial e no padrão do Spring Boot (`/usuario/...`), sem sucesso simulado no login | Endpoints divergentes (`/usuarios/login`, `/cadastro/usuarios`, `/api/...`) e fallback de login simulado |
| Dados | Dados locais em `Home.jsx` e `Agendamento.jsx` | Dependência forte de JSON em `public/` (`usuarios.json`, `unidades.json`, `doencas.json`, etc.) |
| Configuração | Vite mínimo, sem proxy definido | Servidor em `0.0.0.0:5000` com `allowedHosts`, típico de Replit |
| Problemas conhecidos | Rota `/welcome` inexistente; cadastro ainda usa `/usuario/usuarioNew`; agendamento apenas simula sucesso | `Home.jsx` chama `HealthService.buscarProximos` sem import; login simula sucesso; interfaces administrativas mockadas |

## Decisão

**Projeto oficial: a versão na raiz de `apenas frontend/`.**

Critérios que pesaram na escolha:

1. �? a única versão com uma camada de serviço alinhada ao contrato real do backend (`/usuario/...` com sessão por cookie).
2. Não contém login simulado nem dezenas de telas baseadas em JSON local que precisariam ser desligadas antes de qualquer integração.
3. �? a versão mais recente (setembro/2026) e está no diretório de trabalho principal, com `package.json`, `index.html` e `src/` prontos para evolução.
4. A versão `React-Javascript` mantém value como **biblioteca de componentes/páginas** para reaproveitamento (FE-01.2), mas como base principal traz dívidas que o próprio backlog pede para corrigir (rotas inexistentes, serviço ausente, mocks, sucesso falso).
5. `tentativa.zip` foi confirmado como backup byte-a-byte da versão da raiz; não representa uma terceira base.

## Ações executadas

- `React-Javascript/` movido para `../versoes-arquivadas/React-Javascript/`.
- `tentativa.zip` movido para `../versoes-arquivadas/tentativa.zip`.
- Nenhum arquivo foi excluído: a versão arquivada permanece disponível para consulta e reaproveitamento controlado.
- A pasta oficial passa a ter um único `package.json`, um único `src/` e um único `index.html`.

## Critério de aceitação

**Existe uma única versão oficial para desenvolvimento.** �o.

A versão arquivada não deve ser usada para novas alterações. Todo trabalho seguinte (FE-01.2 em diante) é feito exclusivamente em `apenas frontend/`. O inventário de reaproveitamento da versão arquivada está em `INVENTARIO-FE-01.2.md`.



