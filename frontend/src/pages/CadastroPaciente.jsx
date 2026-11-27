import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { pacientesApi } from '../api/pacientes.js';
import { Alert } from '../components/Feedback.jsx';

const INITIAL = {
  nome: '', email: '', senha: '', confirmarSenha: '', cpf: '',
  dataNascimento: '', genero: '', logradouro: '', numero: '', bairro: '',
  cidade: '', uf: '', cep: '', complemento: '',
};

export default function CadastroPaciente() {
  const [step, setStep] = useState(1);
  const [form, setForm] = useState(INITIAL);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const update = (key, value) => {
    setForm((current) => ({ ...current, [key]: value }));
    setError('');
  };
  function validate() {
    if (step === 1 && (!form.nome.trim() || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim()) || form.senha.length < 6 || form.senha !== form.confirmarSenha)) {
      setError('Informe nome, e-mail e uma senha confirmada com pelo menos 6 caracteres.');
      return false;
    }
    const nascimento = form.dataNascimento ? new Date(`${form.dataNascimento}T00:00:00`) : null;
    if (step === 2 && (!/^\d{11}$/.test(form.cpf.replace(/\D/g, '')) || !nascimento || nascimento > new Date() || !form.genero)) {
      setError('Informe CPF, data de nascimento e gênero.');
      return false;
    }
    if (step === 3 && (!form.logradouro.trim() || !form.numero.trim() || !form.bairro.trim() ||
      !form.cidade.trim() || form.uf.trim().length !== 2 || !form.cep.trim())) {
      setError('Preencha os campos obrigatórios do endereço.');
      return false;
    }
    return true;
  }
  async function submit(event) {
    event.preventDefault();
    if (!validate() || loading) return;
    setLoading(true);
    try {
      const response = await pacientesApi.cadastroCompleto({
        nome: form.nome.trim(), email: form.email.trim().toLowerCase(), senha: form.senha,
        cpf: form.cpf.trim(), dataNascimento: form.dataNascimento, genero: form.genero,
        logradouro: form.logradouro.trim(), numero: form.numero.trim(), bairro: form.bairro.trim(),
        cidade: form.cidade.trim(), uf: form.uf.trim().toUpperCase(), cep: form.cep.trim(),
        complemento: form.complemento.trim(),
      });
      if (!response?.paciente?.id) throw new Error('O servidor não confirmou o cadastro.');
      navigate('/login', { replace: true });
    } catch (cause) {
      setError(cause.message || 'Não foi possível concluir o cadastro.');
    } finally {
      setLoading(false);
    }
  }
  const field = (label, key, type = 'text', required = true) => (
    <label className="field"><span>{label}</span><input type={type} required={required} value={form[key]} disabled={loading} onChange={(event) => update(key, event.target.value)} /></label>
  );
  return <div className="auth-page"><div className="auth-panel"><div className="auth-top"><Link to="/"><img src="/a.png" alt="MedScan" /></Link><Link className="back-link" to="/">Voltar ao início</Link></div><div className="auth-body"><div className="auth-heading"><span className="eyebrow">CADASTRO DE PACIENTE</span><h1>Etapa {step} de 3</h1><p>Preencha seus dados para criar seu acesso.</p></div><form className="auth-form" onSubmit={step === 3 ? submit : (event) => { event.preventDefault(); if (validate()) setStep(step + 1); }}>{error && <Alert type="error">{error}</Alert>}{step === 1 && <>{field('Nome completo', 'nome')}{field('E-mail', 'email', 'email')}{field('Senha', 'senha', 'password')}{field('Confirmar senha', 'confirmarSenha', 'password')}</>}{step === 2 && <>{field('CPF', 'cpf')}{field('Data de nascimento', 'dataNascimento', 'date')} {field('Gênero', 'genero')}</>}{step === 3 && <>{field('Logradouro', 'logradouro')}{field('Número', 'numero')}{field('Bairro', 'bairro')}{field('Cidade', 'cidade')}{field('UF', 'uf')}{field('CEP', 'cep')}{field('Complemento', 'complemento', 'text', false)}</>}<div className="form-actions">{step > 1 && <button type="button" className="btn btn-outline" onClick={() => { setError(''); setStep(step - 1); }} disabled={loading}>Voltar</button>}<button className="btn btn-primary" disabled={loading}>{loading ? 'Enviando...' : step === 3 ? 'Concluir cadastro' : 'Próximo'}</button></div><p className="auth-switch">Já possui conta? <Link to="/login">Entrar</Link></p></form></div></div>
    <aside className="auth-aside">
      <img src="/CadastroImage.png" alt="Ilustração da plataforma MedScan" />
      <div className="auth-quote"><i className="pi pi-heart-fill" aria-hidden="true" /><h2>Conectando pessoas a um cuidado melhor.</h2><p>Informação organizada, acessível e disponível no momento certo.</p></div>
    </aside>
  </div>;
}



