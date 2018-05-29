import { h, app } from 'hyperapp'
import { Header } from './components/Header.js'
import { ABCEditor } from './components/ABCEditor.js'
import { LoadDialog } from './components/LoadDialog.js'
import 'bulma/bulma.sass'
import 'abcjs/abcjs-midi.css'
import 'font-awesome/css/font-awesome.css'
import './main.css'
import controllers from './controllers.js'

const state = {
  documentId: -1,
  dialogState: '',
  document: 'T: Untitled\nC: Unknown\nK: ',
  documentList: [],
  status: ''
}

const actions = {
  setText: text => ({
    document: text
  }),
  saveDocument: value => state => {
    controllers.saveDocument(state)
  },
  loadDocument: ({ id }) => state => {
    controllers.loadDocument(id)
  },
  showLoad: function(state) {
    return {dialogState: "is-active"}
  },
  hideLoad: function(state) {
    return {dialogState: ""}
  },
  postStatus: status => state => {
    status: status
  }
}

const view = (state, actions) =>
  <main>
    <Header saveDocument={actions.saveDocument} showLoad={actions.showLoad}></Header>
    <ABCEditor document={state.document} setText={actions.setText}></ABCEditor>
    <LoadDialog dialogState={state.dialogState} hideLoad={actions.hideLoad} documentList={state.documentList}></LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
