# MedScan — Frontend (React + Vite)

Frontend oficial do MedScan: autenticação por sessão do Spring Boot, Fetch API com cookies e CSRF, perfis de paciente, atendente e administrador. **Não contém mocks, `localStorage` nem respostas simuladas.**

O projeto é independente do backend: para executá-lo é necessário ter o Spring Boot ativo em `http://localhost:8080` com o context path `/MedScan`.

## 1. Requisitos

- Node.js 20.19+ ou 22.12+
- npm
- Backend Spring Boot com `/MedScan` e um banco de dados configurado

## 2. Executar

```bash
npm ci
npm run dev
```

Acesse **http://localhost:5173**.

Outros comandos:

```bash
npm run build     # gera dist/
npm run preview   # serve a versão de produção
npm test          # testes dos utilitários
```

O proxy do Vite encaminha `/MedScan` para `http://localhost:8080`. Se o backend estiver em outro endereço, defina `MEDSCAN_BACKEND_URL` antes de iniciar o Vite:

```bash
MEDSCAN_BACKEND_URL=http://localhost:8080 npm run dev
```

### Variáveis de ambiente

O arquivo `.env` **não é necessário**: a base `/MedScan` é o padrão e os caminhos confirmados estão no código. Use `.env.example` apenas se precisar sobrescrever a base ou habilitar uma funcionalidade cuja rota ainda não existe no backend. Nunca coloque senhas ou segredos no frontend.

### Contas de teste locais

Usadas apenas no banco de desenvolvimento:

| Perfil | E-mail | Senha |
|---|---|---|
| Administrador | `admin@medscan.test` | `Teste@123456` |
| Atendente | `atendente@medscan.test` | `Teste@123456` |
| Paciente | `paciente@medscan.test` | `Teste@123456` |

## 3. Estrutura

```text
public/                 imagens (a.png é a logo oficial) e assets estáticos
src/
  api/                  http.js (fetch, cookies, CSRF, erros) + módulos por domínio
  components/           Layout, guards, feedback e ErrorBoundary
  context/              AuthContext (sessão e perfil vindos do backend)
  pages/                páginas públicas, paciente, atendente e administrador
  utils/                normalização de respostas e rótulos
  App.jsx               rotas oficiais e redirecionamentos legados
  main.jsx              ponto de entrada
  styles.css            identidade visual e responsividade
tests/                  testes dos utilitários
docs/                   auditorias e decisões por requisito do backlog
```

## 4. Integração com o backend

| Fluxo | Método | Endpoint |
|---|---|---|
| Login | POST | `/usuario/usuarioLogin` |
| Cadastro do paciente | POST | `/paciente/pacienteCadastroCompleto` |
| Sessão | GET | `/usuario/usuarioSession` |
| Logout | POST | `/usuario/usuarioLogout` |
| Usuários | POST | `/usuario/usuarioList`, `/usuario/usuarioNew`, `/usuario/usuarioDelete` |
| Níveis de acesso | POST | `/nivelAcesso/nivelAcessoList` |
| UBS e serviços | POST | `/unidadeSaude/unidadeSaudeList`, `/tipoServico/listarPorUnidade` |
| Perfil do paciente | GET | `/paciente/pacientePerfil` |
| Agendamento do paciente | POST/GET | `/agendamento/agendamentoNew`, `/agendamento/agendamentoPacienteList` |
| Painel do atendente | POST | `/agendamento/porUnidade`, `/agendamento/alterarStatus` |
| CSRF | GET | `/csrf` |

O cliente envia `credentials: "include"`, lê o token em `/csrf` quando o cookie não é acessível via `document.cookie` e reenvia `X-XSRF-TOKEN`. A identidade de paciente, atendente e UBS vem sempre da sessão do servidor.

O status exposto pela API usa o vocabulário do frontend: `PENDENTE`, `CONFIRMADO` e `CANCELADO`.

### Limitações conhecidas

- Cancelamento de agendamento pelo paciente depende de endpoint ainda não exposto.
- Edição de usuários e cadastro/edição de UBS dependem de endpoints ainda não expostos.
- A listagem de níveis exige permissão administrativa no backend.

## 5. Layout

O menu lateral é exibido por padrão em desktop. O botão de menu na barra superior recolhe e reabre a sidebar em desktop; em telas até 980 px ele abre e fecha o menu sobreposto.

## 6. Verificação

Executados neste projeto:

- `npm ci` / dependências resolvidas
- `npm run build` — aprovado
- `npm test` — 4 testes aprovados
- Fluxos HTTP contra o backend local e MariaDB de teste — 27 verificações aprovadas
- Testes automatizados do backend — 26 aprovados com datasource configurado

Não há script `lint` configurado.

## 7. Documentação

- `INTEGRACAO_BACKEND.md` — contratos confirmados endpoints a implementar
- `RELATORIO_INTEGRACAO.md`, `FLUXO_USUARIO.md`, `MELHORIAS_REALIZADAS.md`
- `docs/productBacklog.txt` — backlog completo
- `docs/DECISAO-FE-01.1.md` a `docs/URLS-CENTRALIZADAS-FE-02.3.md` — decisões e evidências por requisito

## 8. Observações para publicação

A pasta contém apenas o frontend. `node_modules`, `dist` e `.env` são ignorados pelo Git. Nenhuma credencial de banco ou segredo está incluído.
