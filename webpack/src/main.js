import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import { html } from 'snabbdom-jsx'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

function intent(dom) {

  return {}
}

function model(actions, http) {
//const state = {
//  id: null,
//  name: "",
//  document: ""
//}
}

function view(state$) {
//  h("section", { class: "section" }, [
//    h("section", { class: "section" }, [
//      h("div", { class: "container" }, [
//        h("h1", { class: "title" }, "ABC Editor")
//      ])
//    ]),
//    h("section", { class: "section" }, [
//      h("div", { class: "container" }, [
//        "Document Name: ",
//        h("input", { class: "input", type: "text" }, ""), " ",
//        h("button", { class: "button" }, "Save"), " ",
//        h("button", { class: "button" }, "Load")
//      ])
//    ]),
//    h("section", { class: "section" }, [
//      h("div", { class: "container" }, [
//        h("textarea", { id: "abcEditor", class: "textarea code" }, ""),
//        h("div", { id: "warnings" }, []),
//        h("div", { id: "canvas" }, [])
//      ])
//    ])
//  ])
}

function main(sources) {
  const actions = intent(sources.DOM)
  const state$ = model(actions, sources.HTTP)
  const vdom$ = view(state$)

  return {
    DOM: vdom$
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
