import axios from 'axios'

const controllers  = {
  saveDocument: function(state) {
    if (state.documentId > 0) {
      axios.patch('documents/'+state.documentId, {
        document: state.document
      })
      .then(function (response) {
        console.log(response); //TODO present feedback to user
      })
      .catch(function (error) {
        console.log(error); //TODO present feedback to user
      });
    } else {
      axios.post('documents/', {
        document: state.document
      })
      .then(function (response) {
        console.log(response); //TODO present feedback to user
      })
      .catch(function (error) {
        console.log(error); //TODO present feedback to user
      });
    }
  },
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

export default controllers;
