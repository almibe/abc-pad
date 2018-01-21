import xs from 'xstream'
import { html } from 'snabbdom-jsx'

export function ABCEditor(sources) {
  const domSource = sources.DOM
  const state$ = sources.state

  const vdom$ = state$.map( state =>
    <div className="container">
      <div className="columns">
        <div className="column">
          <textarea id="abcEditor" className="textarea code" rows="20">{state.document}</textarea>
          <div id="warnings"></div>
        </div>
        <div className="column">
          <div id="canvas"></div>
        </div>
      </div>
    </div>
  )
  return { DOM: vdom$ }
}
