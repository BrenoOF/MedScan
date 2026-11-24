import { useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { usuariosApi } from '../api/usuarios.js';
import { Alert } from '../components/Feedback.jsx';
import { homeForRole } from '../components/Layout.jsx';

function AuthFrame({ title, subtitle, children, image = '/LoginImage.png' }) {
  return <div className="auth-page"><div className="auth-panel"><div className="auth-top"><Link to="/"><img src="/a.png" alt="MedScan" /></Link><Link className="back-link" to="/"><i className="pi pi-arrow-left" /> Voltar ao início</Link></div><div className="auth-body"><div className="auth-heading"><span className="eyebrow">BEM-VINDO AO MEDSCAN</span><h1>{title}</h1><p>{subtitle}</p></div>{children}</div><div className="auth-bottom">MedScan • Saúde e tecnologia</div></div><aside className="auth-aside"><img src={image} alt="Ilustração da plataforma MedScan" /><div className="auth-quote"><i className="pi pi-heart-fill"/><h2>Conectando pessoas a um cuidado melhor.</h2><p>Informação organizada, acessível e disponível no momento certo.</p></div></aside></div>;
}
export function Login() {
  const [form, setForm] = useState({email:'',senha:''});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  async function submit(e) {
    e.preventDefault();setError('');setLoading(true);
    try {
      await login(form.email.trim().toLowerCase(),form.senha);
      // Não confia em perfil derivado de dados digitados. O contexto foi atualizado pelo servidor.
      const state = location.state?.from;
      navigate(state && !state.startsWith('/supervisor') && !state.startsWith('/atendente') ? state : '/acesso', {replace:true});
    } catch(err){setError(err.message || 'Não foi possível entrar.');}
    finally{setLoading(false);}
  }
  return <AuthFrame title="Acesse sua conta" subtitle="Entre para acompanhar seus atendimentos e acessar os serviços disponíveis."><form className="auth-form" onSubmit={submit}><label className="field"><span>E-mail</span><div className="input-icon"><i className="pi pi-envelope"/><input type="email" autoComplete="email" placeholder="seuemail@exemplo.com" required maxLength={150} value={form.email} onChange={e=>setForm({...form,email:e.target.value})} disabled={loading}/></div></label><label className="field"><span>Senha</span><div className="input-icon"><i className="pi pi-lock"/><input type="password" autoComplete="current-password" placeholder="Digite sua senha" required value={form.senha} onChange={e=>setForm({...form,senha:e.target.value})} disabled={loading}/></div></label>{error && <Alert type="error">{error}</Alert>}<button className="btn btn-primary btn-full btn-large" disabled={loading}>{loading ? 'Verificando acesso...' : 'Entrar na plataforma'} <i className={`pi ${loading ? 'pi-spin pi-spinner' : 'pi-arrow-right'}`}/></button><p className="auth-switch">Ainda não tem uma conta? <Link to="/cadastro">Cadastre-se gratuitamente</Link></p></form></AuthFrame>;
}
export function Cadastro() {
  const [form, setForm] = useState({nome:'',email:'',senha:'',confirmar:''});
  const [loading,setLoading] = useState(false);
  const [error,setError] = useState('');
  const [success,setSuccess] = useState(false);
  async function submit(e){
    e.preventDefault();setError('');
    if(form.senha.length < 8){setError('Por segurança, utilize uma senha de pelo menos 8 caracteres.');return;}
    if(form.senha!==form.confirmar){setError('As senhas digitadas não coincidem.');return;}
    setLoading(true);
    try {await usuariosApi.cadastro(form.nome.trim(),form.email.trim().toLowerCase(),form.senha);setSuccess(true);}
    catch(err){setError(err.message);}
    finally{setLoading(false);}
  }
  return <AuthFrame title="Crie sua conta" subtitle="O primeiro passo para organizar o acesso às informações de saúde." image="/CadastroImage.png">{success ? <div className="auth-success"><div className="success-bubble"><i className="pi pi-check" /></div><h2>Conta criada com sucesso!</h2><p>Seu cadastro de usuário foi confirmado pelo servidor. Após entrar, confira os dados do perfil de paciente.</p><Link className="btn btn-primary btn-large" to="/login">Ir para o login <i className="pi pi-arrow-right"/></Link></div> : <form className="auth-form" onSubmit={submit}><label className="field"><span>Nome completo</span><input required minLength={3} maxLength={120} autoComplete="name" placeholder="Como podemos chamar você?" value={form.nome} onChange={e=>setForm({...form,nome:e.target.value})} disabled={loading}/></label><label className="field"><span>E-mail</span><input type="email" required autoComplete="email" placeholder="seuemail@exemplo.com" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} disabled={loading}/></label><div className="form-grid two"><label className="field"><span>Senha</span><input type="password" required minLength={8} autoComplete="new-password" placeholder="Mínimo 8 caracteres" value={form.senha} onChange={e=>setForm({...form,senha:e.target.value})} disabled={loading}/></label><label className="field"><span>Confirmar senha</span><input type="password" required autoComplete="new-password" placeholder="Repita a senha" value={form.confirmar} onChange={e=>setForm({...form,confirmar:e.target.value})} disabled={loading}/></label></div>{error && <Alert type="error">{error}</Alert>}<div className="form-hint"><i className="pi pi-shield" /> Seu cadastro é enviado diretamente ao backend; nenhum dado será salvo apenas no navegador.</div><button className="btn btn-primary btn-full btn-large" disabled={loading}>{loading ? 'Criando conta...' : 'Criar minha conta'} <i className="pi pi-arrow-right"/></button><p className="auth-switch">Já possui conta? <Link to="/login">Fazer login</Link></p></form>}</AuthFrame>;
}
export function AccessRedirect(){
  const { role, status } = useAuth();
  return status==='authenticated' ? <Navigate to={homeForRole(role)} replace /> : <div className="centered-screen"><p>Verificando sessão...</p></div>;
}
