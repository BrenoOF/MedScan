# FE-01.4 — Dependências e configurações do `package.json`

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/` (oficial FE-01.1)  
**Critério de aceitação:** o projeto instala suas dependências, inicia e executa o build sem erros.

## Comandos definidos no `package.json`

| Script | Comando | Resultado |
| --- | --- | --- |
| `dev` | `vite` | �o. iniciou em `127.0.0.1:5173` |
| `build` | `vite build` | �o. concluído sem erros |
| `lint` | `oxlint` | �o. sem erros e sem avisos após ajustes |
| `preview` | `vite preview` | Disponível (não executado nesta validação) |

## Validações executadas

### 1. Instalação

```text
npm install --no-audit --no-fund
added 39 packages in 16s
```

### 2. Build de produção

```text
npm run build
vite v8.2.1 building client environment for production...
�o" 76 modules transformed.
dist/index.html                          0.46 kB
dist/assets/index-*.css                  24.01 kB
dist/assets/index-*.js                  308.18 kB
�o" built in 651ms
```

### 3. Lint

Antes dos ajustes, `oxlint` apontava 2 avisos:

- `Login.jsx`: parâmetro `error` do `catch` não usado.
- `Cadastro.jsx`: import de `Button` não usado.

Correções aplicadas:

- `Login.jsx`: `console.error("Erro no login:", error)` dentro do `catch`.
- `Cadastro.jsx`: remoção do import não usado de `Button`.

Após as correções: `oxlint` terminou sem saída de avisos/erros.

### 4. Inicialização do servidor de desenvolvimento

Servidor iniciado com:

```text
npm run dev -- --host 127.0.0.1 --port 5173
VITE v8.2.1 ready in 421 ms
```

Requisições verificadas:

| Rota | Status | HTML com `#root` |
| --- | --- | --- |
| `/` | 200 | �o. |
| `/login` | 200 | �o. |
| `/cadastro` | 200 | �o. |
| `/agendamento` | 200 | �o. |

O processo do servidor foi encerrado ao final do teste.

## Dependências instaladas (`npm ls --depth=0`)

```text
frontend@0.0.0
+-- @types/react-dom@19.2.4
+-- @types/react@19.2.18
+-- @vitejs/plugin-react@6.0.5
+-- oxlint@1.78.0
+-- primeicons@8.0.2
+-- primereact@11.1.0
+-- react-dom@19.2.8
+-- react-router-dom@7.18.2
+-- react@19.2.8
`-- vite@8.2.1
```

Observações sobre versões:

- `react@19.2.8` e `react-dom@19.2.8` atendem ao peer dependency do `primereact@11.1.0` (`>=19`).
- `PrimeReactProvider` é importado de `@primereact/core`, pacote incluído pelo `primereact@11.1.0`; a resolução foi confirmada pelo build.
- O `index.jsx` ainda não importa um tema do PrimeReact. Na v11 os temas não estão em `primereact/resources/...` (caminho da versão 10) e nenhum pacote de tema está instalado. Como as telas atuais usam poucos componentes PrimeReact, isso **não bloqueia** instalação, build ou inicialização; a padronização visual fica para **FE-14**.
- Não há script `test` no `package.json`; testes automatizados não fazem parte deste requisito.

## Critério de aceitação

**O projeto instala suas dependências, inicia e executa o build sem erros.** �o.

- `npm install`: ok.
- `npm run dev`: ok, com fallback de rotas SPA funcionando.
- `npm run build`: ok.
- `npm run lint`: ok (após remoção de imports/parâmetros não usados).

Próximo passo sugerido: retomar a ordem do backlog em **FE-02 — Configurar a conexão com a API Spring Boot** (FE-02.1: endereço base único com context path `/MedScan`).



