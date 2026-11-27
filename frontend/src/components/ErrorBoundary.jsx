import React from 'react';

export default class ErrorBoundary extends React.Component {
  constructor(props) { super(props); this.state = { failed: false }; }
  static getDerivedStateFromError() { return { failed: true }; }
  componentDidCatch(error) {
    if (import.meta.env.DEV) console.error('Erro de renderização MedScan:', error);
  }
  render() {
    if (this.state.failed) return <div className="centered-screen"><section className="card narrow"><div className="empty-icon"><i className="pi pi-exclamation-triangle" /></div><h1>Não foi possível abrir esta página</h1><p>Ocorreu um problema inesperado ao carregar a interface. Nenhuma operação será considerada concluída por causa deste erro.</p><button className="btn btn-primary" type="button" onClick={() => window.location.reload()}>Recarregar página</button></section></div>;
    return this.props.children;
  }
}



