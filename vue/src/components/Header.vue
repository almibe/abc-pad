<template>
  <div className="container">
    <div className="container">
      <h1 className="title">ABC Editor</h1>
    </div>

    <div className="container">
      <div className="columns">
        <div className="column">
          <div className="field is-horizontal">
            <div className="field-label is-normal">
              <label className="label">Name</label>
            </div>
            <div className="field-body">
              <div className="field">
                <p className="control">
                  <input id="name" className="input" type="text" value={docName} />
                  <input id="id" className="input" type="hidden" value={docId} />
                </p>
              </div>
            </div>
          </div>
        </div>
        <div className="column">
          <div className="field is-grouped">
            <div className="control">
              <button className="button" id="save">Save</button>
            </div>
            <div className="control">
              <button className="button" id="showLoad">Load</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Header',
  data () {
    return {
      docName: '',
      docId: null
    }
  }
}
</script>

<style scoped>
</style>
