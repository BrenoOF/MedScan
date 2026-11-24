import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { firstName } from '../utils/normalize.js';
import { PageHeading, PendingFeature } from '../components/Feedback.jsx';

export default function Dashboard() {
  const { user } = useAuth();
  return <><PageHeading eyebrow="ÁREA DO PACIENTE" title={`Olá, ${firstName(user)}!`} description="Que bom ter você por aqui. O que você gostaria de fazer hoje?" />
    <div className="welcome-banner"><div><span className="white-kicker">SEU ESPAÇO DE CUIDADO</span><h2>Mais facilidade para encontrar o atendimento que você precisa.</h2><p>Consulte unidades e serviços disponíveis e acompanhe seus pedidos com transparência.</p><Link className="btn btn-white" to="/app/ubs">Encontrar unidade <i className="pi pi-arrow-right"/></Link></div><div className="welcome-illustration"><i className="pi pi-heart-fill"/><div className="care-ring"></div></div></div>
    <div className="section-heading compact"><h2>Acesso rápido</h2><p>Escolha uma das opções abaixo para continuar.</p></div>
    <div className="quick-grid"><Link to="/app/ubs" className="quick-card"><span className="quick-icon turquoise"><i className="pi pi-map-marker"/></span><h3>Unidades de saúde</h3><p>Consulte as UBS e os serviços oferecidos.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link><Link to="/app/agendar" className="quick-card"><span className="quick-icon blue"><i className="pi pi-calendar-plus"/></span><h3>Solicitar atendimento</h3><p>Informe suas necessidades e a unidade desejada.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link><Link to="/app/agendamentos" className="quick-card"><span className="quick-icon lilac"><i className="pi pi-clock"/></span><h3>Meus agendamentos</h3><p>Consulte solicitações e seus respectivos status.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link><Link to="/app/perfil" className="quick-card"><span className="quick-icon yellow"><i className="pi pi-user"/></span><h3>Meu perfil</h3><p>Confira seus dados cadastrais.</p><span className="quick-arrow"><i className="pi pi-arrow-right"/></span></Link></div>
    <div className="section-heading compact" style={{marginTop:36}}><h2>Informações do sistema</h2></div><PendingFeature feature="Agendamentos" details="A interface já está preparada, mas o backend auditado ainda não disponibiliza criação e consulta de agendamentos. Não exibimos pedidos ou confirmações simuladas." />
  </>;
}
