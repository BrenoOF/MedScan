import { get, post, optionalPath } from './http.js';

export const pacientesApi = {
  perfil: () => get('/paciente/pacientePerfil'),
  /** Só usar quando o backend expuser cadastro completo seguro, com endereço e titularidade na sessão. */
  cadastroCompleto: (dados) => post(optionalPath(import.meta.env.VITE_PACIENTE_CADASTRO_COMPLETO_PATH, 'O cadastro completo do paciente'), dados),
  atualizar: (dados) => post(optionalPath(import.meta.env.VITE_PACIENTE_ATUALIZAR_PATH, 'A edição do perfil'), dados),
};
