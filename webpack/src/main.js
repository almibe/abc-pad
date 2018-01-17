import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import { html } from 'snabbdom-jsx'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

function view(state$) {
  return xs.of(
    <section class="section">
      <section class="section">
        <div class="container">
          <h1 class="title">ABC Editor</h1>
        </div>
      </section>
      <section class="section">
        <div class="container">
          Document Name:
          <input id="name" class="input" type="text" />
          <button class="button" id="save">Save</button>
          <button class="button" id="load">Load</button>
        </div>
      </section>
      <section class="section">
        <div class="container">
          <textarea id="abcEditor" class="textarea code"></textarea>
          <div id="warnings"></div>
          <div id="canvas"></div>
        </div>
      </section>
    </section>
  )
}

function main(sources) {
  const domSource = sources.DOM
  const httpSource = sources.HTTP

//  const saveClick$ = domSource.select('#save').events('click')
//  const loadClick$ = domSource.select('#load').events('click')
//  const nameChange$ = domSource.select('#name').events('change')
//  const documentChange$ = domSource.select('#abcEditor').events('change')
//
//  const nameValue$ = nameChange$.map(e => e.target.value)
//  const documentValue$ = documentChange$.map(e => e.target.value)
//
//  const saveRequest$ = saveClick$.map({
//    name: nameValue$.value,
//    document: documentValue$.value
//  }) //TODO map to http request
//
//  const dialogVisible$ = loadClick$ //
//
//  //TODO read httpSource
//
//  const loadRequest$ = null //TODO
//
  const state$ = xs.of({})
  const vdom$ = view(state$)

  return {
    DOM: vdom$,
    HTTP: xs.empty()//xs.merge(saveRequest$, loadRequest$)
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
