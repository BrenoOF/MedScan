import { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { agendamentosApi } from '../api/agendamentos.js';
import { unidadesApi } from '../api/unidades.js';
import { listFrom, ubsId, ubsName, serviceId, serviceName } from '../utils/normalize.js';
import { Alert, Loading, PageHeading } from '../components/Feedback.jsx';

const INITIAL = { unidadeId:'', tipoServicoId:'', diaAgendamento:'', horaAgendamento:'', escalaDor:'0', sintomas:'', queixa:'', classificacaoRisco:'NAO_URGENTE' };
const READY = true;

export default function Agendar(){
  const location=useLocation();
  const [form,setForm]=useState({...INITIAL,unidadeId:location.state?.unidadeId||''});
  const [unidades,setUnidades]=useState([]);
  const [servicos,setServicos]=useState([]);
  const [loading,setLoading]=useState(true);
  const [servicosLoading,setServicosLoading]=useState(false);
  const [submitting,setSubmitting]=useState(false);
  const [error,setError]=useState('');
  const [success,setSuccess]=useState('');
  const [step,setStep]=useState(1);
  useEffect(()=>{let mounted=true;unidadesApi.listar().then(r=>{if(mounted)setUnidades(listFrom(r,['unidadeSaude','unidadesSaude','unidades']));}).catch(e=>{if(mounted)setError(e.message);}).finally(()=>{if(mounted)setLoading(false);});return()=>{mounted=false;};},[]);
  useEffect(()=>{
    if(!form.unidadeId){setServicos([]);return;}
    let mounted=true;setServicosLoading(true);setServicos([]);
    unidadesApi.servicos(form.unidadeId).then(r=>{if(mounted)setServicos(listFrom(r,['tipoServico','tipoServicos','servicos']));}).catch(e=>{if(mounted)setError(e.message);}).finally(()=>{if(mounted)setServicosLoading(false);});
    return()=>{mounted=false;};
  },[form.unidadeId]);
  function update(key,val){setForm(prev=>({...prev,[key]:val,...(key==='unidadeId'?{tipoServicoId:''}:{})}));setError('');}
  function next(e){e.preventDefault();if(!form.unidadeId||!form.tipoServicoId||!form.queixa.trim()){setError('Selecione a UBS, o serviço e descreva brevemente sua necessidade.');return;}setError('');setStep(2);}
  async function submit(e){e.preventDefault();setError('');setSuccess('');if(!READY){setError('A API de agendamentos ainda não está disponível. Nenhuma solicitação foi registrada.');return;}
    if(!form.diaAgendamento||!form.horaAgendamento){setError('Informe a data e o horário desejados.');return;}
    const today=new Date();today.setHours(0,0,0,0);const selectedDate=new Date(`${form.diaAgendamento}T00:00:00`);if(selectedDate<today){setError('Escolha uma data de hoje em diante.');return;}
    setSubmitting(true);
    try {
      const payload={unidadeCnes:Number(form.unidadeId),tipoServicoId:Number(form.tipoServicoId),diaAgendamento:form.diaAgendamento,horaAgendamento:form.horaAgendamento,escalaDor:Number(form.escalaDor),sintomas:form.sintomas.trim(),descricaoBuscaAtiva:form.queixa.trim(),classificacaoRisco:form.classificacaoRisco};
      const response=await agendamentosApi.criar(payload);
      if(!response?.agendamento?.id){throw new Error('O backend não confirmou o registro. Consulte o suporte antes de tentar novamente.');}
      setSuccess('Solicitação confirmada pelo servidor. Você pode acompanhar o andamento na seção Meus agendamentos.');setStep(3);
    }catch(e){setError(e.message);}finally{setSubmitting(false);}
  }
  return <><PageHeading eyebrow="SOLICITA•fO" title="Solicitar atendimento" description="Preencha seus dados em etapas para facilitar a conferência das informações."/><div className="stepper"><div className={`step ${step>=1?'active':''}`}><span>1</span> Escolha do serviço</div><div className="step-line"/><div className={`step ${step>=2?'active':''}`}><span>2</span> Informações da solicitação</div><div className="step-line"/><div className={`step ${step===3?'active':''}`}><span>3</span> Confirmação</div></div>
  {error && <Alert type="error" onClose={()=>setError('')}>{error}</Alert>}
  {step===3 ? <div className="card submit-complete"><div className="success-bubble"><i className="pi pi-check"/></div><h2>Solicitação registrada</h2><p>{success}</p><Link className="btn btn-primary" to="/app/agendamentos">Acompanhar solicitação</Link></div> : <div className="card form-panel"><form onSubmit={step===1?next:submit}>
   {step===1?<><div className="form-section-title"><div className="feature-icon"><i className="pi pi-building"/></div><div><h2>Onde deseja ser atendido?</h2><p>Escolha uma unidade e o serviço que está procurando.</p></div></div>{loading?<Loading label="Carregando unidades..."/>:<div className="form-grid"><label className="field"><span>Unidade de saúde <b>*</b></span><select required value={form.unidadeId} onChange={e=>update('unidadeId',e.target.value)}><option value="">Selecione a UBS</option>{unidades.map(u=><option key={ubsId(u)} value={ubsId(u)}>{ubsName(u)} — CNES {ubsId(u)}</option>)}</select></label><label className="field"><span>Serviço desejado <b>*</b></span><select required disabled={!form.unidadeId||servicosLoading} value={form.tipoServicoId} onChange={e=>update('tipoServicoId',e.target.value)}><option value="">{servicosLoading?'Carregando serviços...':'Selecione o serviço'}</option>{servicos.map(s=><option key={serviceId(s)} value={serviceId(s)}>{serviceName(s)}</option>)}</select></label></div>}<label className="field"><span>Descreva brevemente o motivo da solicitação <b>*</b></span><textarea required minLength={5} maxLength={500} rows={4} placeholder="Descreva o que você precisa informar à unidade..." value={form.queixa} onChange={e=>update('queixa',e.target.value)}/><small>Evite informações que não sejam necessárias para o atendimento.</small></label></>:<><div className="form-section-title"><div className="feature-icon"><i className="pi pi-calendar"/></div><div><h2>Informações complementares</h2><p>Confira sua solicitação antes do envio.</p></div></div><div className="form-grid two"><label className="field"><span>Data desejada <b>*</b></span><input required type="date" min={new Date(Date.now()-new Date().getTimezoneOffset()*60000).toISOString().slice(0,10)} value={form.diaAgendamento} onChange={e=>update('diaAgendamento',e.target.value)}/></label><label className="field"><span>Horário desejado <b>*</b></span><input type="time" required value={form.horaAgendamento} onChange={e=>update('horaAgendamento',e.target.value)}/></label></div><label className="field"><span>Sintomas (quando aplicável)</span><textarea rows={3} maxLength={500} placeholder="Descreva os sintomas, se houver..." value={form.sintomas} onChange={e=>update('sintomas',e.target.value)}/></label><label className="field"><span>Escala de dor: <strong>{form.escalaDor}/10</strong></span><input type="range" min="0" max="10" value={form.escalaDor} onChange={e=>update('escalaDor',e.target.value)}/><small>0 significa ausência de dor; 10 representa dor muito intensa.</small></label><div className="review-box"><strong>Resumo da solicitação</strong><p>UBS: {ubsName(unidades.find(u=>ubsId(u)===form.unidadeId))}</p><p>Serviço: {serviceName(servicos.find(s=>serviceId(s)===form.tipoServicoId))}</p><p>Motivo: {form.queixa}</p></div><Alert type="info">A data e o horário são preferências e dependem de confirmação da unidade. Não há reserva garantida antes da resposta do backend.</Alert></>}
   <div className="form-actions">{step===2?<button type="button" className="btn btn-outline" onClick={()=>{setStep(1);setError('');}}>Voltar</button>:<Link to="/app/ubs" className="btn btn-outline">Voltar às unidades</Link>}<button className="btn btn-primary" disabled={loading||submitting||(step===2&&!READY)}>{step===1?'Continuar':submitting?'Enviando...':READY?'Enviar solicitação':'Aguardando integração'} <i className="pi pi-arrow-right"/></button></div>
  </form></div>}</>;
}



