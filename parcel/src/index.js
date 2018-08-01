import riot from 'riot'
import axios from 'axios'
import '../build/tags'
import 'bulma/bulma.sass'
import 'abcjs/abcjs-midi.css'
import 'font-awesome/css/font-awesome.css'
import './main.css'

const bus = riot.observable()
const busMixin = {bus}
riot.mixin(busMixin)
riot.mount('abc-application')

const documentId = -1
const documentEditor = document.getElementById('abcEditor')

bus.on('save request', () => {
  if (documentId > 0) {
    axios.patch('documents/'+documentId, {
      document: documentEditor.value
    })
    .then((response) => {
      console.log(response)
      status.next("Saved document " + documentId)
    })
    .catch((error) => {
      console.log(error)
      staus.next("Error saving document " + documentId)
    });
  } else {
    axios.post('documents/', {
      document: documentEditor.value
    })
    .then((response) => {
      console.log(response); //TODO present feedback to user
    })
    .catch((error) => {
      console.log(error); //TODO present feedback to user
    });
  }
})

//
//bus.on('', function() {
//
//})
