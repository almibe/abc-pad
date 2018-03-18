import { h, app } from 'hyperapp'
import Header from './components/Header.js'
import ABCEditor from './components/ABCEditor.js'
import LoadDialog from './components/LoadDialog.js'

const state = {
  title: 'Untitled',
  document: '',
  documentId: null
}

const actions = {
  saveDocument: value => state => ({})
}

const view = () =>
  <main>
    <Header></Header>
    <ABCEditor></ABCEditor>
    <LoadDialog></LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
