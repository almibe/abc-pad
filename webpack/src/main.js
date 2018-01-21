import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import { html } from 'snabbdom-jsx'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import { ABCEditor } from './components/ABCEditor'
import { Header } from './components/Header'
import { LoadDialog } from './components/LoadDialog'
import './main.scss'

function view(header, abcEditor, loadDialog) {
  return xs.combine(header.DOM, abcEditor.DOM, loadDialog.DOM).map(([headerDOM, abcEditorDOM, loadDialogDOM]) =>
    <section className="section">
      {headerDOM}
      {abcEditorDOM}
      {loadDialogDOM}
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

  const abcEditor = ABCEditor({ DOM: sources.DOM, state: state$ })
  const header = Header({ DOM: sources.DOM, state: state$ })
  const loadDialog = LoadDialog({ DOM: sources.DOM, state: state$ })

  const saveRequest$ = saveClick$.compose((input) =>
    xs.never()
//    state$.take(1).map(state => {
//      url: 'documents',
//      method: 'POST',
//      category: 'save-document',
//      send: { id: state.id, name: state.name, document: state.document }
//    })
  )

  const saveResponse$ = httpSource.select('save-document').flatten() //TODO finish

  const fetchAllRequest$ = dialogVisible$.filter(i => i).compose((input) =>
    xs.of({
      url: 'documents',
      category: 'fetch-all'
    })
  )

  const fetchAllResponse$ = httpSource.select('fetch-all').flatten() //TODO finish

  const loadRequest$ = loadClick$.compose((input) =>
    xs.never()
//    state$.take(1).map( state => {
//      url: 'documents',
//      category: 'load-document',
//      query: { id: state.idToLoad } //TODO state.idToLoad doesn't exist yet
//    })
  )

  const loadResponse$ = httpSource.select('load-document').flatten() //TODO finish

  const vdom$ = view(header, abcEditor, loadDialog)

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
