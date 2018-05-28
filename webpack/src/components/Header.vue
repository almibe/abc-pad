<template>
  <div class="container header">
    <div class="container">
      <div class="columns">
        <div class="column">
          <h1 class="title">ABC Editor</h1>
          <div class="field is-grouped">
            <div class="control">
              <button class="button" id="save" onclick={() => saveDocument()}>Save</button>
            </div>
            <div class="control">
              <button class="button" id="showLoad" onclick={() => showLoad()}>Manage Documents</button>
            </div>
          </div>
        </div>
        <div class="column">
          {{ status }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header {
  margin-top: 12px;
}
</style>
