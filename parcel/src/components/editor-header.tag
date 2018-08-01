<editor-header>
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
          </div>
        </div>
        <div class="column">
          {status}
        </div>
      </div>
    </div>
  </div>

  this.saveButtonDisabled = false

  saveOnClick() {
    this.saveButtonDisabled = true
    this.bus.trigger('save request')
  }

  managerOnClick() {
    this.bus.trigger('show manager')
  }

</editor-header>
