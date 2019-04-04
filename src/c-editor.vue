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
              <input v-model="tempo" @input="changeTempo" type="text">
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

const abcTemplate = "T:Title\nC:Composer\nM:4/4\nK:C\n"

export default {
  name: 'c-editor',
  created() {
    EventBus.$on('new-doc', () => {
      if (this.saveCheck()) {
        this.editorContent = abcTemplate;
        this.$nextTick(() => { this.abcEditor.fireChanged(); })
        this.$refs.abcEditor.focus();
      }
    });

    EventBus.$on('save-doc', () => {
      if (this.fileReference != null) {
        this.handleSaveAs();
      } else {
        this.writeFile();
      }
    });

    EventBus.$on('save-doc-as', () => {
      this.handleSaveAs()
    });

    EventBus.$on('load-doc', () => {
      if (this.saveCheck()) {
        this.handleLoad()
      }
    });
  },
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
    saveCheck: function() {
      if (this.editorContent === abcTemplate && this.fileReference === null) {
        return true;
      } else if (this.fileReference === null) {
        //TODO prompt user to save with dialog
        return true;
      } else {
        //TODO read file reference and compare to editorContent
        return true;
      }
    },
    handleSaveAs: function() {
      //TODO show show as dialog
      //TODO update file reference
      this.writeFile();
    },
    handleLoad: function() {
      //TODO show load dialog
      //TODO set file reference
      //TODO load file
    },
    writeFile: function() {
      //TODO
    },
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
      if (isNaN(this.tempo)) {
         //TODO handle error
      } else {
         this.abcEditor.paramChanged({qpm: this.tempo})
      }
    }
  },
  data() {
    return {
      transpose: 'Piano',
      abcEditor: null,
      editorContent: abcTemplate,
      tempo: 180,
      fileReference: null
    }
  }
}
</script>
