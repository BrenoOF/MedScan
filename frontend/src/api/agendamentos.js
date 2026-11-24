import { post, optionalPath } from './http.js';

// Os endpoints de agendamento NÃO existem no backend auditado.
// Os nomes e os contratos finais devem ser definidos com o AgendamentoController.
export const agendamentosApi = {
  criar: (dados) => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_CRIAR_PATH, 'A solicitação de agendamento'), dados),
  meus: () => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_MEUS_PATH, 'A consulta dos seus agendamentos'), {}),
  porUnidade: (filtros = {}) => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_UNIDADE_PATH, 'O painel de agendamentos da UBS'), filtros),
  atualizarStatus: (id, status) => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_STATUS_PATH, 'A atualização do status'), { id, status }),
  cancelar: (id) => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_CANCELAR_PATH, 'O cancelamento de agendamento'), { id }),
};
