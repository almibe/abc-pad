import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import { html } from 'snabbdom-jsx'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

function intent(domSource, http) {
  const saveClick$ = //
  const loadClick$ = //
  const nameChange$ = //
  const documentChange$ = //

  const http$ = //

  return { http$ }
}

function model(actions) {
//const state = {
//  id: null,
//  name: "",
//  document: ""
//}
}

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
          <input class="input" type="text">
          <button class="button">Save</button>
          <button class="button">Load</button>
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
  const actions = intent(sources.DOM, sources.HTTP)
  const state$ = model(actions)
  const vdom$ = view(state$)

  return {
    DOM: vdom$,
    HTTP: actions['http$']
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
