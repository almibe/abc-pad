import { h, app } from 'hyperapp'
import abcjs from 'abcjs/midi'
import axios from 'axios'
import page from 'page'
import 'bulma/css/bulma.css'
import './main.css'
import 'abcjs/abcjs-midi.css'
import 'font-awesome/css/font-awesome.css'

const state = {
  documentId: -1,
  documents: [],
  saveButtonDisabled: false,
  status: '',
  defaultText: 'T: Untitled\nC: Unknown\nK: ',
  dialogState: ''
}

const actions = {
  saveOnClick: value => (state, actions) => {
    const abcEditor = document.getElementById('abcEditor')
    if (state.documentId > 0) {
      axios.patch('documents/'+state.documentId, {
        document: abcEditor.value
      })
      .then((response) => {
        actions.updateStatus("Saved document " + state.documentId)
      })
      .catch((error) => {
        actions.updateStatus("Error saving document " + state.documentId)
      });
    } else {
      axios.post('documents/', {
        document: abcEditor.value
      })
      .then((response) => {
        actions.updateStatus("Saved new document " + response.data.id)
      })
      .catch((error) => {
        actions.updateStatus("Error saving new document.")
      });
    }
  },
  updateStatus: value => (state, actions) => ({
    status: value
  }),
  managerOnClick: value => (state, actions) => {
    actions.loadDocumentList()
    actions.showLoad()
  },
  showLoad: value => (state, actions) => {
    return {dialogState: "is-active"}
  },
  hideLoad: value => (state, actions) => {
    return {dialogState: ""}
  },
  loadDocumentList: value => (state, actions) => {
    axios.get('documents/')
    .then(function (response) {
      console.log(response); //TODO present feedback to user
      actions.setDocumentList(response.data.documents)
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
  },
  setDocumentList: value => (state, actions) => ({
    documents: value
  }),
  deleteDocument: value => (state, actions) => {
    axios.delete('documents/'+value)
    .then(function (response) {
      console.log(response); //TODO present feedback to user
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
  },
  loadDocument: value => (state, actions) => {
    axios.get('documents/'+value)
    .then(function (response) {
      console.log(response); //TODO present feedback to user
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
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
          <textarea id="abcEditor" class="textarea code" rows="20" >{state.defaultText}</textarea>
          <div id="warnings"></div>
          <div id="midi"></div>
        </div>
        <div class="column">
          <div id="canvas"></div>
        </div>
      </div>
    </div>

    <div class={"modal " + state.dialogState}>
      <div class="modal-background" onclick={actions.hideLoad}></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <p class="modal-card-title">Manage Documents</p>
          <button class="delete" aria-label="close" onclick={actions.hideLoad}></button>
        </header>
        <section class="modal-card-body">
            <span hidden={state.documents.length > 0}>No Documents</span>
          <ol>
            {state.documents.map(({id, title, composer}) => (
              <li value={id}><a onclick={actions.loadDocument(id)} {composer} - {title}</a></li>
            ))}
          </ol>
        </section>
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
