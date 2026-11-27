# RELATORIO_INTEGRACAO

O relatório consolidado está em [`docs/AUDITORIA-INTEGRACAO.md`](docs/AUDITORIA-INTEGRACAO.md).

## Tabela de funcionalidades

| ID | Funcionalidade | Endpoint real | Método HTTP | Situação | Pendências |
|---|---|---|---|---|---|
| FE-04.1 | Login | `/usuario/usuarioLogin` | POST | Parcial | Validar sessão com backend/banco |
| FE-04.2 | Cadastro completo de paciente | `/paciente/pacienteCadastroCompleto` | POST | Implementada no frontend/backend | Validar com MariaDB |
| FE-04.3 | Listagem administrativa | `/usuario/usuarioList` | POST | Parcial | Permissão e tela administrativa |
| FE-04.4 | Edição | Não existe | — | Bloqueada | Backend não expõe endpoint |
| FE-04.5 | Permissões e perfil | `nivelAcesso` em `/usuario/usuarioSession` | GET | Implementada no frontend | Validar perfis reais com banco |
| FE-05.1 | Login sem sucesso simulado | `/usuario/usuarioLogin` | POST | Implementada | Falhas permanecem na tela de login |
| FE-05.2 | Cadastro sem sucesso simulado | `/paciente/pacienteCadastroCompleto` | POST | Implementada | Falhas permanecem no cadastro |
| FE-05.3 | Remover persistência local simulada | Não se aplica | — | Implementada | Nenhuma pendência no frontend oficial |
| FE-05.4 | Confirmação de agendamento persistido | `/agendamento/agendamentoNew` | POST | Implementada | Validar com MariaDB |
| FE-06.1 | Rotas registradas | `src/App.jsx` | — | Implementada | Nenhuma |
| FE-06.2 | Redirecionamentos de login e logout | React Router + sessão | — | Implementada | Validar com backend ativo |
| FE-06.3 | Referências a rotas inexistentes | `src/App.jsx` e menus | — | Implementada | Nenhuma |
| FE-06.4 | Rotas não encontradas | `*` → `/` | — | Implementada | Nenhuma |
| FE-07.1 | Recuperação de sessão | `/usuario/usuarioSession` | GET | Implementada | Validar com backend ativo |
| FE-07.2 | Estado de autenticação centralizado | `AuthContext` | — | Implementada | Nenhuma |
| FE-07.3 | Logout | `/usuario/usuarioLogout` | POST | Implementada | Validar cookie de sessão |
| FE-07.4 | Expiração de sessão | HTTP 401 | — | Implementada | Validar com sessão expirada real |
| FE-08.1 | Proteção visual de rotas | Guards React Router | — | Implementada | Backend permanece autoridade final |
| FE-08.2 | Separação de perfis | `nivelAcesso.descricao` | — | Implementada | Perfis possuem áreas próprias no frontend |
| FE-08.3 | Menu administrativo separado | `TopBar` | — | Implementada | Validar perfis reais |
| FE-08.4 | Acesso negado | HTTP 403 / perfil sem tela | — | Implementada | Mensagem frontend genérica |
| FE-09.1 | Cadastro alinhado ao DTO | `PacienteCadastroCompletoRequest` | POST | Implementada | Campos documentados |
| FE-09.2 | Formulários administrativos de usuário | `UsuarioRequest` | POST | Implementada parcialmente | Usuários e novo usuário disponíveis; edição não possui endpoint |
| FE-09.3 | Agendamento alinhado ao request | `AgendamentoPacienteRequest` | POST | Implementada | Contrato validado |
| FE-09.4 | Datas, horários e enums | `LocalDate`, `LocalTime`, `ClassificacaoRisco` | — | Implementada | Validar com backend ativo |
| FE-09.5 | Identidade do paciente | Sessão HTTP | — | Implementada | Backend determina o paciente |
| UBS | Listagem de UBS | `/unidadeSaude/unidadeSaudeList` | POST | Integrada | Validar com banco |
| UBS | Serviços por UBS | `/tipoServico/listarPorUnidade` | POST | Integrada | Validar dados reais |
| Agendamento | Criação | `/agendamento/agendamentoNew` | POST | Implementada | Validar regras/dados reais |
| Agendamento | Lista do paciente | `/agendamento/agendamentoPacienteList` | GET | Implementada | Validar sessão |
| Paciente | Cadastro completo | `/paciente/pacienteCadastroCompleto` | POST | Implementada | Validar com MariaDB |
| Paciente | Perfil | `/paciente/pacientePerfil` | GET | Não integrado | Avaliar após autenticação |

## Mocks eliminados

Foram removidos objetos estáticos de unidades, tipos de consulta e confirmação falsa de agendamento. O frontend oficial não usa `localStorage`, `sessionStorage` ou JSON local para simular dados.

## Escopo desta execução

Foram alterados frontend e backend dentro do escopo autorizado. O banco não foi alterado nem foram executadas operações destrutivas. A integração real ainda depende de MariaDB com dados válidos.

## Testes

Executado:

```sh
npm run lint
npm run build
```

- Frontend `npm run lint`: aprovado.
- Frontend `npm run build`: aprovado.
- Backend `mvnw -DskipTests package`: aprovado.
- Backend `mvnw test`: `UsuarioOptionalTest` aprovado; `BackendApplicationTests` falhou porque o MariaDB/variáveis de conexão não estão disponíveis.
- Não foi possível executar login, cadastro, UBS ou agendamento contra o banco real neste ambiente.

## FE-15.1

- `npm ci`: concluído usando `package-lock.json`.
- `npm run build`: aprovado.
- `npm test`: 4 testes aprovados.
- `npm ci` reportou 10 vulnerabilidades transitivas; nenhuma atualização automática foi aplicada nesta etapa.

## FE-15.2

- Fluxo de login revisado: `POST /usuario/usuarioLogin`.
- Credenciais rejeitadas permanecem na tela com erro.
- Credenciais aceitas navegam somente após confirmação do servidor.
- Teste ponta a ponta com contas reais bloqueado pela ausência de backend/MariaDB neste ambiente.

## FE-15.3

- Recuperação de sessão: `GET /usuario/usuarioSession` na inicialização.
- Encerramento: `POST /usuario/usuarioLogout`.
- Estado local e redirecionamento só ocorrem após confirmação do logout.
- Teste com cookie de sessão real permanece pendente sem backend/MariaDB disponível.

## FE-15.4

- Rotas públicas, autenticadas e administrativas revisadas.
- Guards validam sessão e perfil antes de renderizar as páginas.
- Rotas antigas possuem redirecionamentos oficiais.
- Rotas inexistentes exibem página 404.

## FE-15.5

- Formulários de autenticação, cadastro, agendamento e administração revisados.
- Entradas inválidas são bloqueadas no frontend com mensagens contextuais.
- Entradas válidas seguem para os endpoints reais e aguardam confirmação.
- Teste com dados reais depende do backend/MariaDB disponível.

## Correção administrativa

O perfil e as rotas foram corrigidos de Supervisor para Administrador. As URLs antigas `/supervisor/*` continuam redirecionando para `/administrador/*` para preservar compatibilidade. Frontend e backend foram recompilados após a correção.

## FE-15.6

- Respostas `204`, JSON, texto e corpo vazio tratadas pelo cliente HTTP.
- Erros HTTP e falhas de conexão convertidos em mensagens de interface.
- Listagens sem conteúdo exibem estados vazios, sem quebrar a tela.
- Operações de escrita exigem confirmação do backend.

## FE-15.7

- Páginas essenciais revisadas para celular e desktop.
- Menu lateral, formulários, grids e ações adaptam-se aos breakpoints.
- Tabelas usam contêiner com rolagem horizontal em telas estreitas.
- Conteúdo permanece utilizável sem criar dados fictícios.
