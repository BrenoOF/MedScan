import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { Alert, Loading } from './Feedback.jsx';
import { homeForRole } from './Layout.jsx';
export function RequireAuth({ allowedRoles }) {
  const { status, role, connectionMessage, refresh } = useAuth();
  const location = useLocation();
  if (status === 'loading') return <div className="centered-screen"><Loading label="Verificando sua sessão..." /></div>;
  if (status === 'offline') return <div className="centered-screen"><div className="card narrow"><h2>Não foi possível verificar sua sessão</h2><Alert type="error">{connectionMessage}</Alert><button className="btn btn-primary" onClick={refresh}>Tentar novamente</button><p className="muted">O acesso não será liberado sem confirmação do servidor.</p></div></div>;
  if (status !== 'authenticated') return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  if (allowedRoles && !allowedRoles.includes(role)) return <Navigate to={homeForRole(role)} replace />;
  return <Outlet />;
}
export function GuestOnly({ children }) {
  const { status, role } = useAuth();
  // Visitantes offline ainda podem abrir a página de login para tentar novamente.
  if (status === 'loading') return <div className="centered-screen"><Loading label="Verificando sua sessão..." /></div>;
  if (status === 'authenticated') return <Navigate to={homeForRole(role)} replace />;
  return children;
}
