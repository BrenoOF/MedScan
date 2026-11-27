# FE-03.4 — Tratamento de status HTTP

## Implementação

`apiJson` diferencia:

| Resposta | Comportamento |
| --- | --- |
| 200 e demais sucessos com JSON | Retorna o conteúdo interpretado |
| 204 ou sucesso com corpo vazio | Retorna `null` |
| 400 | `ApiError`: dados inválidos |
| 401 | `ApiError`: autenticação necessária ou credenciais inválidas |
| 403 | `ApiError`: operação sem permissão |
| 404 | `ApiError`: recurso não encontrado |
| 500 | `ApiError`: erro interno |
| Outros erros HTTP | `ApiError` com mensagem e código HTTP |
| JSON inválido em sucesso | `SyntaxError` |
| Falha de rede | Propaga o erro de transporte |

`ApiError` expõe `status` e `data` (JSON, texto ou `null`). Um corpo HTML ou vazio em uma falha HTTP não mascara o status como erro de JSON.

Login e cadastro utilizam `apiErrorMessage` para apresentar mensagens distintas. Corpos brutos do backend ficam disponíveis no erro, mas não são exibidos como mensagem ao usuário.

O cadastro aceita sucesso sem conteúdo. O login informa ausência de dados quando recebe resposta vazia, sem afirmar credenciais incorretas ou autenticação confirmada. Recuperação e expiração de sessão pertencem ao FE-07.

## Validação

Verificação com respostas controladas no Node: 200, 204, 400, 401, 403, 404, 500 e 502; erros com corpos JSON, HTML e vazios; JSON inválido em sucesso; falha de rede; mensagens distintas por status. Aprovada.

`npm run lint` e `npm run build`: aprovados.

A integração com respostas reais do Spring Boot permanece pendente de validação.



