import { h, app } from 'hyperapp'
import Editor from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
import './main.scss'

const state = {
  id: null,
  name: "",
  document: ""
}

const actions = {
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
        "Document Name",
        h("input", { class: "" }, ""),
        h("button", { class: "" }, "Save"),
        h("button", { class: "" }, "Load")
      ])
    ]),
    h("section", { class: "section" }, [
      h("div", { class: "container" }, [
        h("textarea", { id: "abcEditor", class: "", cols="120", rows="20" }, ""),
        h("div", { id: "warnings" }, []),
        h("div", { id: "midi" }, []),
        h("div", { id: "canvas" }, [])
      ])
    ])
  ])

window.main = app(state, actions, view, document.body)

window.onload = function() {
  abc_editor = new ABCJS.Editor("abcEditor", { canvas_id: "canvas", midi_id:"midi", warnings_id:"warnings" });
}
