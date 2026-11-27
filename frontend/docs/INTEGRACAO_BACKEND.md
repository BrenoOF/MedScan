# MedScan — mapa de integração com o Spring Boot

Base da API: `http://localhost:8080/MedScan`. Em desenvolvimento, o proxy do Vite encaminha `/MedScan` para essa base.

## 1. Endpoints confirmados em execução real

Todos foram exercitados contra o backend Spring Boot e um MariaDB de teste, com cookies de sessão e token CSRF.

| Operação | Método | Endpoint | Corpo / retorno |
|---|---|---|---|
| CSRF | GET | `/csrf` | `{ "token": "..." }` |
| Login | POST | `/usuario/usuarioLogin` | `{ "email", "senha" }` → `{ "usuario", "message" }` |
| Cadastro público | POST | `/paciente/pacienteCadastroCompleto` | nome, e-mail, senha, cpf, dataNascimento, genero, logradouro, numero, bairro, cidade, uf, cep, complemento |
| Sessão | GET | `/usuario/usuarioSession` | `{ "usuario": { ... } }`; 401 sem sessão |
| Logout | POST | `/usuario/usuarioLogout` | `{}` → `{ "message" }` |
| Usuários | POST | `/usuario/usuarioList` | `{ "filtro": "listar todos" }` ou `nome`, `email`, `nivel` com `valor` |
| Novo usuário | POST | `/usuario/usuarioNew` | dados do usuário; exige `GERENCIARUSUARIO` |
| Excluir usuário | POST | `/usuario/usuarioDelete` | `{ "id" }`; exige `GERENCIARUSUARIO` |
| Níveis de acesso | POST | `/nivelAcesso/nivelAcessoList` | `{}` → `{ "niveisAcesso": [...] }`; exige `GERENCIARNIVELACESSO` |
| UBS | POST | `/unidadeSaude/unidadeSaudeList` | `{}` → `{ "unidadeSaude": [...] }` |
| Serviços por UBS | POST | `/tipoServico/listarPorUnidade` | `{ "unidadeId": 1001 }` → `{ "tipoServico": [...] }` |
| Perfil do paciente | GET | `/paciente/pacientePerfil` | `{ "paciente": { ... } }` |
| Criar agendamento | POST | `/agendamento/agendamentoNew` | `{ unidadeCnes, tipoServicoId, diaAgendamento, horaAgendamento, escalaDor, sintomas, descricaoBuscaAtiva, classificacaoRisco }` |
| Agendamentos do paciente | GET | `/agendamento/agendamentoPacienteList` | `{ "agendamentos": [...] }` |
| Agendamentos da UBS | POST | `/agendamento/porUnidade` | corpo ignorado; exige `VISUALIZARAGENDAMENTO` |
| Alterar status | POST | `/agendamento/alterarStatus` | `{ "id": 7, "status": "CONFIRMADO" }`; exige `MUDARSTATUS` |

As permissões vêm do vínculo entre nível de acesso e categorias de configuração no banco.

## 2. Identidade e autorização

- A identidade do paciente, do atendente e da UBS é obtida da sessão autenticada no backend.
- `/agendamento/porUnidade` e `/agendamento/alterarStatus` ignoram IDs enviados pelo navegador para autorização.
- A alteração de status só é aceita se a UBS do agendamento for a mesma do atendente autenticado.
- Transições aceitas: `PENDENTE → CONFIRMADO`, `PENDENTE → CANCELADO`, `CONFIRMADO → CANCELADO`.

### Vocabulário de status

| Enum interno | Valor na API |
|---|---|
| `PENDENTE` | `PENDENTE` |
| `AGENDADO` | `CONFIRMADO` |
| `REJEITADO` | `CANCELADO` |

`classificacaoRisco` é independente e continua em `NAO_URGENTE`, `POUCO_URGENTE`, `URGENTE`, `MUITO_URGENTE` e `EMERGENCIA`.

## 3. Sessão e CSRF

- Autenticação por sessão HTTP, sem JWT.
- O frontend envia `credentials: "include"` em todas as chamadas.
- O cookie `XSRF-TOKEN` é emitido com `Path=/MedScan`, portanto não é visível em `document.cookie` nas páginas do frontend. Por isso o cliente busca o token no JSON de `GET /csrf` e o envia em `X-XSRF-TOKEN`.
- Erros não expõem SQL, nomes de constraints nem detalhes do Hibernate: conflitos retornam 409 com mensagem genérica.

## 4. Contratos ainda necessários

Estes fluxos dependem de endpoints que o backend ainda não expõe:

- Cancelamento de agendamento pelo paciente.
- Edição de usuários.
- Cadastro e edição de UBS e serviços.
- Listagem de funções para criar níveis de acesso sem informar IDs manualmente.

## 5. Checklist de validação

Verificado em execução real:

1. Compilação e build do frontend e do backend.
2. Cadastro público e login com credenciais válidas (200).
3. Login inválido e acesso sem sessão (401).
4. Recuperação de sessão e logout; sessão após logout (401).
5. UBS e serviços retornando registros reais.
6. Cadastro completo de paciente com endereço e vínculo de perfil.
7. Criação de agendamento, bloqueio de duplicidade e listagem do paciente.
8. Atendente listando apenas a própria UBS e alterando status permitido.
9. Acesso negado a paciente e a atendente de outra unidade (403).
10. Níveis de acesso restritos à administração (403 para paciente).
11. Erro de duplicidade sem vazamento de SQL.

Pendente de validação visual: responsividade em dispositivos, navegação por teclado e contraste.
