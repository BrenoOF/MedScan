import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { firstName } from '../utils/normalize.js';
import { PageHeading, Alert } from '../components/Feedback.jsx';

export default function Administrador(){const {user}=useAuth();return <><PageHeading eyebrow="GESTÃO DO MEDSCAN" title={`Olá, ${firstName(user)}!`} description="Gerencie os dados essenciais da plataforma com controle de acesso."/>
  <div className="welcome-banner administrador-banner"><div><span className="white-kicker">ADMINISTRAÇÃO</span><h2>Organização e controle em um só painel.</h2><p>Acompanhe os cadastros da plataforma e consulte as configurações disponíveis no backend.</p><Link to="/administrador/usuarios" className="btn btn-white">Gerenciar usuários <i className="pi pi-arrow-right" /></Link></div><div className="welcome-illustration"><i className="pi pi-shield"/><div className="care-ring" /></div></div>
  <div className="section-heading compact"><h2>Gestão da plataforma</h2><p>Selecione a área que deseja consultar.</p></div><div className="quick-grid"><Link className="quick-card" to="/administrador/usuarios"><span className="quick-icon blue"><i className="pi pi-users"/></span><h3>Usuários</h3><p>Consulte e cadastre usuários conforme suas permissões.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link><Link className="quick-card" to="/administrador/niveis"><span className="quick-icon lilac"><i className="pi pi-lock"/></span><h3>Níveis de acesso</h3><p>Visualize níveis cadastrados na plataforma.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link><Link className="quick-card" to="/administrador/unidades"><span className="quick-icon turquoise"><i className="pi pi-building"/></span><h3>Unidades</h3><p>Consulte as unidades de saúde cadastradas.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link></div><div style={{marginTop:24}}><Alert type="warning">A edição e a exclusão de usuários, assim como alterações em unidades de saúde, ainda não estão disponíveis.</Alert></div>
  </>}



