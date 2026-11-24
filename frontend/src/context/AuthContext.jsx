import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { usuariosApi } from '../api/usuarios.js';
import { parseRole } from '../utils/normalize.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState('loading');
  const [connectionMessage, setConnectionMessage] = useState('');

  const refresh = useCallback(async () => {
    setStatus('loading');
    try {
      const data = await usuariosApi.sessao();
      const usuario = data?.usuario || data?.user || null;
      setUser(usuario);
      setConnectionMessage('');
      setStatus(usuario ? 'authenticated' : 'anonymous');
      return usuario;
    } catch (error) {
      setUser(null);
      if (error.status === 401 || error.status === 403) {
        setConnectionMessage('');
        setStatus('anonymous');
      } else {
        setConnectionMessage(error.message);
        setStatus('offline');
      }
      return null;
    }
  }, []);

  useEffect(() => { refresh(); }, [refresh]);

  const login = useCallback(async (email, senha) => {
    const response = await usuariosApi.login(email, senha);
    const usuario = response?.usuario || response?.user;
    if (!usuario) {
      // Algumas APIs devolvem só a mensagem. Consulta a sessão para validar a autenticação.
      const sessao = await usuariosApi.sessao();
      const recovered = sessao?.usuario || sessao?.user;
      if (!recovered) throw new Error('Não foi possível confirmar a sessão do usuário.');
      setUser(recovered);
    } else setUser(usuario);
    setStatus('authenticated');
    setConnectionMessage('');
    return response;
  }, []);

  const logout = useCallback(async () => {
    await usuariosApi.logout();
    setUser(null);
    setStatus('anonymous');
  }, []);
  const value = useMemo(() => ({ user, role: parseRole(user), status, connectionMessage, login, logout, refresh }), [user, status, connectionMessage, login, logout, refresh]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth deve ser utilizado dentro do AuthProvider');
  return context;
}
