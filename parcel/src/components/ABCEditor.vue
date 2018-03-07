<template>
  <div class="container">
    <div class="columns">
      <div class="column">
        <textarea id="abcEditor" class="textarea code" rows="20">{abcDoc}</textarea>
        <div id="warnings"></div>
      </div>
      <div className="column">
        <div id="canvas"></div>
      </div>
    </div>
  </div>
</template>

<script>
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'

export default {
  name: 'ABCEditor',
  data () {
    return {
      abcDoc: ''
    }
  }
}
</script>

<style scoped>
</style>
