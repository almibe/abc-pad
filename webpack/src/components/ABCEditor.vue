<template>
  <div class="container">
    <div class="columns">
      <div class="column">
        <textarea id="abcEditor" class="textarea code" rows="20" value={{$store.state.document}}></textarea>
        <div id="warnings"></div>
      </div>
      <div class="column">
        <div id="canvas"></div>
      </div>
    </div>
  </div>
</template>

<script>
import abcjs from 'abcjs'

export default {
  mounted: function() {
    new abcjs.Editor('abcEditor', { canvas_id: 'canvas', generate_midi: false, warnings_id: 'warnings' })
  }
}
</script>

<style scoped>
textarea.code {
  font-family: monospace;
}
</style>
