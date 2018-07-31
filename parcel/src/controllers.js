import axios from 'axios'
import { Observable, BehaviorSubject } from 'rxjs'
import { ajax } from 'rxjs/ajax';
import { map } from 'rxjs/operators';

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
  const documentEditor = document.getElementById('abcEditor')

  //Observables
  const saveClick = Observable.fromEvent(saveButton, 'click')
  const loadClick = Observable.fromEvent(showLoadButton, 'click')

  saveClick.pipe(
    map(x => documentId),
    switchMap(documentId => {
      if (documentIdValue > 0) {
        return Observable.create(observer => {
          axios.patch('documents/'+documentIdValue, {
            document: documentEditor.value
          })
          .then(function (response) {
            console.log(response)
            status.next("Saved document " + documentIdValue)
          })
          .catch(function (error) {
            console.log(error)
            staus.next("Error saving document " + documentIdValue)
          });
        })
      } else {
        return Observable.create(observer => {
          axios.post('documents/', {
            document: documentEditor.value
          })
          .then(function (response) {
            console.log(response); //TODO present feedback to user
          })
          .catch(function (error) {
            console.log(error); //TODO present feedback to user
          });
        })
      }
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

