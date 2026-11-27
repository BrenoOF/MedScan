export function listFrom(response, keys = []) {
  if (Array.isArray(response)) return response;
  for (const key of keys) if (Array.isArray(response?.[key])) return response[key];
  if (Array.isArray(response?.content)) return response.content;
  if (Array.isArray(response?.data)) return response.data;
  return [];
}
export function ubsId(item) { return String(item?.cnes ?? item?.CNES ?? item?.codigo_cnes ?? item?.id ?? ''); }
export function ubsName(item) { return item?.nome ?? item?.nomeFantasia ?? item?.nome_fantasia ?? item?.razaoSocial ?? 'Unidade sem nome'; }
export function ubsCity(item) { return item?.cidade ?? item?.municipio?.nome ?? item?.municipio_nome ?? item?.endereco?.cidade ?? ''; }
export function serviceId(item) { return String(item?.id ?? item?.tipoServicoId ?? item?.codigo ?? ''); }
export function serviceName(item) { return item?.descricao ?? item?.nome ?? item?.tipoAgendamento?.descricao ?? item?.tipoAgendamento?.nome ?? `Serviço ${serviceId(item)}`; }
export function statusLabel(status) {
  return ({ PENDENTE: 'Pendente', CONFIRMADO: 'Confirmado', CONFIRMADA: 'Confirmado', CANCELADO: 'Cancelado', CANCELADA: 'Cancelado', CONCLUIDO: 'Concluído', REALIZADO: 'Realizado' })[String(status ?? '').toUpperCase()] || (status || 'Não informado');
}
export function parseRole(usuario) {
  const values = [usuario?.perfil, usuario?.papel, usuario?.role, usuario?.nivelAcesso?.descricao, usuario?.nivelAcesso?.nome, usuario?.nivelAcesso?.funcao?.descricao, usuario?.NivelAcesso?.descricao, usuario?.funcao?.descricao,
    ...(Array.isArray(usuario?.roles) ? usuario.roles : []), ...(Array.isArray(usuario?.authorities) ? usuario.authorities.map(r => typeof r === 'string' ? r : r?.authority) : [])];
  const text = values.filter(Boolean).join(' ').normalize('NFD').replace(/[\u0300-\u036f]/g,'').toUpperCase();
  if (/ADMINISTRADOR|\bADMIN\b/.test(text)) return 'administrador';
  if (/ATENDENTE/.test(text)) return 'atendente';
  return 'paciente';
}
export function firstName(usuario) { return (usuario?.nome || 'Usuário').trim().split(/\s+/)[0]; }
export function formatDate(value) {
  if (!value) return 'Não informada';
  const iso = String(value).slice(0,10);
  const parts = iso.split('-');
  return parts.length === 3 ? `${parts[2]}/${parts[1]}/${parts[0]}` : String(value);
}



