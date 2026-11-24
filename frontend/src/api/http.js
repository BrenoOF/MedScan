/** Todas as requisições à API Spring Boot passam por este módulo. */
const API_BASE = (import.meta.env.VITE_API_BASE || '/MedScan').replace(/\/$/, '');

export class ApiError extends Error {
  constructor(message, status = 0, details = null) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.details = details;
  }
}

export class FeatureUnavailableError extends ApiError {
  constructor(label) {
    super(`${label} ainda depende da disponibilização do endpoint correspondente no backend.`, 501);
    this.name = 'FeatureUnavailableError';
  }
}

export function optionalPath(path, label) {
  if (!path?.trim()) throw new FeatureUnavailableError(label);
  if (!path.startsWith('/') || path.startsWith('//') || path.includes('://')) {
    throw new ApiError('Configuração de caminho da API inválida. Consulte o arquivo .env.', 0);
  }
  return path;
}

function cookie(name) {
  const encoded = document.cookie.split('; ').find((part) => part.startsWith(`${name}=`));
  return encoded ? decodeURIComponent(encoded.substring(name.length + 1)) : '';
}

export async function apiRequest(path, { method = 'GET', body, signal } = {}) {
  const safePath = path.startsWith('/') && !path.startsWith('//') ? path : `/${path}`;
  const token = cookie('XSRF-TOKEN');
  const headers = { Accept: 'application/json' };
  if (method !== 'GET') {
    headers['Content-Type'] = 'application/json';
    if (token) headers['X-XSRF-TOKEN'] = token;
  }
  let response;
  try {
    response = await fetch(`${API_BASE}${safePath}`, {
      method,
      credentials: 'include',
      headers,
      signal,
      ...(method !== 'GET' ? { body: JSON.stringify(body ?? {}) } : {}),
    });
  } catch (error) {
    if (error?.name === 'AbortError') throw error;
    throw new ApiError('Não foi possível conectar ao servidor. Confirme se o Spring Boot está em execução.', 0);
  }

  if (response.status === 204) return null;
  const raw = await response.text();
  let data = null;
  if (raw) {
    try { data = JSON.parse(raw); }
    catch { data = { message: response.ok ? raw : '' }; }
  }
  if (!response.ok) {
    const fallbacks = {
      400: 'Dados inválidos. Confira os campos e tente novamente.',
      401: 'Sua sessão expirou ou suas credenciais são inválidas.',
      403: 'Você não possui permissão para realizar esta operação.',
      404: 'Recurso não encontrado. Verifique se a API está atualizada.',
      409: 'Não foi possível concluir: existe um conflito com os dados cadastrados.',
      500: 'Ocorreu um erro no servidor. Tente novamente mais tarde.',
    };
    throw new ApiError(data?.message || data?.mensagem || fallbacks[response.status] || `Erro HTTP ${response.status}.`, response.status, data);
  }
  return data;
}

export const post = (path, body = {}, options = {}) => apiRequest(path, { method: 'POST', body, ...options });
export const get = (path, options = {}) => apiRequest(path, { method: 'GET', ...options });
