# MedScan — Frontend reformulado (React + Vite)

Projeto reconstruído a partir das duas versões recebidas (`projeto apenas front.zip` e `tentativa.zip`) e do diagnóstico técnico datado de **10/11/2026**. Esta versão mantém a identidade visual MedScan, reúne as telas em uma aplicação coerente, padroniza a API HTTP e elimina dados falsos e operações que fingiam ter sido persistidas.

> **Importante:** este pacote inclui **somente o frontend**. Não modifica nem substitui o backend Spring Boot. A auditoria constatou que o backend atual tem erros de compilação e APIs essenciais ainda indisponíveis. Portanto, **não é correto afirmar que todo o sistema está funcional de ponta a ponta**.

## 1. Requisitos

- Node.js **20.19+ ou 22.12+** (requisito da versão do Vite instalada pelo projeto).
- npm.
- Backend Spring Boot em `http://localhost:8080` com contexto `/MedScan`.
- MariaDB e configurações de acesso disponibilizadas pelo backend.

## 2. Como executar

Abra um terminal dentro desta pasta:

```bash
npm ci
npm run dev
```

Acesse **http://localhost:5173**.

Para gerar a versão de produção:

```bash
npm run build
npm run preview
```

Para testar os utilitários do projeto:

```bash
npm test
```

O proxy do `vite.config.js` encaminha **`/MedScan` → `http://localhost:8080/MedScan`**. O `VITE_API_BASE` usa `/MedScan` por padrão. Todas as requisições enviam cookies com `credentials: "include"`. Se o backend utilizar outra porta, defina `MEDSCAN_BACKEND_URL` no ambiente antes de iniciar o Vite.

### Variáveis de ambiente

Copie `.env.example` para `.env` **apenas quando necessário**. **Não configure URLs de agendamento que não existem** — elas foram deixadas comentadas e são propostas para integração futura, não rotas confirmadas.

Para ambientes de produção, configure reverse proxy para o mesmo host ou forneça a base apropriada (`VITE_API_BASE`). **Nunca coloque senhas do banco nem segredos no `.env` do frontend**.

## 3. Estrutura

```
src/
├── api/                   # fetch, erros HTTP, usuários, unidades, pacientes e agendamentos
├── components/            # Layout, proteção de rotas, avisos, carregamento
├── context/               # Sessão HTTP e perfis
├── pages/                 # Públicas, paciente, atendente e supervisor
├── utils/                 # Normalização de retornos e rótulos
├── App.jsx                # Rotas únicas + redirecionamentos de rotas antigas
├── main.jsx               # Entrada da aplicação
└── styles.css             # Identidade visual e layouts responsivos
```

## 4. Funcionalidades disponíveis e limitações

| Área | Situação no frontend | Integração real |
|---|---|---|
| Home pública e Sobre | Implementadas | Não depende do banco |
| Login | Formulário e autenticação via cookie | `POST /usuario/usuarioLogin` — depende da correção da compilação e sessão no backend |
| Cadastro de usuário | Formulário validado, sem sucesso simulado | `POST /usuario/usuarioCadastro` |
| Logout e restauração de sessão | Implementados | `POST /usuario/usuarioLogout` e `GET /usuario/usuarioSession` |
| Proteção de rotas | Implementada para os três perfis | Backend precisa reforçar autorização real |
| UBS | Lista e busca visual, com atualizações | `POST /unidadeSaude/unidadeSaudeList` |
| Serviços por UBS | Consulta ao selecionar uma UBS | `POST /tipoServico/listarPorUnidade` |
| Solicitação de agendamento | Interface em duas etapas, validações e revisão | **Bloqueada até existir AgendamentoController** |
| Meus agendamentos e cancelamento | Interface pronta para dados reais | **Endpoints não disponíveis** |
| Perfil do paciente | Consulta e formulário preparado | `GET /paciente/pacientePerfil`; cadastro completo **aguarda API de endereço/titularidade** |
| Painel do atendente | Painel, filtro e tabela preparados | **Endpoint da própria UBS indisponível** |
| Supervisor — usuários | Consulta com filtro e criação administrativa | `POST /usuario/usuarioList` e `/usuario/usuarioNew` |
| Supervisor — níveis | Consulta sem mocks | `POST /nivelAcesso/nivelAcessoList` (formato exato ainda deve ser confirmado) |
| Supervisor — UBS | Consulta real | Criação e edição indisponíveis |
| Edição e exclusão administrativa | **Não simuladas** | Edição sem controller; exclusões aguardam correção das cascatas |
| Chat, doenças e notificações | Não incluídos no escopo mínimo | Não havia backend comprovado para os fluxos principais |

## 5. Segurança e integridade

- **Não existe login falso**, lista de dados fictícios nem sucesso de agendamento offline.
- A sessão não é gravada em `localStorage`; a fonte da autenticação é o servidor.
- **Não** envie `pacienteId`, `atendenteId`, `usuarioId` ou `status` diretamente do formulário como prova de titularidade. O backend deve obter essas informações da sessão e aplicar as regras de negócio.
- A navegação é limitada por perfil, mas **a segurança efetiva deve ser garantida no Spring Security**.
- O cliente lê o cookie `XSRF-TOKEN`, caso exista, e envia `X-XSRF-TOKEN`. O backend ainda precisa fornecer e validar o token ao habilitar CSRF; essa mudança exige teste de integração.
- Para TCC, use dados fictícios durante a demonstração.

## 6. Contratos ainda necessários

Consulte **`INTEGRACAO_BACKEND.md`**, que diferencia os endpoints verificados dos endpoints sugeridos e documenta exemplos de requests/responses para a próxima etapa.

## 7. Verificação da entrega

- Os **4 testes automatizados dos utilitários** foram executados com sucesso.
- Todos os arquivos JS e JSX foram analisados por um parser JavaScript/JSX sem erros de sintaxe.
- **Build Vite não verificado neste ambiente**: a instalação npm excedeu o tempo disponível por falta de acesso aos pacotes. Execute `npm ci && npm run build` na máquina de desenvolvimento para validar a compilação final. Não foi realizado teste no navegador com o backend.

## 8. Observações sobre a auditoria

A F1 foi preservada como **referência visual e de identidade**; a F2 serviu como referência inicial do uso de `fetch`. Os dois ZIPs originais permanecem inalterados. A nova versão é um projeto autônomo e deve ser trabalhada como **único frontend oficial** após revisão da equipe.
