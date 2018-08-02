<template>
  <div id="app">
    <div class="container header">
      <div class="container">
        <div class="columns">
          <div class="column">
            <h1 class="title">ABC Editor</h1>
            <div class="field is-grouped">
              <div class="control">
                <button class="button" id="save" onclick={ saveOnClick } disabled={saveButtonDisabled}>Save</button>
              </div>
              <div class="control">
                <button class="button" id="showManager" onclick={ managerOnClick } >Manage Documents</button>
              </div>
              <div id="status">
                {status}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container">
      <div class="columns">
        <div class="column">
          <textarea id="abcEditor" class="textarea code" rows="20" ></textarea>
          <div id="warnings"></div>
          <div id="midi"></div>
        </div>
        <div class="column">
          <div id="canvas"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'app',
    data () {
      return {
        //defaultValue: 'T: Untitled\nC: Unknown\nK: '
      }
    }
  }


//  this.documentId = -1
//  this.dialogState = ''
//  this.documentList = []
//  this.status = ''
//  var self = this


/*
    this.saveButtonDisabled = false
    this.status = ""

    saveOnClick() {
      this.saveButtonDisabled = true
  //    this.bus.trigger('save request')
    }

    managerOnClick() {
  //    this.bus.trigger('show manager')
    }

    this.bus.on('*', (value) => {
      if(value.hasOwnProperty('status') {
  //      this.status = value.status
  //      this.update()
      }
    })
  */
</script>

<style>
  #status { padding-top: 6px; }
</style>
