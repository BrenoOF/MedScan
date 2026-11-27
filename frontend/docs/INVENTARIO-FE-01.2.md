# FE-01.2 — Inventário de reaproveitamento da versão arquivada

**Data:** 26/11/2026  
**Fonte analisada:** `../versoes-arquivadas/React-Javascript/`  
**Projeto de destino:** `apenas frontend/` (oficial, conforme FE-01.1)

## Regra adotada (sem duplicação)

- Todo arquivo que **já existe na versão oficial** tem a implementação oficial como **canônica**. A cópia arquivada não será trazida para o projeto principal.
- Arquivos exclusivos da versão arquivada são apenas **identificados** neste documento. A cópia física para o projeto oficial só ocorrerá quando a funcionalidade for realmente implementada, com:
  - endpoints passando pelos serviços oficiais (`src/services/`);
  - dados mockados (`localStorage` e JSON de `public/`) substituídos por API;
  - DTOs validados conforme FE-09;
  - rotas registradas no `App.jsx` oficial (FE-06).
- Nenhum arquivo foi copiado ou excluído nesta etapa: a versão arquivada continua intacta como referência.

---

## 1. Implementações já canônicas na versão oficial — **não reaproveitar cópia antiga**

| Arquivo/área | Situação | Decisão |
| --- | --- | --- |
| `src/components/banner/Banner.jsx` | Versão oficial é a mais recente; `Banner.css` é idêntico | Manter oficial |
| `src/components/menubar/Menubar.jsx` + `.css` | Arquivos idênticos nas duas versões | Manter oficial (`flags.css`, não referenciado, foi arquivado no FE-01.3) |
| `src/components/topbar/TopBar.jsx` | Oficial é mais enxuta; `TopBar.css` idêntico | Manter oficial; consultar antiga só se precisar de funcionalidade removida |
| `src/pages/home/Home.jsx` + `Home.css` | Oficial substitui chamada quebrada a `HealthService` por dados locais explícitos | Manter oficial; a carousel da antiga é inspiração opcional (FE-14) |
| `src/pages/login/Login.jsx` | Oficial já usa `usuarioService.login` e não simula sucesso | Manter oficial |
| `src/pages/cadastro/Cadastro.jsx` | Oficial já usa `usuarioService` | Manter oficial |
| `src/pages/agendamento/Agendamento.jsx` | Oficial é a versão atual do fluxo | Manter oficial |
| `src/App.jsx`, `src/index.jsx`, `vite.config.js`, `package.json` | Configuração oficial | Manter oficial |
| `src/services/healthService.js` (arquivado) | Arquivo totalmente comentado; não exporta nada | **Descartar** como código; aproveitar apenas o esboço de DTO como ideia |
| `src/services/usuarioService.js` | Existe somente na oficial | Manter como serviço único de usuários |

---

## 2. Componentes e páginas exclusivas da versão arquivada — **candidatos a reaproveitamento**

Todos os caminhos abaixo são relativos a `../versoes-arquivadas/React-Javascript/`.

| Item | Arquivos | Valor reaproveitável | Como adaptar na oficial | Backlog relacionado |
| --- | --- | --- | --- | --- |
| **Welcome / landing** | `src/components/welcome/Welcome.jsx`, `Welcome.css` | Tela simples de entrada com botões Login/Cadastro; resolve o destino `/welcome` hoje inexistente | Criar `src/pages/welcome/` na oficial e registrar rota `/welcome`; trocar `<Image>`/`Button` do PrimeReact pelo padrão visual oficial se necessário | FE-06.3, FE-14.2 |
| **Página Sobre** | `src/pages/sobre/Sobre.jsx`, `Sobre.css` | Conteúdo institucional completo (missão, valores, badges ODS, CTA) | Criar `src/pages/sobre/`; revisar textos que citam CNES como dado real; redirecionar CTA para rota existente | FE-06.3, FE-14 |
| **Listagem de UBS** | `src/pages/ubs/Ubs.jsx`, `Ubs.css` | Layout `DataView` com paginação, estado vazio e navegação para agendamento com `state.ubs` | Substituir `fetch('/unidades.json')` por serviço HTTP oficial; manter padrão de card/paginação e o repasse da UBS selecionada | FE-10.2, FE-12.4, FE-14.2 |
| **Hub administrativo** | `src/pages/admin/Admin.jsx`, `Admin.css` | Estrutura de menus para Usuário e Nível de Acesso | Recriar somente após existirem rotas e permissões reais; usar controle de acesso em vez de apenas hover | FE-08.2, FE-08.3 |
| **CRUD de usuários (lista/novo/editar)** | `src/pages/usuarios/UsuarioList.jsx`, `UsuarioNew.jsx`, `UsuarioEdit.jsx` e CSSs | Padrões de `DataTable`, filtros, `Dialog` de confirmação, `InputMask` de telefone e `Dropdown` de nível | Substituir JSON + `localStorage` por `usuarioService.listar/criar/editar/deletar`; ajustar filtros ao `POST /usuario/usuarioList`; remover edição inexistente | FE-04.3, FE-04.4, FE-09.2, FE-10.4 |
| **CRUD de níveis de acesso** | `src/pages/nivelAcesso/NivelAcessoList.jsx`, `NivelAccessoNew.jsx`, `NivelAcessoEdit.jsx` e CSSs | Estrutura de consulta com filtro por código/descrição e confirmação de exclusão | Só portar quando houver endpoints e permissões; remover `localStorage` e rotas com capitalização inconsistente | FE-08.2, FE-10.4 |
| **Listagem de doenças** | `src/pages/doencas/DoencaList.jsx`, `DoencaList.css` | Filtro por ID/CID/nome e `DataTable` paginado | Só portar quando o backend expuser o recurso; remover exclusão simulada em `localStorage` | FE-10.4, FE-12.4 |
| **Busca de unidades** | `search/Search.jsx`, `SearchBar.jsx`, `SearchResult.jsx`, `SearchResultsList.jsx` e CSSs | Componente de busca com sugestões, estado vazio e foco controlado | Mover para `src/components/search/`; trocar `unidades.json` pelo serviço de UBS; integrar ao fluxo de agendamento | FE-10.2, FE-10.3, FE-12.4 |
| **Agendamento com PrimeReact** | `src/pages/agendamento/Agendamento.jsx`, `Agendamento.css` | Uso de `Stepper`, `AutoComplete`, `Dropdown`, `Checkbox`, `Slider`, `Calendar`, `InputTextarea`; pré-seleção de UBS via `location.state` | Extrair apenas os padrões de UI necessários para `Agendamento.jsx`; não copiar mocks nem `alert` de sucesso | FE-09.3, FE-09.4, FE-12, FE-13 |
| **Campos de perfil** | `src/components/perfil/Perfil.jsx`, `Perfil.css` | Hoje está **100% comentado**; útil apenas como especificação de campos (nome, e-mail, telefone, nascimento, gênero) | Não copiar código; usar o levantamento de campos ao implementar perfil com a sessão real | FE-07, FE-09.2 |
| **Imagens adicionais** | `public/LoginImage.png`, `public/CadastroImage.png` | Ilustrações exclusivas que podem compor login/cadastro | Copiar somente se usadas no redesign; não há duplicação com `a.png`/banners atuais | FE-14.2 |
| **Configuração PrimeReact** | `src/index.jsx` da arquivada | Importa tema `lara-light-blue`, `primereact.min.css`, `primeicons.css` e usa `PrimeReactProvider` com `ripple: true` | Comparar com o `index.jsx` oficial (que importa provider de `@primereact/core` e não carrega tema) e ajustar na estabilização | FE-01.4, FE-11.2 |

---

## 3. Itens da versão arquivada que **não** devem ser reaproveitados

| Item | Motivo |
| --- | --- |
| `public/*.json` (`usuarios.json`, `unidades.json`, `doencas.json`, `nivelAcesso.json`, `sintomas.json`, `permissoes.json`, etc.) | São massas de dados simuladas; usar no máximo como referência temporária de nomes de campos até o mapeamento de DTOs (FE-09) |
| `localStorage` (`medscanUsuarios`, `medscanUsuariosRemovidos`, `medscanDoencasRemovidas`, `medscanNiveisAcesso`, etc.) | Simulam persistência e mascaram ausência de backend — proibido pelo FE-05.3 |
| Fallbacks de login/cadastro com "Modo de desenvolvimento" | Sucesso simulado — proibido pelo FE-05.1 e FE-05.2 |
| Endpoints `/usuarios/login`, `/cadastro/usuarios`, `/api/...`, `/MedScan/usuario/excluir` | Divergem do contrato Spring Boot adotado (`/usuario/...`) |
| `src/components/cadastro/CadastroDados.jsx`, `DadosBasicos.jsx`, `Endereco.jsx`, `VerificarCadastro.jsx` | Arquivos vazios ou stubs sem implementação |
| `public/m.jsx`, `public/codigo.c`, `public/VisaoJuiz.java`, `public/sintomas`, `public/n.json` | Artefatos avulsos de experimentação, fora da aplicação React |
| `src/services/healthService.js` | Totalmente comentado; não exporta serviço utilizável |

---

## 4. Critério de aceitação

**Componentes aproveitáveis estão identificados, sem duplicação de implementações.** �o.

- Os componentes compartilhados permanecem apenas na versão oficial.
- Os candidatos de reaproveitamento estão listados com destino, adaptação obrigatória e requisito relacionado.
- Nenhuma implementação duplicada foi introduzida no projeto oficial nesta etapa.

Próximo passo sugerido: **FE-01.3** — revisar a organização das pastas `pages`, `components`, `services` e `assets` na versão oficial.



