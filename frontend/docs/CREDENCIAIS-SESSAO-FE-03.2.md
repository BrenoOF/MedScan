# FE-03.2 — Credenciais de sessão

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/`  
**Critério de aceitação:** o navegador mantém a sessão autenticada nas operações permitidas.

## Implementação

`src/services/api.js` aplica `credentials: "include"` em todas as chamadas feitas por `apiFetch`.

A propriedade é definida **depois** das opções recebidas pelo serviço:

```js
return fetch(apiUrl(path), {
    ...requestOptions,
    credentials: "include",
    headers: {
        Accept: "application/json",
        ...headers
    }
});
```

Isso evita que uma chamada individual substitua acidentalmente o valor por `"omit"` ou `"same-origin"`.

## Operações protegidas

As operações de `usuarioService.js` passam pelo wrapper:

- login;
- cadastro;
- listagem;
- exclusão;
- edição;
- logout;
- consulta da sessão.

Nenhuma delas configura `credentials` individualmente, evitando divergência entre chamadas.

## Responsabilidades

- **Frontend:** enviar e aceitar cookies com `credentials: "include"`.
- **Proxy Vite:** encaminhar as requisições `/MedScan/**`.
- **Backend:** configurar CORS com origem explícita e `allowCredentials(true)` quando houver origens diferentes.
- **Backend:** emitir cookies com atributos compatíveis com o ambiente (`Path`, `Secure` e `SameSite`).

## Verificações

- Busca por `credentials` em `src/`: somente `src/services/api.js`.
- Busca por `fetch(` em `src/`: somente `src/services/api.js`.
- `npm run lint`: �o.
- `npm run build`: �o.

A persistência efetiva do cookie e a renovação da sessão precisam ser confirmadas com o Spring Boot real; esse backend não está disponível neste workspace.

## Critério de aceitação

**Todas as requisições do frontend enviam credenciais de sessão pelo cliente HTTP centralizado.** �o.



