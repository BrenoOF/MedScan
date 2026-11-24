import { get, post, optionalPath } from './http.js';

export const usuariosApi = {
  login: (email, senha) => post('/usuario/usuarioLogin', { email, senha }),
  cadastro: (nome, email, senha) => post('/usuario/usuarioCadastro', { nome, email, senha }),
  sessao: () => get('/usuario/usuarioSession'),
  logout: () => post('/usuario/usuarioLogout', {}),
  /** O contrato exige filtro; {} resulta em HTTP 400. */
  listar: (filtro = 'todos', valor = '') => post('/usuario/usuarioList', { filtro, valor }),
  criarAdministrativo: (usuario) => post('/usuario/usuarioNew', usuario),
  excluir: (id) => post('/usuario/usuarioDelete', { id }),
  editar: (usuario) => post(optionalPath(import.meta.env.VITE_USUARIO_EDITAR_PATH, 'A edição de usuários'), usuario),
};
