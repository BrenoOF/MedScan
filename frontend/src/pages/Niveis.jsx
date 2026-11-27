import { useEffect, useState } from 'react';
import { niveisApi } from '../api/niveis.js';
import { listFrom } from '../utils/normalize.js';
import { Alert, EmptyState, Loading, PageHeading } from '../components/Feedback.jsx';

export default function Niveis(){const[levels,setLevels]=useState([]);const[loading,setLoading]=useState(true);const[error,setError]=useState('');async function load(){setLoading(true);setError('');try{const data=await niveisApi.listar();setLevels(listFrom(data,['niveisAcesso','nivelAcesso','niveis','nivelAcessos']));}catch(e){setError(e.message);}finally{setLoading(false);}}useEffect(()=>{load();},[]);return <><PageHeading eyebrow="PERMISSÕES" title="Níveis de acesso" description="Consulte os níveis de acesso cadastrados." action={<button className="btn btn-outline" onClick={load}><i className="pi pi-refresh"/> Atualizar</button>}/>{error&&<Alert type="error">{error}</Alert>}<div className="card"><div className="card-heading"><h2>Níveis cadastrados</h2><p>Os níveis determinam as funcionalidades liberadas a cada perfil.</p></div>{loading?<Loading/>:levels.length?<div className="table-scroll"><table className="data-table"><thead><tr><th>Código</th><th>Descrição</th><th>Função</th></tr></thead><tbody>{levels.map((l,i)=><tr key={l.id??i}><td>#{l.id??'—'}</td><td><strong>{l.descricao||l.nome||'Não informado'}</strong></td><td>{l.funcao?.descricao||l.funcaoId||'Não informada'}</td></tr>)}</tbody></table></div>:<EmptyState icon="pi-lock" title="Nenhum nível encontrado" description="Não foram retornados níveis de acesso para a consulta."/>}</div><div style={{marginTop:18}}><Alert type="warning">O cadastro de novos níveis de acesso ainda não está disponível nesta página.</Alert></div></>}



