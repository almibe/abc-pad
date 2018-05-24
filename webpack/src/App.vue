<template>
  <div id="app">
    <main>
      <Header saveDocument={actions.saveDocument} showLoad={actions.showLoad}></Header>
      <ABCEditor document={state.document} setText={actions.setText}></ABCEditor>
      <LoadDialog showLoad={state.showLoad} ></LoadDialog>
    </main>
  </div>
</template>

<script>
import Header from './components/Header.vue'
import ABCEditor from './components/ABCEditor.vue'
import LoadDialog from './components/LoadDialog.vue'
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

export default {
  name: 'app',
  components: {

  }
}
</script>

<style>
</style>
