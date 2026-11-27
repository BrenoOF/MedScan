import { Link } from 'react-router-dom';

export function Alert({ type = 'info', children, title, onClose }) {
  return <div className={`alert alert-${type}`} role={type === 'error' ? 'alert' : 'status'}>
    <i className={`pi ${type === 'error' ? 'pi-exclamation-circle' : type === 'success' ? 'pi-check-circle' : type === 'warning' ? 'pi-info-circle' : 'pi-info-circle'}`} aria-hidden="true" />
    <div>{title && <strong>{title}</strong>}{children && <div>{children}</div>}</div>
    {onClose && <button type="button" className="icon-button" aria-label="Dispensar aviso" onClick={onClose}><i className="pi pi-times" aria-hidden="true" /></button>}
  </div>;
}
export function Loading({ label = 'Carregando informações...' }) {
  return <div className="loading-state" role="status"><i className="pi pi-spin pi-spinner" aria-hidden="true" /> {label}</div>;
}
export function EmptyState({ icon = 'pi-inbox', title = 'Nenhum registro encontrado', description, action, actionLabel }) {
  return <div className="empty-state"><div className="empty-icon" aria-hidden="true"><i className={`pi ${icon}`} /></div><h3>{title}</h3><p>{description}</p>{action && <Link className="btn btn-primary" to={action}>{actionLabel || 'Continuar'}</Link>}</div>;
}
export function PageHeading({ eyebrow, title, description, action }) {
  return <header className="page-heading"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1>{description && <p>{description}</p>}</div>{action && <div className="heading-action">{action}</div>}</header>;
}
export function PendingFeature({ feature, details }) {
  return <Alert type="warning" title={`${feature}: integração pendente`}>
    {details || 'Esta funcionalidade ainda não está disponível.'}
  </Alert>;
}
export function StatusBadge({ status }) {
  const s = String(status ?? '').toLowerCase();
  const style = /confirm|conclu|realiz/.test(s) ? 'good' : /cancel|recus/.test(s) ? 'bad' : 'waiting';
  return <span className={`status-badge ${style}`}>{status || 'Não informado'}</span>;
}



