<template>
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
</template>

<script>
import abcjs from 'abcjs/midi'

export default {
  name: 'c-editor',
  mounted: function() {
    console.log("in mounted")
    new abcjs.Editor('abcEditor', {
      canvas_id: 'canvas',
      generate_midi: true,
      midi_id: 'midi',
      generate_warnings: true,
      warnings_id: 'warnings'
    })
  },
  data() {
    return {
    }
  }
}
</script>
