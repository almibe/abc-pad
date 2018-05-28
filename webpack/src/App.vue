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
import './assets/main.css'
import controllers from './controllers.js'

export default {
  name: 'App',
  components: {
    Header,
    ABCEditor,
    LoadDialog
  },
  data: {
    documentId: -1,
    showLoad: false,
    document: 'T: Untitled\nC: Unknown\nK: ',
    documentList: [],
    status: ''
  }
}
</script>
