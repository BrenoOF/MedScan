import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { agendamentosApi } from '../api/agendamentos.js';
import { listFrom, formatDate, statusLabel } from '../utils/normalize.js';
import { Alert, EmptyState, Loading, PageHeading, PendingFeature, StatusBadge } from '../components/Feedback.jsx';
import { useAuth } from '../context/AuthContext.jsx';

const READY=Boolean(import.meta.env.VITE_AGENDAMENTO_UNIDADE_PATH);
const CAN_UPDATE=Boolean(import.meta.env.VITE_AGENDAMENTO_STATUS_PATH);
export default function Atendente({ dashboard=false }){
  const {user}=useAuth();
  const [items,setItems]=useState([]);
  const [loading,setLoading]=useState(READY);
  const [error,setError]=useState('');
  const [notice,setNotice]=useState('');
  const [filter,setFilter]=useState('todos');
  const [busy,setBusy]=useState('');
  async function load(){if(!READY)return;setLoading(true);setError('');try{const data=await agendamentosApi.porUnidade({});setItems(listFrom(data,['agendamentos','solicitacoes']));}catch(e){setError(e.message);}finally{setLoading(false);}}
  useEffect(()=>{load();},[]);
  const filtered=items.filter(item=>filter==='todos'||String(item.status||'').toUpperCase()===filter);
  async function update(item,status){setBusy(String(item.id));setError('');setNotice('');try{await agendamentosApi.atualizarStatus(item.id,status);setNotice('Status atualizado e confirmado pelo backend.');await load();}catch(e){setError(e.message);}finally{setBusy('');}}
  return <><PageHeading eyebrow="ÁREA DO ATENDENTE" title={dashboard?'Painel de atendimento':'Solicitações da unidade'} description={`Olá, ${user?.nome?.split(' ')[0]||'atendente'}. Acesse os registros autorizados da sua UBS.`} action={dashboard&&<Link className="btn btn-primary" to="/atendente/solicitacoes">Ver solicitações <i className="pi pi-arrow-right"/></Link>}/>
   <div className="stat-grid"><div className="stat-card"><span className="stat-icon blue"><i className="pi pi-inbox" /></span><span>Solicitações recebidas</span><strong>{READY?items.length:'—'}</strong></div><div className="stat-card"><span className="stat-icon yellow"><i className="pi pi-clock" /></span><span>Pendentes</span><strong>{READY?items.filter(x=>String(x.status||'').toUpperCase()==='PENDENTE').length:'—'}</strong></div><div className="stat-card"><span className="stat-icon turquoise"><i className="pi pi-check-circle" /></span><span>Confirmadas</span><strong>{READY?items.filter(x=>String(x.status||'').toUpperCase()==='CONFIRMADO').length:'—'}</strong></div></div>
   {!READY&&<PendingFeature feature="Painel do atendente" details="Falta um endpoint que retorne somente os agendamentos da UBS à qual o atendente autenticado pertence. Não serão utilizados dados fictícios nem IDs de UBS informados pelo navegador."/>}
   {error&&<Alert type="error">{error}</Alert>}{notice&&<Alert type="success">{notice}</Alert>}
   <div className="card"><div className="card-heading inline"><div><h2>Solicitações de atendimento</h2><p>Atualize ou consulte a situação de cada solicitação.</p></div><div className="compact-controls"><select aria-label="Filtrar solicitações por status" value={filter} onChange={e=>setFilter(e.target.value)}><option value="todos">Todos os status</option><option value="PENDENTE">Pendentes</option><option value="CONFIRMADO">Confirmados</option><option value="CANCELADO">Cancelados</option></select>{READY&&<button className="btn btn-outline" onClick={load}><i className="pi pi-refresh"/> Atualizar</button>}</div></div>{loading?<Loading/>:filtered.length?<div className="table-scroll"><table className="data-table"><thead><tr><th>Solicitação</th><th>Paciente</th><th>Data</th><th>Status</th><th>Ações</th></tr></thead><tbody>{filtered.map(item=><tr key={item.id}><td>#{item.id}</td><td>{item?.paciente?.usuario?.nome||item?.pacienteNome||'Paciente não informado'}</td><td>{formatDate(item.diaAgendamento)}</td><td><StatusBadge status={statusLabel(item.status)}/></td><td>{CAN_UPDATE&&String(item.status).toUpperCase()==='PENDENTE'?<button className="btn btn-small btn-primary" disabled={busy===String(item.id)} onClick={()=>update(item,'CONFIRMADO')}>Confirmar</button>:<span className="muted">—</span>}</td></tr>)}</tbody></table></div>:<EmptyState icon="pi-clipboard" title="Nenhuma solicitação para exibir" description={READY?'Não há registros para o filtro escolhido.':'A listagem estará disponível quando a API do atendente for criada.'}/>}</div>
   {READY&&!CAN_UPDATE&&<Alert type="warning">O endpoint de alteração de status ainda não foi configurado. Consultas podem funcionar, mas alterações ficam desabilitadas.</Alert>}
  </>;
}
