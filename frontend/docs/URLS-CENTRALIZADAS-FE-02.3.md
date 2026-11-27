# FE-02.3 — Eliminar URLs inconsistentes e caminhos HTTP fixos

**Data:** 26/11/2026  
**Projeto:** `apenas frontend/` (oficial FE-01.1)  
**Critério de aceitação:** as chamadas utilizam o serviço HTTP centralizado.

## Varredura realizada

Busca em `src/**/*.{js,jsx,css}` por:

- `fetch(`, `axios`, `XMLHttpRequest`;
- `https?://`, `/api/`, `/MedScan`;
- `credentials`;
- `.json` (buscas em arquivos locais).

### Resultado

| Padrão | Onde aparece | Situação |
| --- | --- | --- |
| `fetch(` | somente `src/services/api.js` | �o. único ponto |
| `credentials` | somente `src/services/api.js` | �o. único ponto |
| `/MedScan` | somente `src/services/api.js` | �o. único ponto |
| `"/usuario/..."` | somente `src/services/usuarioService.js` (7 endpoints) | �o. centralizado |
| `axios`, `XMLHttpRequest` | nenhum arquivo | �o. inexistente |
| `fetch("/algo.json")` | nenhum arquivo na versão oficial | �o. inexistente |

As páginas (`Home`, `Login`, `Cadastro`, `Agendamento`) e os componentes (`Banner`, `Menubar`, `TopBar`) **não contêm URL do backend**. Eles chamam apenas `usuarioService` ou usam dados locais explícitos.

> Os JSONs simulados (`unidades.json`, `usuarios.json`, etc.) existem apenas na versão arquivada, conforme `INVENTARIO-FE-01.2.md`, e não foram trazidos para o projeto oficial.

## Endpoint mapeado (referência, não duplicado)

| Método | Caminho passado a `apiFetch` | Chamador |
| --- | --- | --- |
| POST | `/usuario/usuarioLogin` | `usuarioService.login` |
| POST | `/usuario/usuarioNew` | `usuarioService.cadastrar` |
| POST | `/usuario/usuarioList` | `usuarioService.listar` |
| POST | `/usuario/usuarioDelete` | `usuarioService.deletar` |
| POST | `/usuario/usuarioEdit` | `usuarioService.editar` |
| POST | `/usuario/usuarioLogout` | `usuarioService.logout` |
| GET | `/usuario/usuarioSession` | `usuarioService.sessao` |

O prefixo `/MedScan` e a origem são resolvidos por `apiUrl()`; nenhum desses caminhos é repetido em componentes.

## Guarda contra regressão

Para garantir que o critério continue válido, `.oxlintrc.json` passou a proibir `fetch` fora da camada HTTP:

```json
"no-restricted-globals": [
  "error",
  { "name": "fetch", "message": "Use apiFetch() de src/services/api.js em vez de fetch() direto." }
]
```

Exceção única, via `overrides`: `src/services/api.js`, onde o `fetch` real é executado.

### Teste negativo executado

Foi criado temporariamente `src/__fe023_probe.jsx` com:

```js
fetch("/usuario/usuarioLogin")
```

O lint acusou:

```text
error eslint(no-restricted-globals): Unexpected use of 'fetch'.
Use apiFetch() de src/services/api.js em vez de fetch() direto.
```

O arquivo de teste foi removido em seguida e o lint final ficou limpo.

## Verificação final

- `npm run lint`: �o. sem avisos/erros.
- `npm run build`: �o. sem erros (77 módulos).
- Nenhum arquivo temporário restou em `src/`.

## Observações

- Caminhos de navegação (`navigate('/home')`, `navigate('/agendamento')`, `src="/a.png"`) **não** são chamadas HTTP e, portanto, não entram neste requisito. Rotas inexistentes são tratadas em **FE-06.3**.
- A correção de contrato de `/usuario/usuarioNew` para `/usuario/usuarioCadastro` pertence a **FE-04.2**; aqui apenas se garante que o caminho está centralizado no serviço.
- O tratamento de headers, JSON e status HTTP pertence a **FE-03**.

## Critério de aceitação

**As chamadas utilizam o serviço HTTP centralizado.** �o.

Próximo passo sugerido: **FE-02.4** — verificar compatibilidade entre origem do frontend, CORS e cookies.



