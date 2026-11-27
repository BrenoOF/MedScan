import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { agendamentosApi } from '../api/agendamentos.js';
import { listFrom, formatDate, statusLabel } from '../utils/normalize.js';
import { Alert, EmptyState, Loading, PageHeading, StatusBadge } from '../components/Feedback.jsx';

const READY=true;
export default function Agendamentos(){
  const [items,setItems]=useState([]);
  const [loading,setLoading]=useState(READY);
  const [error,setError]=useState('');
  const [busy,setBusy]=useState('');
  const [notice,setNotice]=useState('');
  async function load(){if(!READY)return;setLoading(true);setError('');try{const res=await agendamentosApi.meus();setItems(listFrom(res,['agendamentos','solicitacoes','resultado']));}catch(e){setError(e.message);}finally{setLoading(false);}}
  useEffect(()=>{load();},[]);
  async function cancel(item){if(!window.confirm('Deseja solicitar o cancelamento deste agendamento?'))return;setBusy(String(item.id));setNotice('');try{await agendamentosApi.cancelar(item.id);setNotice('Cancelamento confirmado pela API.');await load();}catch(e){setError(e.message);}finally{setBusy('');}}
  return <><PageHeading eyebrow="ACOMPANHAMENTO" title="Meus agendamentos" description="Acompanhe as solicitações registradas em sua conta." action={<Link className="btn btn-primary" to="/app/agendar"><i className="pi pi-plus"/> Nova solicitação</Link>}/>
  {error&&<Alert type="error">{error}</Alert>}{notice&&<Alert type="success">{notice}</Alert>}
  {loading?<Loading/>:items.length?<div className="card table-scroll"><table className="data-table"><thead><tr><th>Código</th><th>Unidade / serviço</th><th>Data</th><th>Status</th><th>Ações</th></tr></thead><tbody>{items.map(item=><tr key={item.id}><td>#{item.id}</td><td><strong>{item?.unidadeSaude?.nome||item?.unidade?.nome||'UBS não informada'}</strong><small>{item?.tipoServico?.descricao||item?.tipoServico?.nome||'Serviço não informado'}</small></td><td>{formatDate(item.diaAgendamento||item.data)}</td><td><StatusBadge status={statusLabel(item.status)}/></td><td>{import.meta.env.VITE_AGENDAMENTO_CANCELAR_PATH&&/PENDENTE|CONFIRMADO/i.test(item.status||'')?<button className="btn btn-outline btn-small" disabled={busy===String(item.id)} onClick={()=>cancel(item)}>Cancelar</button>:<span className="muted">—</span>}</td></tr>)}</tbody></table></div>:<EmptyState icon="pi-calendar" title={READY?'Nenhum agendamento encontrado':'Acompanhamento indisponível'} description={READY?'Quando houver solicitações registradas, elas aparecerão aqui.':'Esta área será habilitada após a criação do endpoint de consulta por paciente.'} action="/app/ubs" actionLabel="Ver unidades"/>}</>;
}



