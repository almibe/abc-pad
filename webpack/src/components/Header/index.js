import xs from 'xstream'
import { html } from 'snabbdom-jsx'

export function Header(sources) {
  const domSource = sources.DOM
  const state$ = sources.state

  const vdom$ = state$.map( state =>
    <div className="container">
      <div className="container">
        <h1 className="title">ABC Editor</h1>
      </div>

      <div className="container">
        <div className="columns">
          <div className="column">
            <div className="field is-horizontal">
              <div className="field-label is-normal">
                <label className="label">Name</label>
              </div>
              <div className="field-body">
                <div className="field">
                  <p className="control">
                    <input id="name" className="input" type="text" value={state.name} />
                    <input id="id" className="input" type="hidden" value={state.id} />
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div className="column">
            <div className="field is-grouped">
              <div className="control">
                <button className="button" id="save">Save</button>
              </div>
              <div className="control">
                <button className="button" id="showLoad">Load</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
  return { DOM: vdom$ }
}
