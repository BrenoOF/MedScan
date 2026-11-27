import { Navigate, Route, Routes } from 'react-router-dom';
import Landing from './pages/Landing.jsx';
import { Login, AccessRedirect } from './pages/AuthPages.jsx';
import CadastroPaciente from './pages/CadastroPaciente.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Unidades from './pages/Unidades.jsx';
import Agendar from './pages/Agendar.jsx';
import Agendamentos from './pages/Agendamentos.jsx';
import Perfil from './pages/Perfil.jsx';
import Atendente from './pages/Atendente.jsx';
import Administrador from './pages/Administrador.jsx';
import { Usuarios, UsuarioNovo } from './pages/Usuarios.jsx';
import Niveis from './pages/Niveis.jsx';
import Sobre from './pages/Sobre.jsx';
import { AppLayout } from './components/Layout.jsx';
import { RequireAuth, GuestOnly } from './components/Access.jsx';

export default function App(){return <Routes>
  <Route path="/" element={<Landing/>}/>
  <Route path="/sobre" element={<Sobre/>}/>
  <Route path="/login" element={<GuestOnly><Login/></GuestOnly>}/>
  <Route path="/cadastro" element={<GuestOnly><CadastroPaciente/></GuestOnly>}/>
  <Route element={<RequireAuth/>}>
    <Route path="/acesso" element={<AccessRedirect/>}/>
    <Route element={<AppLayout/>}>
      <Route path="/app/perfil" element={<Perfil/>}/>
      <Route element={<RequireAuth allowedRoles={['paciente']}/> }>
        <Route path="/app" element={<Dashboard/>}/>
        <Route path="/app/ubs" element={<Unidades/>}/>
        <Route path="/app/agendar" element={<Agendar/>}/>
        <Route path="/app/agendamentos" element={<Agendamentos/>}/>
      </Route>
      <Route element={<RequireAuth allowedRoles={['atendente']}/> }>
        <Route path="/atendente" element={<Atendente dashboard/>}/>
        <Route path="/atendente/solicitacoes" element={<Atendente/>}/>
      </Route>
      <Route element={<RequireAuth allowedRoles={['administrador']}/> }>
        <Route path="/administrador" element={<Administrador/>}/>
        <Route path="/administrador/usuarios" element={<Usuarios/>}/>
        <Route path="/administrador/usuarios/novo" element={<UsuarioNovo/>}/>
        <Route path="/administrador/niveis" element={<Niveis/>}/>
        <Route path="/administrador/unidades" element={<Unidades administrador/>}/>
      </Route>
    </Route>
  </Route>
  <Route path="/home" element={<Navigate to="/acesso" replace/>}/>
  <Route path="/welcome" element={<Navigate to="/acesso" replace/>}/>
  <Route path="/agendamento" element={<Navigate to="/app/agendar" replace/>}/>
  <Route path="/ubs" element={<Navigate to="/app/ubs" replace/>}/>
  <Route path="/admin" element={<Navigate to="/administrador" replace/>}/>
  <Route path="/supervisor" element={<Navigate to="/administrador" replace/>}/>
  <Route path="/supervisor/usuarios" element={<Navigate to="/administrador/usuarios" replace/>}/>
  <Route path="/supervisor/usuarios/novo" element={<Navigate to="/administrador/usuarios/novo" replace/>}/>
  <Route path="/supervisor/niveis" element={<Navigate to="/administrador/niveis" replace/>}/>
  <Route path="/supervisor/unidades" element={<Navigate to="/administrador/unidades" replace/>}/>
  <Route path="/usuarioList" element={<Navigate to="/administrador/usuarios" replace/>}/>
  <Route path="/usuarioNew" element={<Navigate to="/administrador/usuarios/novo" replace/>}/>
  <Route path="/nivelAcessoList" element={<Navigate to="/administrador/niveis" replace/>}/>
  <Route path="/perfil" element={<Navigate to="/app/perfil" replace/>}/>
  <Route path="*" element={<div className="centered-screen"><div className="card narrow"><h1>Página não encontrada</h1><p>O endereço informado não corresponde a uma página do MedScan.</p><a className="btn btn-primary" href="/">Voltar ao início</a></div></div>}/>
</Routes>}



