import { h, app } from 'hyperapp'
import { Header } from './components/Header.js'
import { ABCEditor } from './components/ABCEditor.js'
import { LoadDialog } from './components/LoadDialog.js'
import '../node_modules/bulma/bulma.sass'

const state = {
  title: 'Untitled',
  document: '',
  documentId: ''
}

const actions = {
  saveDocument: value => state => ({})
}

const view = () =>
  <main>
    <Header title={state.title} documentId={state.documentId}></Header>
    <ABCEditor></ABCEditor>
    <LoadDialog></LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
