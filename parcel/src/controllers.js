
export default  = {
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
  loadDocument: function(id) {
    //TODO make axios call
  }
}
