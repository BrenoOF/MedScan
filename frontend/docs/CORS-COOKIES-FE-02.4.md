# FE-02.4 — CORS, origem e cookies de sessão

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/`  
**Critério de aceitação:** o navegador consegue realizar as requisições permitidas sem bloqueios de CORS.

## Estratégia adotada

O modo padrão usa o proxy do Vite:

- o navegador chama o frontend em `http://localhost:5173`;
- as chamadas usam `/MedScan/...`, portanto são same-origin para o navegador;
- o Vite encaminha as requisições para `http://localhost:8080`;
- `apiFetch()` sempre usa `credentials: "include"`.

Nesse modo, o CORS não é necessário entre navegador e frontend. O backend ainda deve ter CORS correto para o modo direto e para ambientes publicados.

## Configuração necessária no Spring Boot

Quando o frontend for acessado diretamente em outra origem, o backend deve:

- permitir somente as origens conhecidas, por exemplo `http://localhost:5173`;
- responder `Access-Control-Allow-Credentials: true`;
- permitir `GET`, `POST` e `OPTIONS`;
- permitir o header `Content-Type`;
- responder o preflight `OPTIONS` para `/MedScan/**`;
- nunca usar `Access-Control-Allow-Origin: *` junto com credenciais.

Exemplo conceitual para a configuração do backend:

```java
configuration.setAllowedOrigins(List.of("http://localhost:5173"));
configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
configuration.setAllowedHeaders(List.of("Content-Type", "X-CSRF-TOKEN"));
configuration.setAllowCredentials(true);
```

O código acima é uma referência de contrato para o backend; não foi copiado para o projeto React.

## Cookies e sessão

`src/services/api.js` aplica:

```js
credentials: "include"
```

Assim, login, consulta de sessão e logout podem enviar/receber o cookie de sessão. O backend deve emitir o cookie com atributos compatíveis com o ambiente:

- `Path=/` ou um caminho que inclua `/MedScan`;
- `HttpOnly` quando o cookie não precisar ser lido por JavaScript;
- `Secure` em HTTPS;
- `SameSite` compatível com a topologia publicada.

No proxy local, o navegador permanece na origem do frontend, que é o modo recomendado para desenvolvimento.

## Verificações realizadas

| Verificação | Resultado |
| --- | --- |
| `apiFetch` define `credentials: "include"` | �o. |
| URLs das páginas passam pela camada centralizada | �o. FE-02.3 |
| Proxy dev preserva `/MedScan` | �o. FE-02.2 |
| Proxy preview preserva `/MedScan` | �o. FE-02.2 |
| `npm run lint` | �o. |
| `npm run build` | �o. |

Não foi possível validar o CORS contra o Spring Boot real porque o backend não está disponível neste workspace. Portanto, a aceitação contra a API real depende da aplicação da configuração acima no backend e de um teste com o navegador.

## Checklist para homologação

- [ ] Backend responde ao preflight `OPTIONS` com a origem exata do frontend.
- [ ] Backend retorna `Access-Control-Allow-Credentials: true`.
- [ ] Backend não retorna `Access-Control-Allow-Origin: *`.
- [ ] Login retorna o cookie de sessão.
- [ ] `usuarioSession` reconhece a sessão após atualizar a página.
- [ ] Logout invalida a sessão.
- [ ] Em produção, frontend e backend usam HTTPS quando necessário.

## Critério de aceitação

**Contrato de CORS e cookies documentado, com credenciais habilitadas no cliente.** �o.

**Validação contra o backend real:** pendente da disponibilidade/configuração do Spring Boot.



