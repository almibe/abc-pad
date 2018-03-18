import { h } from 'hyperapp'

export const LoadDialog = ({ }) => (
  <div className="modal">
    <div className="modal-background"></div>
    <div className="modal-card">
      <header className="modal-card-head">
        <p className="modal-card-title">Load Document</p>
        <button className="delete" aria-label="close"></button>
      </header>
      <section className="modal-card-body">
        Content
      </section>
      <footer className="modal-card-foot">
        <button className="button is-success">Load</button>
        <button className="button">Cancel</button>
      </footer>
    </div>
  </div>
)
