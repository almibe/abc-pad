import { h, app } from 'hyperapp'
import abcjs from 'abcjs/midi'
import 'bulma/css/bulma.css'
import './main.css'
import 'abcjs/abcjs-midi.css'
import 'font-awesome/css/font-awesome.css'

const state = {
  documentId: -1,
  saveButtonDisabled: false,
  status: ""
}

const actions = {
  saveOnClick: value => (state, actions) => {
    console.log("in saveOnClick")
  },
  managerOnClick: value => (state, actions) => {
    console.log("in managerOnClick")
  }
}

const view = (state, actions) => (
  <div id="app">
    <div class="container header">
      <div class="container">
        <div class="columns">
          <div class="column">
            <h1 class="title">ABC Editor</h1>
            <div class="field is-grouped">
              <div class="control">
                <button class="button" id="save" onclick={ actions.saveOnClick } disabled={state.saveButtonDisabled}>Save</button>
              </div>
              <div class="control">
                <button class="button" id="showManager" onclick={ actions.managerOnClick } >Manage Documents</button>
              </div>
              <div id="status">
                {state.status}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container" oncreate={ () => mounted() }>
      <div class="columns">
        <div class="column">
          <textarea id="abcEditor" class="textarea code" rows="20" ></textarea>
          <div id="warnings"></div>
          <div id="midi"></div>
        </div>
        <div class="column">
          <div id="canvas"></div>
        </div>
      </div>
    </div>
  </div>
)

app(state, actions, view, document.body)

function mounted() {
  console.log("in mounted")
  new abcjs.Editor('abcEditor', {
    canvas_id: 'canvas',
    generate_midi: true,
    midi_id: 'midi',
    generate_warnings: true,
    warnings_id: 'warnings'
  })
}
