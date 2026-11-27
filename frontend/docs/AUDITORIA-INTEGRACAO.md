# Auditoria do fluxo e da integração

## Base oficial consolidada

`MedScan-main/frontend` é a única base oficial ativa. Ela reúne os fluxos de paciente, atendente e administrador. A antiga pasta `apenas frontend` foi preservada apenas como histórico de migração.

## FE-09.3 — contrato do agendamento

O formulário envia para `/agendamento/agendamentoNew` os campos compatíveis com o contrato real: `unidadeCnes`, `tipoServicoId`, `diaAgendamento`, `horaAgendamento`, `escalaDor`, `sintomas`, `descricaoBuscaAtiva` e `classificacaoRisco`. O paciente não informa `pacienteId`; o backend resolve a identidade pela sessão.

## FE-09.4 — representação dos dados

CNES e tipo de serviço são convertidos para identificadores numéricos no envio. Datas usam `yyyy-MM-dd`, horários usam `HH:mm`, a escala de dor é enviada como número e `classificacaoRisco` usa os valores de enum aceitos pelo backend.

## FE-09.5 — identidade autenticada

O formulário não envia `pacienteId`, `usuarioId` ou outro identificador de titularidade. A requisição usa a sessão HTTP (`credentials: "include"`) e o backend resolve e valida o paciente autenticado.

## FE-10.1 — fontes de dados

As páginas oficiais usam os módulos `src/api/` para consultar o backend. Não há arquivos JSON de dados, `localStorage`, `sessionStorage` ou mocks em `src/` ou `public/`; o único `fetch` direto é usado pelo cliente HTTP para obter o token CSRF e executar as requisições da API.

## FE-10.2 — UBS

`Unidades.jsx` usa `POST /unidadeSaude/unidadeSaudeList`, preserva CNES, nome e município/endereço retornados e exibe estados de carregamento, erro e lista vazia. Não há campos fictícios de Natureza ou Status.

## FE-10.3 — serviços por UBS

Ao expandir uma unidade, `Unidades.jsx` chama `POST /tipoServico/listarPorUnidade` usando o CNES selecionado. Os serviços ficam associados àquela chave CNES e são limpos quando outra UBS é selecionada; o agendamento também recarrega os serviços ao trocar a unidade.

## FE-11.1 — dependências ausentes

Não há referências a `HealthService`, `healthService` ou `buscarProximos` no frontend oficial. A Home e os painéis não chamam funções inexistentes.

## FE-11.2 — imports e exports

A revisão de imports da base oficial não encontrou referências a módulos ausentes. O build do Vite confirma que os exports usados por `App`, páginas, guards, contexto e módulos de API são resolvidos corretamente.

## FE-11.3 — dependências indisponíveis

Os componentes que dependem de endpoints ainda não disponíveis usam `PendingFeature` ou mensagens de indisponibilidade. Após a consolidação, criação e consulta de agendamentos usam os endpoints reais; ações sem contrato, como cancelamento, edição de usuário e gestão de UBS, continuam bloqueadas sem simulação.

## FE-11.4 — validação da base

O build de produção e a suíte de testes da base oficial foram executados após a consolidação, sem erros de compilação. A validação de navegador contra o MariaDB permanece dependente da infraestrutura local.

## FE-12.1 — carregamento

Login, cadastro, sessão, UBS, serviços, agendamentos, usuários, níveis, perfil e operações do atendente mantêm estados de carregamento e desabilitam ações durante requisições assíncronas.

## FE-12.2 — mensagens de erro

`apiRequest` traduz erros de conexão e respostas HTTP 400, 401, 403, 404, 409 e 500 para mensagens compreensíveis. As páginas exibem essas mensagens em componentes de alerta, sem converter falhas em sucesso.

## FE-12.3 — confirmações reais

Login, cadastro de paciente, cadastro administrativo e agendamento só avançam ou exibem confirmação após respostas válidas contendo os dados esperados pelo contrato. Respostas vazias ou incompatíveis geram erro.

## FE-12.4 — estados vazios

UBS, serviços, agendamentos, usuários, níveis e solicitações do atendente exibem componentes de estado vazio quando a API retorna uma coleção sem registros. Nenhuma dessas telas cria linhas ou dados locais para preencher a ausência.

## FE-13.1 — campos obrigatórios

Login, cadastro de paciente, agendamento e formulários administrativos usam `required` e validações antes do envio. Campos compostos do cadastro e etapas do agendamento também são verificados explicitamente antes de avançar.

## FE-13.2 — formatos

O cadastro de paciente valida e-mail, CPF com 11 dígitos e data de nascimento não futura. Login e formulários administrativos usam validação nativa de e-mail, enquanto o agendamento limita a data escolhida ao dia atual ou posterior.

## FE-13.3 — mensagens de validação

As mensagens de validação aparecem dentro do formulário, junto ao grupo de campos da etapa atual. Ao voltar no cadastro, a mensagem anterior é limpa para não permanecer associada à etapa errada.

## FE-13.4 — envios duplicados

Login, cadastro, agendamento, consultas administrativas e operações do atendente controlam `loading`, `submitting` ou `busy` e desabilitam seus botões durante a requisição.

## FE-13.5 — compatibilidade com o backend

As validações usam os mesmos nomes e formatos dos DTOs integrados: `LocalDate`, `LocalTime`, identificadores de UBS/serviço, escala numérica e valores de classificação de risco. Identidade e vínculos do paciente permanecem determinados pelo backend.

## FE-14.1 — duplicações

Os elementos recorrentes de feedback (`Alert`, `Loading`, `EmptyState`, `PageHeading` e `StatusBadge`) estão centralizados em `components/Feedback.jsx`. A navegação autenticada também está centralizada em `components/Layout.jsx`; não foram criadas implementações paralelas durante a consolidação.

## FE-14.2 — padronização visual

As páginas autenticadas usam `AppLayout`, `PageHeading`, `card`, `btn`, `field`, `form-grid` e componentes de feedback compartilhados. Menus, cabeçalhos, formulários e listagens seguem a mesma folha visual em `src/styles.css`.

## FE-14.3 — responsividade

`src/styles.css` possui breakpoints para 1180px, 980px, 720px e 380px. Eles adaptam o menu lateral, cabeçalho público, hero, grids, formulários, filtros, cartões e perfil. Tabelas usam `overflow:auto` para preservar a leitura em telas estreitas, e há suporte a `prefers-reduced-motion`.

## FE-14.4 — acessibilidade básica

Os formulários usam `label` associado por aninhamento, campos nativos e mensagens próximas ao formulário. Navegação e controles têm nomes acessíveis (`aria-label`), foco visível por teclado, `role="status"`/`role="alert"` para feedback e textos alternativos nas imagens. Ícones decorativos foram marcados como ocultos para leitores de tela.

## FE-15.1 — instalação, inicialização e build

As dependências foram reinstaladas com `npm ci`, usando o `package-lock.json`. O `npm run build` e o `npm test` foram executados com sucesso após a instalação. O `package.json` disponibiliza os comandos `dev`, `build`, `preview` e `test`; a execução contra backend e MariaDB continua dependente da infraestrutura local.

O `npm ci` reportou 10 vulnerabilidades transitivas (1 baixa e 9 altas). Nenhuma correção automática foi aplicada para evitar alterações de dependências fora do escopo deste requisito.

## FE-15.2 — login válido e inválido

O fluxo de login envia `POST /usuario/usuarioLogin` pela sessão HTTP real, mantém o usuário na tela com a mensagem retornada/traduzida quando a API rejeita as credenciais e só navega após `login` concluir com sucesso. O botão fica desabilitado durante a requisição. A validação com credenciais reais permanece pendente porque o ambiente não possui backend/MariaDB e contas de teste disponíveis; nenhum resultado foi simulado.

## FE-15.3 — recuperação e encerramento de sessão

`AuthProvider` consulta `GET /usuario/usuarioSession` ao inicializar e após login, permitindo recuperar a sessão baseada no cookie HTTP quando a página é atualizada. O logout chama `POST /usuario/usuarioLogout`; somente após a confirmação da API o estado local é limpo e a navegação retorna ao login. Falhas de logout permanecem visíveis e não são tratadas como sucesso. A validação ponta a ponta depende do backend ativo.

## FE-15.4 — navegação e permissões

As rotas públicas, autenticadas e administrativas estão registradas em `App.jsx`. `RequireAuth` redireciona visitantes para login, impede acesso a áreas de outro perfil e preserva a origem solicitada; `GuestOnly` evita reabrir login/cadastro para usuários autenticados. Rotas antigas redirecionam para destinos oficiais e caminhos desconhecidos exibem página 404.

## FE-15.5 — formulários válidos e inválidos

Os formulários oficiais foram revisados para entradas válidas e inválidas: login usa validação nativa de e-mail e senha obrigatória; cadastro completo valida e-mail, CPF, data, endereço e confirmação de senha; agendamento valida UBS, serviço, queixa, data e horário; formulários administrativos validam filtro, e-mail, senha e nível. Erros permanecem no formulário e envios válidos só avançam após confirmação da API. A execução com dados persistidos reais depende do backend/MariaDB.

## Correção de nomenclatura administrativa

O perfil administrativo foi padronizado como `administrador` em vez de `supervisor`, alinhando o frontend às permissões do backend (`GERENCIARUSUARIO`, `GERENCIARNIVELACESSO`, `GERENCIARFUNCAO` e demais autoridades administrativas). As rotas oficiais agora usam `/administrador`; as rotas antigas `/supervisor/*` permanecem apenas como redirecionamentos de compatibilidade. A verificação adicional não encontrou referências ativas a Supervisor no backend ou nas áreas funcionais do frontend.

## FE-15.6 — respostas HTTP

O cliente HTTP trata respostas `2xx` com JSON, texto ou corpo vazio, retorna `null` para `204`, converte erros HTTP em `ApiError` com status e mensagem, e traduz falhas de conexão. Consultas de coleção usam `listFrom`, que transforma respostas vazias ou nulas em listas vazias; operações de cadastro/agendamento exigem a confirmação esperada antes de informar sucesso.

## FE-15.7 — páginas essenciais

As páginas públicas, login, cadastro, painel do paciente, UBS, agendamento, acompanhamento, perfil, atendimento e administração usam os breakpoints responsivos já definidos em `src/styles.css`. Em celular, o menu lateral vira menu recolhível, grids passam a uma ou duas colunas, cabeçalhos e ações ocupam a largura disponível e tabelas permanecem legíveis com rolagem horizontal. Em desktop, os grids e painéis recuperam suas colunas sem alterar os contratos da API.

## FE-10.4 — listagens administrativas

As telas de administrador consultam dados reais por `usuarioAdminService`/`usuariosApi`, `niveisApi` e `unidadesApi`. O filtro de usuários foi normalizado para enviar `listar todos`, `nome`, `email` ou `nivel`, que são os valores aceitos pelo backend; o filtro inexistente por código foi removido.

## Estado atual do fluxo

1. `/` e `/home` exibem a Home e carregam unidades de saúde pelo backend.
2. A Home oferece login, cadastro e início do agendamento.
3. `/login` autentica por sessão HTTP e redireciona para `/`.
4. `/cadastro` coleta usuário, dados do paciente e endereço em três etapas.
5. `/agendamento` carrega UBS e serviços reais, coleta os dados da triagem e envia a criação ao backend.
6. O menu lateral permite voltar à Home, acessar login, cadastro, agendamento e sair.
7. `/ubs` e `/sobre` não são mais oferecidas porque não existem páginas implementadas.

## Contratos usados

| Endpoint | Método | Uso |
|---|---|---|
| `/usuario/usuarioLogin` | POST | Login |
| `/usuario/usuarioLogout` | POST | Logout |
| `/paciente/pacienteCadastroCompleto` | POST | Cadastro transacional de paciente |
| `/unidadeSaude/unidadeSaudeList` | POST | Lista de UBS |
| `/tipoServico/listarPorUnidade` | POST | Serviços da UBS selecionada |
| `/agendamento/agendamentoNew` | POST | Criação do agendamento |
| `/agendamento/agendamentoPacienteList` | GET | Lista do paciente autenticado |
| `/csrf` | GET | Obtenção do token CSRF |

Todas as chamadas passam pelo cliente HTTP centralizado, usam JSON e `credentials: "include"`.

## Ajustes visuais e de navegação realizados

- Corrigido o alias `/home` e o logo agora retorna para `/`.
- Removidos links do menu para rotas inexistentes.
- Corrigido o posicionamento do menu lateral para `fixed`.
- Corrigido o estilo do botão de logout.
- Separado o card de formulário do agendamento para evitar conflito com os cards de login e cadastro.
- Cadastro recebeu estilos próprios para labels, selects e botões das etapas.
- Home recebeu estados visuais de carregamento, erro e lista vazia.
- Home e agendamento passaram a usar nomenclatura de unidades de saúde, sem referências funcionais a hospitais.
- Melhorada a adaptação dos botões da Home em telas pequenas.

## Pendências de validação

- O frontend ainda não foi validado contra MariaDB neste ambiente.
- Login, cadastro persistido, sessão, UBS, serviços e agendamento precisam de teste ponta a ponta com backend e banco ativos.
- A tela de lista de agendamentos já possui contrato no backend, mas ainda não há uma página consumidora no frontend.
- As rotas de agendamento e administração estão protegidas no frontend; o backend continua sendo a autoridade final da sessão e das permissões.
- Respostas HTTP 401 limpam a sessão local e fazem as rotas protegidas retornarem ao login.
- FE-05.1: o login só chama `iniciarSessao` depois de uma resposta válida; erros HTTP, falhas de comunicação e respostas incompatíveis permanecem na tela de login.
- FE-05.2: o cadastro só redireciona para o login após receber um paciente válido; respostas vazias ou incompatíveis permanecem no cadastro.
- FE-05.4: o agendamento só exibe sucesso e retorna à Home após receber um agendamento válido com identificador.
- O perfil retornado em `usuario.nivelAcesso.descricao` controla os menus e as rotas de paciente/administrador.
- FE-06: as rotas oficiais possuem comportamento definido; `/home` é normalizada para `/`, rotas desconhecidas retornam à Home e rotas protegidas redirecionam para o login.
- FE-06.1 concluído: todas as páginas oficiais possuem uma rota registrada em `App.jsx`, com rotas aninhadas para os guards de autenticação.
- FE-06.2 concluído: login encaminha o usuário ao perfil correto ou à rota protegida originalmente solicitada; logout encerra a sessão e leva ao login.
- FE-06.3 concluído: `/home` é apenas um alias compatível, `/admin` é uma rota existente e não há links funcionais para `/welcome`, `/ubs` ou `/sobre`.
- FE-06.4 concluído: o caminho curinga do React Router redireciona URLs inexistentes para `/`, evitando páginas sem saída.
- FE-07.1 concluído: `AuthProvider` consulta `/usuario/usuarioSession` ao iniciar e aguarda a resposta antes de liberar guards e menus.
- FE-07.2 concluído: `AuthContext` expõe `usuario`, `perfil`, `isAuthenticated`, `isLoading`, `iniciarSessao` e `encerrarSessao` através do hook `useAuth`.
- FE-07.3 concluído: o menu autenticado chama `POST /usuario/usuarioLogout`, limpa o `AuthContext` mesmo após a resposta do servidor e redireciona para `/login`.
- FE-07.4 concluído: `apiJson` dispara o evento de sessão expirada em respostas HTTP 401; o `AuthContext` limpa o usuário e os guards impedem a permanência em rotas protegidas.
- FE-08.1 concluído: `/agendamento` e `/admin` passam pelo `ProtectedRoute`; usuários sem sessão são redirecionados para `/login` com a rota original preservada.
- FE-08.2 concluído parcialmente: `AuthContext` identifica paciente, administrador, atendente e administrador; cada perfil recebe somente as opções disponíveis nesta versão. Atendente e administrador ainda não possuem páginas frontend próprias.
- FE-08.3 concluído: a opção `Administração` é renderizada somente para o perfil `ADMIN`; pacientes não recebem links administrativos.
- FE-08.4 concluído: HTTP 403 é convertido pelo cliente HTTP em mensagem de permissão; as telas mantêm o usuário no fluxo atual e não simulam a operação.
- FE-09.1 concluído: o payload do cadastro contém exatamente os campos de `PacienteCadastroCompletoRequest`, com data ISO e UF normalizada, sem identificadores definidos pelo cliente.
- FE-09.2 verificado: não há formulários administrativos de usuário no frontend oficial; o único serviço administrativo existente não é exposto por uma tela. A edição permanece indisponível porque não existe endpoint correspondente.
- FE-07: `AuthContext` recupera `/usuario/usuarioSession`, disponibiliza o usuário aos componentes, executa logout no backend e limpa o estado quando recebe HTTP 401.
- FE-08: guards impedem acesso anônimo e separam paciente/administrador; perfis atendente e administrador são identificados, mas permanecem pendentes por falta de páginas frontend correspondentes.
- FE-09.1: o cadastro envia os campos do `PacienteCadastroCompletoRequest` sem `usuarioId` ou `nivelAcessoId`.
- FE-09.3/09.4: o agendamento envia CNES, serviço, data ISO (`yyyy-MM-dd`), hora (`HH:mm`), escala de dor, sintomas, queixa e enum de risco.
- FE-09.5: o frontend não envia `pacienteId`; o backend identifica o paciente pela sessão autenticada.
- FE-09.2 permanece pendente: o serviço administrativo conhece os filtros reais de `/usuario/usuarioList`, mas não há formulário/tela administrativa oficial para criação ou edição; não foi criada uma tela fictícia.
- Valores de `SameSite`, `Secure`, CORS e variáveis do banco precisam ser revisados para produção.

## Mocks e persistência local

FE-05.3 está atendido: não há uso de `localStorage`, `sessionStorage`, Axios ou dados mockados no frontend oficial. O estado preenchido no cadastro e no agendamento permanece apenas em memória até o envio ao backend.

## Verificações executadas

- `npm run lint`: aprovado.
- `npm run build`: aprovado.
- `mvnw -DskipTests package`: aprovado anteriormente.
- Testes de integração com MariaDB: pendentes por ausência do banco e das credenciais neste ambiente.



