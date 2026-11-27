# FE-01.3 — Organização das pastas `pages`, `components`, `services` e `assets`

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/` (versão oficial FE-01.1)  
**Critério de aceitação:** cada arquivo possui responsabilidade clara e está localizado de forma consistente.

## Estrutura final

```text
apenas frontend/
�"o�"?�"? public/                         # assets estáticos servidos na raiz (URL absoluta)
�",   �"o�"?�"? a.png                       # logo MedScan
�",   �"o�"?�"? Banner1.png
�",   �"o�"?�"? Banner2.png
�",   �"o�"?�"? Banner3.png
�",   �""�"?�"? favicon.svg
�"o�"?�"? src/
�",   �"o�"?�"? components/                 # pedaços de UI reutilizáveis
�",   �",   �"o�"?�"? banner/                 # Banner.jsx + Banner.css
�",   �",   �"o�"?�"? menubar/                # Menubar.jsx + Menubar.css
�",   �",   �""�"?�"? topbar/                 # TopBar.jsx + TopBar.css (menu lateral, usado por Menubar)
�",   �"o�"?�"? pages/                      # componentes de rota (um diretório por página)
�",   �",   �"o�"?�"? agendamento/            # Agendamento.jsx + Agendamento.css  �?' /agendamento
�",   �",   �"o�"?�"? cadastro/               # Cadastro.jsx + Cadastro.css        �?' /cadastro
�",   �",   �"o�"?�"? home/                   # Home.jsx + Home.css                �?' /
�",   �",   �""�"?�"? login/                  # Login.jsx + Login.css              �?' /login
�",   �"o�"?�"? services/                   # comunicação com o backend
�",   �",   �"o�"?�"? api.js                  # URL base (/MedScan), apiFetch e credenciais (FE-02.1)
�",   �",   �""�"?�"? usuarioService.js       # único serviço de usuários/sessão
�",   �"o�"?�"? App.jsx                     # registro central das rotas
�",   �""�"?�"? index.css                   # estilos globais mínimos (reset/base)
�"o�"?�"? index.html                      # entry HTML (lang pt-BR, título MedScan)
�"o�"?�"? .env.example                    # documento de configuração da URL base da API
�"o�"?�"? package.json / package-lock.json
�"o�"?�"? vite.config.js
�"o�"?�"? productBacklog.txt
�"o�"?�"? DECISAO-FE-01.1.md
�"o�"?�"? INVENTARIO-FE-01.2.md
�""�"?�"? ESTRUTURA-FE-01.3.md
```

## Responsabilidades por área

| Pasta/arquivo | Responsabilidade | Regra de localização |
| --- | --- | --- |
| `src/pages/<nome>/` | Uma página por diretório; componente da rota + CSS privado | Nome do diretório em português, minúsculas, igual ao conceito da rota |
| `src/components/<nome>/` | Componente visual reutilizável em mais de uma página | JSX e CSS colocalizados; sem regras de negócio ou chamadas HTTP diretas |
| `src/services/` | Centralizar URL base (`api.js`), endpoints, `credentials` e tratamento de resposta | Nenhuma página deve conter URL do backend (reforçado em FE-02/FE-03) |
| `public/` | Assets servidos por URL absoluta (`/a.png`, `/favicon.svg`) | Imagens referenciadas por `src="/..."` ficam aqui |
| `src/assets/` | Reservado para assets **importados** pelo código (`import logo from ...`) | Não criado enquanto não houver asset importado; criar quando necessário |
| `src/index.css` | Reset/base global | Sem regras específicas de componente |
| `src/App.jsx` | Apenas registro de rotas | Sem lógica de página ou serviço |

## Ajustes executados nesta etapa

1. **Padronização de nomes**
   - `src/pages/cadastrar/` �?' `src/pages/cadastro/` (consistência com `Cadastro.jsx` e `/cadastro`).
   - `CreateAgendamento.jsx` / `.css` �?' `Agendamento.jsx` / `.css` (consistência com a pasta e a rota `/agendamento`).
   - `App.jsx` atualizado com os novos caminhos e nome do componente.

2. **Arquivos sem responsabilidade verificável** — movidos para `../versoes-arquivadas/removidos-FE-01.3/` (nada foi excluído):
   - `src/App.css`: CSS do template Vite, sem nenhum `import`.
   - `src/components/menubar/flags.css`: sem nenhum `import` ou uso.
   - `public/icons.svg`: sem nenhuma referência no HTML/JS/CSS.

3. **Correção de referência de asset**
   - `Menubar.jsx` usava `src="a.png"` (relativo); em rotas como `/login` isso resolveria para `/login/a.png`. Alterado para `src="/a.png"`, igual a `Login.jsx` e `Cadastro.jsx`.

4. **Metadados da entry**
   - `index.html`: `lang="pt-BR"` e `<title>MedScan</title>` (antes `en` / `frontend`).

## Observações deixadas para requisitos específicos

Estes pontos **não são de organização de pastas** e foram apenas registrados para não serem esquecidos:

- `TopBar.jsx` e `Home.jsx` navegam para `/ubs`, `/admin`, `/sobre` e `/welcome`, rotas ainda não registradas �?' **FE-06.3**.
- `Home.jsx` e `Agendamento.jsx` ainda usam listas locais �?' **FE-10**.
- `usuarioService.cadastrar` ainda aponta para `/usuario/usuarioNew` �?' **FE-04.2**.
- `index.jsx` importa `PrimeReactProvider` de `@primereact/core` e não carrega o tema PrimeReact �?' **FE-01.4 / FE-11.2**.

## Critério de aceitação

**Cada arquivo possui uma responsabilidade clara e está localizado de forma consistente.** �o.

- Convenção única de nomes em `src/pages/`.
- Componentes, páginas e serviços separados por responsabilidade.
- Assets estáticos concentrados em `public/`; `src/assets/` documentado como reserva para imports.
- Arquivos órfãos arquivados, não deixados no projeto ativo.

Próximo passo sugerido: **FE-01.4** — verificar dependências, inicialização e build sem erros.



