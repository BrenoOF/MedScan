# Resumo das correções de frontend relacionadas à auditoria

| Item da auditoria | Solução entregue |
|---|---|
| P18 — duas versões concorrentes | Projeto React/Vite unificado, com uma única `App.jsx` e rotas coerentes |
| P19 — falsos sucessos e `localStorage` como banco | Removidos; cadastro e autenticação dependem de respostas reais da API |
| P20 — `HealthService` inexistente | Substituído por consulta real de UBS através de `/unidadeSaude/unidadeSaudeList` |
| P21 — cadastro/listagem administrativa incompatíveis | Cadastro público corrigido, listagem envia `{ filtro, valor }`, criação administrativa separada |
| P22 — proxy e rotas quebradas | Vite proxy `/MedScan`; rotas públicas, paciente, atendente e administrador com links existentes |
| P23 — rotas sem proteção | `AuthProvider`, `RequireAuth`, restrição visual de perfis e restauração de sessão |
| P24 — testes insuficientes | Testes unitários dos adaptadores; roteiro de testes integrados documentado |
| P09 — paciente sem endereço | Formulário preparado, com bloqueio honesto até backend oferecer cadastro completo seguro |
| P11/P12 — ausência de API de agendamento | Formulário completo sem inventar persistência; adaptador e proposta de contrato documentados |
| P16 — atendente e administrador incompletos | Painéis e navegação criados; integração de atendente explicitamente pendente |

## Mantido do projeto original

- Identidade visual e logotipo MedScan.
- Ilustrações originais dos banners/cadastro/login.
- Stack React + Vite e dependências PrimeReact/PrimeIcons da versão original.
- Estrutura de consulta a unidades de saúde e formulário de agendamento como conceitos de fluxo, reconstruídos com serviços centralizados e estados explícitos.

## Não alegado como concluído

- Funcionalidade real de agendamentos, histórico e cancelamento.
- Atualização de status do atendente.
- Criação e edição de UBS/serviços, edição de usuários e exclusões.
- Persistência de perfil completo com endereço.
- Testes de ponta a ponta com Spring Boot e MariaDB.



