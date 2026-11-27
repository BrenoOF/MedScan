# FE-04.1 — Login conforme o contrato do backend

## Contrato conferido no código Spring Boot

- `UsuarioController.login`: `POST /usuario/usuarioLogin`, recebendo `email` e `senha`.
- Sucesso: HTTP 200 com `{ message, usuario }`.
- `UsuarioResponse`: `id`, `nome`, `email`, `telefone`, `nivelAcesso`.
- `UsuarioService.loginUsuario`: autentica e salva o contexto de segurança na sessão HTTP.

## Alterações no frontend

O serviço normaliza o email, preserva a senha e envia JSON com credenciais habilitadas. Valida a presença de um usuário com ID inteiro positivo, nome e email antes de retornar sucesso. Resposta vazia/204 ou incompatível gera erro de resposta, não confirmação de login.

A página rejeita email composto apenas por espaços, verifica o estado de carregamento antes de enviar e redireciona para `/`, rota existente, após sucesso. As mensagens de erro HTTP e de comunicação continuam centralizadas.

## Validação

Teste com respostas controladas usando o serviço e o cliente HTTP reais: endpoint, método, payload, senha preservada, credenciais, sucesso válido, respostas incompletas/vazias, 204, 400, 401, 403, 500 e falha de rede. Aprovado.

`npm run lint` e `npm run build`: aprovados.

O backend foi apenas consultado. Login com conta real e persistência do cookie no navegador ainda precisam de validação com o Spring Boot em execução.



