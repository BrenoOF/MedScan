import { useState } from 'react';
import { NavLink, Link, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { firstName } from '../utils/normalize.js';
import { Alert } from './Feedback.jsx';

const patientMenu = [
  { href: '/app', icon: 'pi-home', label: 'Visão geral', end: true },
  { href: '/app/ubs', icon: 'pi-building', label: 'Unidades de saúde' },
  { href: '/app/agendar', icon: 'pi-calendar-plus', label: 'Solicitar atendimento' },
  { href: '/app/agendamentos', icon: 'pi-calendar', label: 'Meus agendamentos' },
  { href: '/app/perfil', icon: 'pi-user', label: 'Meu perfil' },
];
const attendantMenu = [
  { href: '/atendente', icon: 'pi-home', label: 'Visão geral', end: true },
  { href: '/atendente/solicitacoes', icon: 'pi-list-check', label: 'Solicitações da UBS' },
  { href: '/app/perfil', icon: 'pi-user', label: 'Meu perfil' },
];
const administradorMenu = [
  { href: '/administrador', icon: 'pi-home', label: 'Visão geral', end: true },
  { href: '/administrador/usuarios', icon: 'pi-users', label: 'Usuários' },
  { href: '/administrador/niveis', icon: 'pi-lock', label: 'Níveis de acesso' },
  { href: '/administrador/unidades', icon: 'pi-building', label: 'Unidades e serviços' },
  { href: '/app/perfil', icon: 'pi-user', label: 'Meu perfil' },
];
export const homeForRole = (role) => role === 'administrador' ? '/administrador' : role === 'atendente' ? '/atendente' : '/app';

export function AppLayout() {
  const { user, role, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const [collapsed, setCollapsed] = useState(false);
  const [logoutError, setLogoutError] = useState('');
  const items = role === 'administrador' ? administradorMenu : role === 'atendente' ? attendantMenu : patientMenu;

  function toggleMenu() {
    if (window.matchMedia('(max-width: 980px)').matches) {
      setMenuOpen((open) => !open);
    } else {
      setCollapsed((value) => !value);
    }
  }

  function closeMenu() {
    setMenuOpen(false);
    setCollapsed(false);
  }

  async function signOut() {
    setLogoutError('');
    try { await logout(); navigate('/login', { replace: true }); }
    catch (error) { setLogoutError(`Não foi possível encerrar a sessão: ${error.message}`); }
  }
  return <div className={`app-shell ${collapsed ? 'is-collapsed' : ''}`}>
    {menuOpen && <button className="sidebar-backdrop" aria-label="Fechar menu" onClick={() => setMenuOpen(false)} />}
    <aside className={`sidebar ${menuOpen ? 'is-open' : ''}`} aria-label="Navegação principal">
      <Link to={homeForRole(role)} className="sidebar-brand" onClick={closeMenu}><img src="/a.png" alt="MedScan" /><span>Plataforma de saúde</span></Link>
      <div className="sidebar-section-label">MENU PRINCIPAL</div>
      <nav className="sidebar-nav">
        {items.map((item) => <NavLink key={item.href} to={item.href} end={item.end} onClick={closeMenu} className={({isActive}) => `sidebar-link ${isActive ? 'active' : ''}`}><i className={`pi ${item.icon}`} />{item.label}</NavLink>)}
      </nav>
      <div className="sidebar-bottom"><div className="help-card"><i className="pi pi-heart" /><strong>Cuidado com informação.</strong><span>Organize suas solicitações de saúde em um só lugar.</span></div><Link className="sidebar-secondary" to="/sobre" onClick={closeMenu}><i className="pi pi-info-circle" /> Sobre o MedScan</Link><button className="sidebar-secondary" onClick={signOut}><i className="pi pi-sign-out" /> Sair da conta</button></div>
    </aside>
    <div className="app-main">
      <header className="app-topbar"><div className="topbar-start"><button className="icon-button mobile-menu" aria-label={collapsed || menuOpen ? 'Abrir menu' : 'Fechar menu'} aria-expanded={!collapsed && !menuOpen} onClick={toggleMenu}><i className="pi pi-bars" /></button><div className="crumb"><span>MedScan</span><i className="pi pi-chevron-right" aria-hidden="true" /><strong>{role === 'administrador' ? 'Gestão' : role === 'atendente' ? 'Atendimento' : 'Área do paciente'}</strong></div></div><div className="topbar-user"><span className="topbar-user-info"><strong>{user?.nome || 'Usuário'}</strong><small>{role === 'administrador' ? 'Administrador' : role === 'atendente' ? 'Atendente' : 'Paciente'}</small></span><span className="avatar">{firstName(user).charAt(0).toUpperCase()}</span></div></header>
      {logoutError && <div className="topbar-alert"><Alert type="error" onClose={() => setLogoutError('')}>{logoutError}</Alert></div>}
      <main className="main-content" id="conteudo"><Outlet /></main>
      <footer className="app-footer">MedScan • Acesso à saúde com mais organização <span>Os dados são consultados no sistema integrado.</span></footer>
    </div>
  </div>;
}



