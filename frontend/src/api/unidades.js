import { post, optionalPath } from './http.js';

export const unidadesApi = {
  listar: () => post('/unidadeSaude/unidadeSaudeList', {}),
  buscarPorNome: (nome) => post('/unidadeSaude/buscarPorNome', { nome }),
  buscarPorCnes: (cnes) => post('/unidadeSaude/buscarPorCnes', { cnes: String(cnes) }),
  servicos: (cnes) => post('/tipoServico/listarPorUnidade', { unidadeId: String(cnes) }),
  salvar: (dados) => post(optionalPath(import.meta.env.VITE_UNIDADE_GERENCIAR_PATH, 'A gestão de unidades'), dados),
};



