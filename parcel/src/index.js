import { h, app } from 'hyperapp'
import axios from 'axios'
import { Header } from './components/Header.js'
import { ABCEditor } from './components/ABCEditor.js'
import { LoadDialog } from './components/LoadDialog.js'
import '../node_modules/bulma/bulma.sass'
import './main.css'

const state = {
  documentId: -1,
  showDialog: false,
  document: 'T: Untitled\nC: Unknown'
}

const actions = {
  setText: text => ({
    document: text
  }),
  saveDocument: value => state => ({
    if (state.documentId > 0) {
      axios.patch('/document/'+state.documentId, {
        document: state.document
      })
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
    } else {
      axios.post('/document/', {
        document: state.document
      })
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
    }
  }),
  loadDocument: ({ id }) => state => ({
    //TODO make axios call
  }),
  showLoad: value => state => ({ showDialog: true })
}

const view = () =>
  <main>
    <Header saveDocument={actions.saveDocument} showLoad={actions.showLoad}></Header>
    <ABCEditor document={state.document} setText={actions.setText}></ABCEditor>
    <LoadDialog showDialog={state.showDialog} ></LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
