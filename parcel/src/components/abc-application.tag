<abc-application>
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

  import riot from 'riot'
  import controllers from './controllers.js'

  this.documentId = -1
  this.dialogState = ''
  this.document = 'T: Untitled\nC: Unknown\nK: '
  this.documentList = []
  this.status = ''
  var self = this

</abc-application>
