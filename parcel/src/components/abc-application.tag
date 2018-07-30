<abc-application>
  <main>
    <editor-header saveDocument={actions.saveDocument} showLoad={actions.showLoad}></editor-header>
    <abc-editor document={state.document} setText={actions.setText}></abc-editor>
    <load-dialog
      dialogState={state.dialogState}
      hideLoad={actions.hideLoad}
      documentList={state.documentList}
      loadDocument={actions.loadDocument}>
    </load-document>
  </main>

  import riot from 'riot'
  import controllers from './controllers.js'

  this.documentId = -1
  this.dialogState = ''
  this.document = 'T: Untitled\nC: Unknown\nK: '
  this.documentList = []
  this.status = ''
  var self = this

  this.on('mount', function() {
    controllers.init()
  })

</abc-application>
