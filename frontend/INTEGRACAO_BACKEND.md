# MedScan — mapa de integração com o Spring Boot

Base de API assumida (auditoria, novembro de 2026): `http://localhost:8080/MedScan`.

## Endpoints confirmados pela auditoria

| Operação | Método | Endpoint | Corpo esperado / observações |
|---|---|---|---|
| Login | POST | `/usuario/usuarioLogin` | `{ "email": "...", "senha": "..." }` |
| Cadastro público | POST | `/usuario/usuarioCadastro` | `{ "nome": "...", "email": "...", "senha": "..." }` |
| Logout | POST | `/usuario/usuarioLogout` | Sessão HTTP; frontend envia `{}` |
| Sessão | GET | `/usuario/usuarioSession` | `{ "usuario": { ... } }` |
| Usuários | POST | `/usuario/usuarioList` | `{ "filtro": "todos", "valor": "" }`; confirmar valores aceitos para cada filtro |
| Criação administrativa | POST | `/usuario/usuarioNew` | `{ "nome": "...", "email": "...", "senha": "...", "nivelAcessoId": 1 }` com autorização administrativa |
| Consulta de UBS | POST | `/unidadeSaude/unidadeSaudeList` | `{}` |
| Busca por nome | POST | `/unidadeSaude/buscarPorNome` | `{ "nome": "..." }` |
| Busca por CNES | POST | `/unidadeSaude/buscarPorCnes` | `{ "cnes": "1234567" }` |
| Serviços por UBS | POST | `/tipoServico/listarPorUnidade` | `{ "unidadeId": "1234567" }` (a auditoria informa que é o CNES) |
| Perfil de paciente | GET | `/paciente/pacientePerfil` | Autenticado, 200 ou 404 |
| Níveis de acesso | POST | `/nivelAcesso/nivelAcessoList` | Forma do retorno não completamente documentada na auditoria; conferir com Controller |

**Respostas variáveis:** o frontend lê coleções em propriedades como `usuarios`, `unidadeSaude`, `tipoServico`, `nivelAcesso`. A formatação definitiva deve ser confirmada com a API real. Para ausência de resultado, a auditoria aponta HTTP 204 em algumas rotas. Erros 400, 401, 403, 404, 409 e 500 recebem mensagens apropriadas na interface.

## Endpoints **não existentes** — propostas a implementar

Os nomes sugeridos abaixo são apenas exemplos para orientar a integração. **Não aparecem como implementados na auditoria**. Só descomente as variáveis no `.env` quando o Spring Boot realmente expuser os contratos.

### Agendamento do paciente

```
POST /agendamento/criar
{
  "unidadeId": "1234567",
  "tipoServicoId": 5,
  "diaAgendamento": "2026-11-20",
  "horaAgendamento": "09:00",
  "escalaDor": 3,
  "sintomas": "...",
  "queixa": "..."
}
```

O backend deve obter **pacienteId pela sessão**, atribuir atendente quando aplicável, verificar o vínculo UBS/serviço, evitar duplicação e conflitos, definir `status` conforme regras de negócio e verificar disponibilidade. A resposta de sucesso deve incluir um registro identificável, por exemplo `{ "agendamento": { "id": 7, "status": "PENDENTE" } }`.

O `AgendamentoRequest` **atual** descrito na auditoria ainda espera `buscaAtivaId`, `pacienteId`, `atendenteId`, `classificacaoRisco` e `status`; esse contrato precisa ser adaptado com segurança no backend. O frontend intencionalmente **não preenche identidades arbitrárias ou status clínico**.

### Acompanhamento do paciente

```
POST /agendamento/meus   {}
POST /agendamento/cancelar   {"id":7}
```

A listagem deve sempre considerar o usuário autenticado, não um `pacienteId` recebido do navegador. Cancelamento deve verificar titularidade, status permitido e garantir a preservação de histórico.

### Painel do atendente

```
POST /agendamento/porUnidade   {}
POST /agendamento/alterarStatus   {"id":7,"status":"CONFIRMADO"}
```

A UBS deve ser inferida do perfil do atendente autenticado no backend; não aceite IDs arbitrários enviados pelo React como critério suficiente de autorização.

### Cadastro completo do paciente

```
POST /paciente/cadastroCompleto
{
  "cpf": "00000000000",
  "dataNascimento": "2000-01-01",
  "genero": "Outro",
  "logradouro": "...",
  "numero": "...",
  "bairro": "...",
  "cidade": "...",
  "uf": "SP",
  "cep": "00000000"
}
```

Esse é um **novo contrato proposto** para cadastrar endereço e paciente de forma transacional usando o **usuário da sessão**, sem exigir `enderecoId` pré-cadastrado nem `usuarioId` do cliente. Também é necessário corrigir o enum de gênero.

### Administração

- Adicionar endpoint administrativo de edição de usuários.
- Expor operações autorizadas para cadastrar/editar UBS e serviços.
- Expor listagem das funções para permitir criar níveis de acesso sem informar IDs manualmente.
- Corrigir exclusões em cascata e autorizações antes de habilitar a exclusão no React.

## Configurações de CSRF e sessão

- Autenticação: sessão HTTP com cookies, **sem JWT**.
- Frontend: `credentials: 'include'` para todas as chamadas.
- Proxy: desenvolvimento na porta 5173 encaminhando `/MedScan`.
- Backend: habilitar estratégia CSRF, entregar o token ao navegador para envio em `X-XSRF-TOKEN` ou especificar outra estratégia compartilhada; configurar SameSite e Secure corretamente. Os ajustes devem ser validados juntos.
- CORS: se frontend/backend forem hospedes diferentes, atualizar a lista de origens permitidas e as credenciais com cuidado.

## Checklist de testes integrados para a equipe

1. Compilação do Spring Boot sem o erro `Optional<Usuario>`.
2. Criação pública da conta e login com e-mail/senha corretos.
3. Recarregar a página mantendo a sessão válida.
4. Logout efetivo e acesso negado à área interna após sair.
5. O paciente A não consegue ver dados do paciente B.
6. O atendente vê apenas as solicitações da sua UBS.
7. O supervisor executa apenas as ações autorizadas.
8. UBS e serviços são consultados e retornam dados reais.
9. Agendamento só confirma após ter sido registrado no MariaDB.
10. Tratamento de falhas de API, de rede, páginas vazias e acesso sem permissão.
11. Responsividade, navegação por teclado e contraste nos principais formulários.
