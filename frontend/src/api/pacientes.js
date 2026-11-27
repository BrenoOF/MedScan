import { get, post, optionalPath } from './http.js';

export const pacientesApi = {
  perfil: () => get('/paciente/pacientePerfil'),
  cadastroCompleto: (dados) => post('/paciente/pacienteCadastroCompleto', dados),
  atualizar: (dados) => post(optionalPath(import.meta.env.VITE_PACIENTE_ATUALIZAR_PATH, 'A edição do perfil'), dados),
};



