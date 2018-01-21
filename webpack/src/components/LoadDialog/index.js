import xs from 'xstream'
import { html } from 'snabbdom-jsx'

export function LoadDialog(sources) {
  const domSource = sources.DOM
  const state$ = sources.state

  const vdom$ = state$.map( state =>
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
  return { DOM: vdom$ }
}
