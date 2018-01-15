import xs from 'xstream'
import {run} from '@cycle/run'
import { makeDOMDriver } from '@cycle/dom'
import { makeHTTPDriver } from '@cycle/http'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

function main(sources) {

}

const drivers = {
  DOM: makeDOMDriver('#app'),
  HTTP: makeHTTPDriver()
}

run(main, drivers)

//OLD HYPERAPP CODE
const state = {
  id: null,
  name: "",
  document: ""
}

const actions = {
  save: value => state =>
}

const view = (state, actions) =>
  h("section", { class: "section" }, [
    h("section", { class: "section" }, [
      h("div", { class: "container" }, [
        h("h1", { class: "title" }, "ABC Editor")
      ])
    ]),
    h("section", { class: "section" }, [
      h("div", { class: "container" }, [
        "Document Name: ",
        h("input", { class: "input", type: "text" }, ""), " ",
        h("button", { class: "button" }, "Save"), " ",
        h("button", { class: "button" }, "Load")
      ])
    ]),
    h("section", { class: "section" }, [
      h("div", { class: "container" }, [
        h("textarea", { id: "abcEditor", class: "textarea code" }, ""),
        h("div", { id: "warnings" }, []),
        h("div", { id: "canvas" }, [])
      ])
    ])
  ])

window.main = app(state, actions, view, document.body)

window.onload = function() {
  const abcEditor = new ABCJS.Editor("abcEditor",
    { canvas_id: "canvas", generate_midi: false, warnings_id: "warnings" })
}
