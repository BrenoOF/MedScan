import { post } from './http.js';
export const niveisApi = {
  listar: () => post('/nivelAcesso/nivelAcessoList', {}),
  criar: (descricao, funcaoId) => post('/nivelAcesso/nivelAcessoNew', { descricao, funcaoId }),
};
