import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { homeForRole } from '../components/Layout.jsx';

export default function Landing() {
  const { status, role } = useAuth();
  const destination = status === 'authenticated' ? homeForRole(role) : '/cadastro';
  return <div className="public-page">
    <header className="public-header wrap"><Link to="/" className="public-logo"><img src="/a.png" alt="MedScan" /></Link><nav className="public-navigation" aria-label="Navegação"><a href="#como-funciona">Como funciona</a><Link to="/sobre">Sobre nós</Link></nav><div className="public-actions"><Link className="btn btn-outline" to="/login">Entrar</Link><Link className="btn btn-primary" to={destination}>{status === 'authenticated' ? 'Meu painel' : 'Criar conta'} <i className="pi pi-arrow-right" /></Link></div></header>
    <main>
      <section className="hero"><div className="wrap hero-grid"><div className="hero-copy"><span className="hero-kicker"><i className="pi pi-heart-fill" /> TECNOLOGIA A FAVOR DA SAÚDE</span><h1>Seu cuidado merece um caminho <em>mais simples.</em></h1><p>Conheça as unidades de saúde, consulte os serviços disponíveis e acompanhe suas solicitações em uma experiência mais organizada.</p><div className="hero-buttons"><Link to={destination} className="btn btn-primary btn-large">{status === 'authenticated' ? 'Acessar painel' : 'Começar agora'} <i className="pi pi-arrow-right" /></Link><Link to="/sobre" className="btn btn-light btn-large">Conheça o MedScan</Link></div><div className="hero-footnote"><i className="pi pi-shield" /> Seus dados são tratados pelo sistema autenticado, sem simulações de cadastro.</div></div><div className="hero-visual"><div className="hero-blob" /><img src="/Banner2.png" alt="Ilustração da proposta de gestão e acessibilidade do MedScan" /><div className="floating-tile"><span className="floating-icon"><i className="pi pi-calendar" /></span><div><strong>Solicitações organizadas</strong><small>Acompanhamento em um só lugar</small></div></div></div></div></section>
      <section className="feature-section wrap" id="como-funciona"><div className="section-heading centered"><span className="eyebrow">COMO FUNCIONA</span><h2>Uma jornada mais prática para você</h2><p>Da escolha da unidade ao acompanhamento do atendimento.</p></div><div className="features-grid">{[
        ['pi-map-marker','01','Encontre sua UBS','Veja as unidades cadastradas e os serviços que oferecem.'],
        ['pi-calendar-plus','02','Solicite atendimento','Preencha os dados necessários para enviar uma solicitação.'],
        ['pi-check-circle','03','Acompanhe o retorno','Consulte o andamento quando a integração de agendamentos estiver disponível.'],
      ].map(([icon,step,title,description]) => <article className="feature-card" key={step}><div className="feature-icon"><i className={`pi ${icon}`} /></div><div className="feature-step">ETAPA {step}</div><h3>{title}</h3><p>{description}</p></article>)}</div></section>
      <section className="public-cta wrap"><div><span className="eyebrow">MEDSCAN</span><h2>Mais clareza para quem cuida e para quem precisa de cuidado.</h2><p>Uma plataforma criada para aproximar pacientes, atendentes e gestores.</p></div><Link to={destination} className="btn btn-white">{status === 'authenticated' ? 'Abrir meu painel' : 'Criar minha conta'} <i className="pi pi-arrow-right" /></Link></section>
    </main><footer className="public-footer wrap"><img src="/a.png" alt="MedScan" /><span>© 2026 MedScan — Projeto acadêmico de tecnologia em saúde.</span><Link to="/sobre">Sobre o projeto</Link></footer>
  </div>;
}
