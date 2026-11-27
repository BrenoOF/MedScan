import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { usuariosApi } from '../api/usuarios.js';
import { niveisApi } from '../api/niveis.js';
import { listFrom } from '../utils/normalize.js';
import { Alert, EmptyState, Loading, PageHeading } from '../components/Feedback.jsx';

export function Usuarios(){
  const [items,setItems]=useState([]);
  const [filter,setFilter]=useState('todos');
  const [term,setTerm]=useState('');
  const [loading,setLoading]=useState(true);
  const [error,setError]=useState('');
  async function load(f=filter,v=term){setLoading(true);setError('');try{const data=await usuariosApi.listar(f,v.trim());setItems(listFrom(data,['usuarios','usuarioList','usuario','resultado']));}catch(err){setItems([]);setError(err.message);}finally{setLoading(false);}}
   useEffect(()=>{load('todos','');},[]);
  function submit(e){e.preventDefault();if(filter!=='todos'&&!term.trim()){setError('Informe um termo para o filtro selecionado.');return;}load(filter,term);}
  return <><PageHeading eyebrow="GESTÃO DE ACESSO" title="Usuários cadastrados" description="Pesquise registros diretamente na API do MedScan." action={<Link className="btn btn-primary" to="/administrador/usuarios/novo"><i className="pi pi-user-plus"/> Novo usuário</Link>}/><form className="card filter-panel" onSubmit={submit}><select aria-label="Filtro de usuários" value={filter} onChange={e=>setFilter(e.target.value)}><option value="todos">Todos os usuários</option><option value="nome">Nome</option><option value="email">E-mail</option><option value="id">Código</option></select><label className="search-field"><i className="pi pi-search"/><input aria-label="Termo de busca" disabled={filter==='todos'} placeholder={filter==='todos'?'Listando todos os usuários':'Digite sua pesquisa...'} value={term} onChange={e=>setTerm(e.target.value)}/></label><button className="btn btn-primary" disabled={loading}>Pesquisar</button></form>{error&&<Alert type="error">{error}</Alert>}
  <div className="card"><div className="card-heading"><h2>Resultado da consulta</h2><p>As informações apresentadas vêm do banco de dados por meio da API.</p></div>{loading?<Loading/>:items.length?<div className="table-scroll"><table className="data-table"><thead><tr><th>ID</th><th>Usuário</th><th>E-mail</th><th>Nível de acesso</th><th>Situação</th></tr></thead><tbody>{items.map((u,i)=><tr key={u.id||i}><td>#{u.id||'—'}</td><td><strong>{u.nome||'Não informado'}</strong></td><td>{u.email||'Não informado'}</td><td>{u.nivelAcesso?.descricao||u.NivelAcesso?.descricao||'Não informado'}</td><td><span className="status-badge waiting">Consulta</span></td></tr>)}</tbody></table></div>:<EmptyState icon="pi-users" title="Nenhum usuário encontrado" description="Nenhum registro foi retornado pela API para essa pesquisa."/>}</div><div style={{marginTop:16}}><Alert type="warning">A edição ainda não possui endpoint no backend. A exclusão foi temporariamente bloqueada na interface até corrigir as regras de cascata e preservação de histórico.</Alert></div></>;
}
export function UsuarioNovo(){
  const navigate=useNavigate();
  const [levels,setLevels]=useState([]);
  const [loadingLevels,setLoadingLevels]=useState(true);
  const [form,setForm]=useState({nome:'',email:'',senha:'',nivelAcessoId:''});
  const [error,setError]=useState('');const [busy,setBusy]=useState(false);
  useEffect(()=>{niveisApi.listar().then(data=>setLevels(listFrom(data,['niveisAcesso','nivelAcesso','niveis','nivelAcessos']))).catch(err=>setError(`Não foi possível carregar os níveis: ${err.message}`)).finally(()=>setLoadingLevels(false));},[]);
  async function submit(e){e.preventDefault();setError('');if(!form.nivelAcessoId){setError('Escolha um nível de acesso válido.');return;}setBusy(true);try{await usuariosApi.criarAdministrativo({nome:form.nome.trim(),email:form.email.trim().toLowerCase(),senha:form.senha,nivelAcessoId:Number(form.nivelAcessoId)});navigate('/administrador/usuarios',{replace:true});}catch(err){setError(err.message);}finally{setBusy(false);}}
  return <><PageHeading eyebrow="GESTÃO DE ACESSO" title="Cadastrar usuário" description="Criação administrativa com nível de acesso validado no backend."/><div className="card form-panel"><form onSubmit={submit}><div className="form-grid two"><label className="field"><span>Nome completo</span><input required minLength={3} value={form.nome} onChange={e=>setForm({...form,nome:e.target.value})}/></label><label className="field"><span>E-mail</span><input required type="email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})}/></label><label className="field"><span>Senha inicial</span><input required type="password" minLength={8} autoComplete="new-password" value={form.senha} onChange={e=>setForm({...form,senha:e.target.value})}/></label><label className="field"><span>Nível de acesso</span><select required disabled={loadingLevels||!levels.length} value={form.nivelAcessoId} onChange={e=>setForm({...form,nivelAcessoId:e.target.value})}><option value="">{loadingLevels?'Carregando níveis...':'Selecione um nível'}</option>{levels.map((x,i)=><option value={x.id} key={x.id??i}>{x.descricao||x.nome||`Nível ${x.id}`}</option>)}</select></label></div>{error&&<Alert type="error">{error}</Alert>}<Alert type="info">Este formulário utiliza /usuario/usuarioNew, destinado somente a perfis com permissão administrativa.</Alert><div className="form-actions"><Link to="/administrador/usuarios" className="btn btn-outline">Cancelar</Link><button className="btn btn-primary" disabled={busy||!levels.length}>{busy?'Salvando...':'Cadastrar usuário'}</button></div></form></div></>;
}



