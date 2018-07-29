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
  showLoad: (value) => (state, actions) => {
    controllers.loadDocumentList(actions)
    return {dialogState: "is-active"}
  },
  setDocumentList: value => {
    return { documentList: value }
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
    <LoadDialog
      dialogState={state.dialogState}
      hideLoad={actions.hideLoad}
      documentList={state.documentList}
      loadDocument={actions.loadDocument}>
    </LoadDialog>
  </main>

const application = app(state, actions, view, document.getElementById('app'))
