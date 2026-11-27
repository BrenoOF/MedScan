import { get, post, optionalPath } from './http.js';

export const agendamentosApi = {
  criar: (dados) => post('/agendamento/agendamentoNew', dados),
  meus: () => get('/agendamento/agendamentoPacienteList'),
  porUnidade: (filtros = {}) => post('/agendamento/porUnidade', filtros),
  atualizarStatus: (id, status) => post('/agendamento/alterarStatus', { id, status }),
  cancelar: (id) => post(optionalPath(import.meta.env.VITE_AGENDAMENTO_CANCELAR_PATH, 'O cancelamento de agendamento'), { id }),
};



