<template>
  <div class="container">
    <div class="columns">
      <div class="column">
        <div class="field">
          <div class="control">
            <textarea id="abcEditor" v-model="editorContent" class="textarea code" rows="20" ref="abcEditor"></textarea>
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
        <div class="field is-horizontal">
          <div class="field-label is-normal">
            <label class="label">Tempo:</label>
          </div>
          <div class="field-body">
            <div class="control">
              <input v-model="tempo" @input="changeTempo" class="input" ref="tempoInput" type="text">
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
import { EventBus } from './event-bus.js';
import { newDoc, saveDoc, saveDocAs, loadDoc } from './abc-file-persistence.js'

export default {
  name: 'c-editor',
  created() {
    EventBus.$on('new-doc', () => {
      newDoc(this); //TODO pass arguments
    });

    EventBus.$on('save-doc', () => {
      saveDoc(this); //TODO pass arguments
    });

    EventBus.$on('save-doc-as', () => {
      saveDocAs(this); //TODO pass arguments
    });

    EventBus.$on('load-doc', () => {
      loadDoc(this); //TODO pass arguments
    });
  },
  mounted: function() {
    this.editorContent = this.abcTemplate;
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
      } else if (this.transpose === 'Guitar') {
        this.abcEditor.paramChanged({midiTranspose: -12})
      } else {
        this.abcEditor.paramChanged({midiTranspose: 0})
      }
    },
    changeTempo: function() {
      if (isNaN(this.tempo) || this.tempo <= 0) {
        this.$refs.tempoInput.classList.add("is-danger")
      } else {
        this.$refs.tempoInput.classList.remove("is-danger")
        this.abcEditor.paramChanged({qpm: this.tempo})
      }
    }
  },
  data() {
    return {
      transpose: 'Piano',
      abcEditor: null,
      editorContent: '',
      tempo: 180,
      fileReference: null,
      abcTemplate: "T:Title\nC:Composer\nM:4/4\nK:C\n"
    }
  }
}
</script>
