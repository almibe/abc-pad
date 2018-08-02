

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
      bus.trigger('save result', {status: "Saved document " + documentId})
    })
    .catch((error) => {
      bus.trigger('save result', {status: "Error saving document " + documentId})
    });
  } else {
    axios.post('documents/', {
      document: documentEditor.value
    })
    .then((response) => {
      bus.trigger('save result', {status: "Saved document " + response.data.id})
    })
    .catch((error) => {
      bus.trigger('save result', {status: "Error saving document."})
    });
  }
})

//
//bus.on('', function() {
//
//})
