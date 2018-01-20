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
  const dialogVisible$ = showLoadClick$.fold((prev, curr) => !curr, false)

  const state$ = xs.combine(idValue$, nameValue$, documentValue$, dialogVisible$)
    .map(([id, name, document, showDialog]) => {
      return { id, name, document, showDialog }
    }).startWith({id: 0, name: "", document: "", showDialog: false})

  const saveRequest$ = saveClick$.compose((input) =>
    state$.take(1).map(state => {
      url: 'documents',
      method: 'POST',
      category: 'save-document',
      send: { id: state.id, name: state.name, document: state.document }
    })
  )

  const saveResponse$ = httpSource.select('save-document').flatten() //TODO finish

  const fetchAllRequest$ = dialogVisible$.filter(i => i).compose((input) =>
    state$.take(1).map(state => {
      url: 'documents',
      category: 'fetch-all'
    })
  )

  const fetchAllResponse$ = httpSource.select('fetch-all').flatten() //TODO finish

  const loadRequest$ = loadClick$.compose((input) =>
    state$.take(1).map( state =>
      {} //TODO map to http load request
    )
  )

  const loadResponse$ = httpSource.select('load-document').flatten() //TODO finish

  const vdom$ = view(state$)

  return {
    DOM: vdom$,
    HTTP: xs.merge(saveRequest$, loadRequest$, fetchAllRequest$)
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
