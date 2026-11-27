import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { unidadesApi } from '../api/unidades.js';
import { listFrom, ubsCity, ubsId, ubsName } from '../utils/normalize.js';
import { Alert, EmptyState, Loading, PageHeading } from '../components/Feedback.jsx';

export default function Unidades({ administrador = false }) {
  const [items,setItems] = useState([]);
  const [loading,setLoading] = useState(true);
  const [error,setError] = useState('');
  const [filter,setFilter] = useState('');
  const [services,setServices] = useState({});
  const [selected,setSelected] = useState('');
  const [servicesLoading,setServicesLoading] = useState(false);
  const [servicesError,setServicesError] = useState('');
  const navigate=useNavigate();
  async function load(){setLoading(true);setError('');try {const data=await unidadesApi.listar();setItems(listFrom(data,['unidadeSaude','unidadesSaude','unidades','resultado']));}catch(err){setError(err.message);}finally{setLoading(false);}}
  useEffect(()=>{load();},[]);
  const filtered=useMemo(()=>items.filter(item=>`${ubsName(item)} ${ubsId(item)} ${ubsCity(item)}`.toLowerCase().includes(filter.toLowerCase())),[items,filter]);
  async function showServices(cnes){
    if(selected===cnes){setSelected('');return;}
    setSelected(cnes);setServicesError('');
    if(services[cnes])return;
    setServicesLoading(true);
    try{const data=await unidadesApi.servicos(cnes);setServices(prev=>({...prev,[cnes]:listFrom(data,['tipoServico','tipoServicos','servicos'])}));}catch(err){setServicesError(err.message);}finally{setServicesLoading(false);}
  }
  return <><PageHeading eyebrow={administrador?'ADMINISTRAÇÃO':'REDE DE ATENDIMENTO'} title={administrador?'Unidades e serviços':'Encontre sua unidade de saúde'} description={administrador?'Consulte as unidades cadastradas. A edição de UBS depende de endpoints administrativos do backend.':'Pesquise pelo nome, município ou CNES e veja os serviços disponíveis.'} action={!administrador && <Link className="btn btn-primary" to="/app/agendar"><i className="pi pi-calendar-plus"/> Solicitar atendimento</Link>}/>
    <div className="card filter-panel"><label className="search-field"><i className="pi pi-search"/><input aria-label="Pesquisar unidades" placeholder="Pesquisar unidade, município ou código CNES..." value={filter} onChange={e=>setFilter(e.target.value)}/></label><button className="btn btn-outline" onClick={load} disabled={loading}><i className="pi pi-refresh"/> Atualizar</button></div>
    {administrador && <Alert type="info">Nesta página, você pode consultar as unidades e seus serviços. O cadastro e a edição de unidades ainda não estão disponíveis.</Alert>}
    {error && <Alert type="error">{error}</Alert>}
    {loading ? <Loading label="Buscando unidades cadastradas..."/> : filtered.length ? <div className="units-grid">{filtered.map((item,index)=>{
      const cnes=ubsId(item);return <article className="card unit-card" key={cnes || index}><div className="unit-icon"><i className="pi pi-building" /></div><div className="unit-body"><span className="meta-label">UNIDADE DE SAÚDE</span><h3>{ubsName(item)}</h3><p><i className="pi pi-map-marker" /> {ubsCity(item) || 'Município não informado'}</p><p><i className="pi pi-id-card" /> CNES: {cnes || 'Não informado'}</p></div><div className="unit-actions"><button className="btn btn-outline" onClick={()=>showServices(cnes)} disabled={!cnes}><i className="pi pi-list"/> {selected===cnes?'Ocultar serviços':'Ver serviços'}</button>{!administrador && <button className="btn btn-primary" disabled={!cnes} onClick={()=>navigate('/app/agendar',{state:{unidadeId:cnes}})}>Selecionar <i className="pi pi-arrow-right"/></button>}</div>{selected===cnes && <div className="unit-services">{servicesLoading?<Loading label="Carregando serviços..."/>:servicesError?<Alert type="error">{servicesError}</Alert>:(services[cnes]?.length?<ul>{services[cnes].map((s,i)=><li key={s?.id??i}>{s?.descricao||s?.nome||s?.tipoAgendamento?.descricao||`Serviço ${s?.id??i}`}</li>)}</ul>:<p className="muted">Nenhum serviço cadastrado para esta unidade.</p>)}</div>}</article>;
    })}</div>:<EmptyState icon="pi-map-marker" title="Nenhuma unidade encontrada" description={filter?'Tente outro termo para a pesquisa.':'Não há unidades disponíveis na resposta da API.'}/>}
  </>;
}



