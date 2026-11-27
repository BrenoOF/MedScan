# FE-04.2 — Cadastro público

## Contrato verificado

O `UsuarioController` expõe `POST /usuario/usuarioCadastro`. O `SecurityConfig` permite acesso sem autenticação a essa rota. O serviço exige nome, email e senha, usa telefone padrão quando omitido e atribui o nível de paciente no servidor.

## Alterações

- `usuarioService.cadastrar` passou de `/usuario/usuarioNew` para `/usuario/usuarioCadastro`.
- Payload permanece `{ nome, email, senha }`, sem ID ou nível de acesso.
- Formulário rejeita campos preenchidos apenas com espaços, valida email após remover espaços externos e verifica carregamento antes de enviar.
- Senha é enviada sem alteração; nome e email são normalizados pelo formulário.
- Falhas HTTP e de rede continuam passando pelo tratamento centralizado.

## Verificação

Teste com respostas controladas: URL pública com `/MedScan`, POST, JSON, credenciais, campos enviados, resposta de sucesso e propagação de 400/403/500 e falha de rede. Aprovado.

`npm run lint` e `npm run build`: aprovados.

Backend apenas consultado. Cadastro de uma conta real no banco ainda não foi validado.



