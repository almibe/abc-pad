<template>
  <div class="container">
    <div class="columns">
      <div class="column">
        <div class="field">
          <div class="control">
            <textarea id="abcEditor" class="textarea code" rows="20" ></textarea>
          </div>
        </div>
        <div id="warnings"></div>
        <div class="field is-horizontal">
          <div class="field-label is-normal">
            <label class="label">Transpose:</label>
          </div>
          <div class="field-body">
            <div class="control">
              <div class="select">
                <select v-model="transpose" @change="changeTranspose">
                  <option>Piano</option>
                  <option>Guitar</option>
                </select>
              </div>
            </div>
          </div>
        </div>
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
    this.abcEditor = new abcjs.Editor('abcEditor', {
      canvas_id: 'canvas',
      generate_midi: true,
      midi_id: 'midi',
      generate_warnings: true,
      warnings_id: 'warnings'
    })
  },
  methods: {
    changeTranspose: function() {
      if (this.transpose === 'Piano') {
        this.abcEditor.paramChanged({midiTranspose: 0})
        //this.abcEditor.abcjsParams = {midiTranspose: 0}
      } else if (this.transpose === 'Guitar') {
        this.abcEditor.paramChanged({midiTranspose: -12})
        //this.abcEditor.abcjsParams = {midiTranspose: -12}
      } else {
        this.abcEditor.paramChanged({midiTranspose: 0})
        //this.abcEditor.abcjsParams = {midiTranspose: 0}
      }
    }
  },
  data() {
    return {
      transpose: 'Piano',
      abcEditor: null
    }
  }
}
</script>
