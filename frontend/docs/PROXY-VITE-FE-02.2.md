# FE-02.2 — Proxy Vite e variáveis de ambiente

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/` (oficial FE-01.1)  
**Critério de aceitação:** requisições chegam ao Spring Boot na porta e caminho corretos.

## Ambiente adotado

**Modo padrão: mesmo origin com proxy do Vite.**

- O frontend continua chamando `/MedScan/**` (definido em `src/services/api.js`).
- O Vite encaminha `/MedScan/**` para `http://localhost:8080`, preservando o context path.
- Não é necessário definir `VITE_API_BASE_URL` no modo padrão.

## Arquivos alterados

### `vite.config.js`

- Passou a usar `defineConfig(({ mode }) => ...)` com `loadEnv`.
- Lê `VITE_BACKEND_ORIGIN` (padrão `http://localhost:8080`).
- Define o mesmo objeto de proxy para `server` (dev, porta 5173) e `preview` (build, porta 4173):

```js
'/MedScan': {
  target: backendOrigin,
  changeOrigin: true,
  secure: false
}
```

- **Sem `rewrite`**: o backend já expõe o context path `/MedScan`, então o caminho chega exatamente como enviado pelo frontend.

### `.env.example`

Documenta os dois modos:

| Modo | Configuração | Comportamento |
| --- | --- | --- |
| Proxy (padrão) | `VITE_API_BASE_URL=` e `VITE_BACKEND_ORIGIN=http://localhost:8080` | Navegador chama `/MedScan/**`; Vite encaminha ao Spring Boot |
| Direto (sem proxy) | `VITE_API_BASE_URL=http://localhost:8080` | Navegador chama a origem do backend; exige CORS liberado (FE-02.4) |

## Validação com backend simulado

Foi iniciado um servidor HTTP temporário em `127.0.0.1:8080` que devolve a URL recebida, e em seguida foram testados os dois modos do Vite.

| Modo | URL chamada no navegador | Status | URL recebida pelo backend | Host recebido |
| --- | --- | --- | --- | --- |
| `npm run dev` (5173) | `http://127.0.0.1:5173/MedScan/usuario/usuarioSession` | 200 | `/MedScan/usuario/usuarioSession` | `localhost:8080` |
| `npm run preview` (4173) | `http://127.0.0.1:4173/MedScan/usuario/usuarioSession` | 200 | `/MedScan/usuario/usuarioSession` | `localhost:8080` |

A aplicação raiz (`/`) continuou respondendo 200 com `#root` durante os testes. Os processos temporários foram encerrados ao final.

Observação: o header `Cookie` não foi usado como critério nesta validação; a propagação de sessão/cookies será tratada em **FE-02.4**.

## Verificações adicionais

- `npm run build`: �o. concluído após a alteração do `vite.config.js`.
- `npm run lint`: �o. sem avisos/erros.

## Relação com os demais requisitos

- **FE-02.1**: fornece a URL base `/MedScan` consumida pelo proxy.
- **FE-02.3**: não há URLs inconsistentes nas páginas atuais — todas passam por `usuarioService` �?' `apiFetch`.
- **FE-02.4**: validar CORS, `credentials` e cookies com o Spring Boot real.
- **FE-03**: centralizar headers, corpo JSON e tratamento de status no wrapper.

## Critério de aceitação

**Requisições chegam ao Spring Boot na porta e caminho corretos.** �o.



