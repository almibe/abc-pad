import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import { html } from 'snabbdom-jsx'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

function view(state$) {
  return state$.map( state =>
    <section className="section">
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
    </section>
  )
}

function main(sources) {
  const domSource = sources.DOM
  const httpSource = sources.HTTP

  const saveClick$ = domSource.select('#save').events('click')
  const showLoadClick$ = domSource.select('#showLoad').events('click')
  const loadClick$ = domSource.select('#load').events('click')
  const nameChange$ = domSource.select('#name').events('change')
  const idChange$ = domSource.select('#id').events('change')
  const documentChange$ = domSource.select('#abcEditor').events('change')

  const nameValue$ = nameChange$.map(e => e.target.value)
  const idValue$ = idChange$.map(e => e.target.value)
  const documentValue$ = documentChange$.map(e => e.target.value)
  const dialogVisible$ = showLoadClick$.fold((prev, curr) => !curr, true)

  const state$ = xs.combine(idValue$, nameValue$, documentValue$, dialogVisible$)
    .map(([id, name, document, showDialog]) => {
      return { id, name, document, showDialog }
    })

  const saveRequest$ = saveClick$.compose({
    return state$.map({
      //TODO map to http save request
    })
  })

  const saveResponse = httpSource.select('save-document') //TODO finish

  const loadRequest$ = loadClick$.compose({
    return state$.map({
      //TODO map to http load request
    })
  })

  const loadResponse = httpSource.select('load-document') //TODO finish

  const vdom$ = view(state$)

  return {
    DOM: vdom$,
    HTTP: xs.merge(saveRequest$, loadRequest$)
  }
}

const drivers = {
  DOM: makeDOMDriver('#app'),
  HTTP: makeHTTPDriver()
}

run(main, drivers)

window.onload = function() {
  const abcEditor = new ABCJS.Editor("abcEditor",
    { canvas_id: "canvas", generate_midi: false, warnings_id: "warnings" })
}
