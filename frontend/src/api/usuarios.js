import { get, post, optionalPath } from './http.js';

export const usuariosApi = {
  login: (email, senha) => post('/usuario/usuarioLogin', { email, senha }),
  cadastro: (nome, email, senha) => post('/usuario/usuarioCadastro', { nome, email, senha }),
  cadastroPacienteCompleto: (dados) => post('/paciente/pacienteCadastroCompleto', dados),
  sessao: () => get('/usuario/usuarioSession'),
  logout: () => post('/usuario/usuarioLogout', {}),
  listar: (filtro = 'todos', valor = '') => post('/usuario/usuarioList', {
    filtro: filtro === 'todos' ? 'listar todos' : filtro,
    ...(filtro === 'todos' ? {} : { valor }),
  }),
  criarAdministrativo: async (usuario) => {
    const response = await post('/usuario/usuarioNew', usuario);
    if (!response?.usuario?.id) {
      throw new Error('O servidor não confirmou o cadastro do usuário.');
    }
    return response;
  },
  excluir: (id) => post('/usuario/usuarioDelete', { id }),
  editar: (usuario) => post(optionalPath(import.meta.env.VITE_USUARIO_EDITAR_PATH, 'A edição de usuários'), usuario),
};



