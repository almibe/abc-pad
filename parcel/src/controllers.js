import axios from 'axios'
import riot from 'riot'

export function init() {
  //Behaviors
  const showLoadModal = new BehaviorSubject(false)
  const listOfDocuments = new BehaviorSubject([])
  const status = new BehaviorSubject("")
  const documentId = new BehaviorSubject(-1)
  const saveButtonActive =  new BehaviorSubject(true)

  //DOM Elements
  const saveButton = document.getElementById('save')
  const showLoadButton = document.getElementById('showLoad')


  //Observables
  const saveClick = Observable.fromEvent(saveButton, 'click')
  const loadClick = Observable.fromEvent(showLoadButton, 'click')

  saveClick.pipe(
    map(x => documentId),
    switchMap(documentId => {
    })
  )

  const saveDocumentCall = saveDocumentInfo.switchMap(state => {
    state.saveButtonActive.next(false)
    state.status.next("Saving...")

  })


}

showLoad() {
  controllers.loadDocumentList(actions)
  return {dialogState: "is-active"}
}

hideLoad() {
  return {dialogState: ""}
}





const controllers  = {
  loadDocumentList: function(actions) {
    axios.get('documents/')
    .then(function (response) {
      console.log(response); //TODO present feedback to user
      actions.setDocumentList(response.data.documents)
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
  },
  deleteDocument: function(id) {
    axios.delete('documents/'+id)
    .then(function (response) {
      console.log(response); //TODO present feedback to user
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
  },
  loadDocument: function(id) {
    axios.get('documents/'+id)
    .then(function (response) {
      console.log(response); //TODO present feedback to user
    })
    .catch(function (error) {
      console.log(error); //TODO present feedback to user
    });
  }
}

