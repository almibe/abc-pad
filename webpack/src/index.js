import { h, app } from 'hyperapp'
import { Header } from './components/Header.js'
import { ABCEditor } from './components/ABCEditor.js'
import { LoadDialog } from './components/LoadDialog.js'
import '../node_modules/bulma/bulma.sass'
import './main.css'
import controllers from './controllers.js'

const state = {
  documentId: -1,
  showLoad: false,
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
  showLoad: value => state => { return {showLoad: true} },
  postStatus: status => state => {
    status: status
  }
}

const view = (state, actions) =>
  <main>
    <Header saveDocument={actions.saveDocument} showLoad={actions.showLoad}></Header>
    <ABCEditor document={state.document} setText={actions.setText}></ABCEditor>
    <LoadDialog showLoad={state.showLoad} ></LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
