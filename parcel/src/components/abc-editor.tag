<abc-editor>
  <div class="container">
    <div class="columns">
      <div class="column">
        <textarea id="abcEditor" class="textarea code" rows="20" value={defaultValue}></textarea>
        <div id="warnings"></div>
        <div id="midi"></div>
      </div>
      <div class="column">
        <div id="canvas"></div>
      </div>
    </div>
  </div>

  import abcjs from 'abcjs/midi'

  this.defaultValue = 'T: Untitled\nC: Unknown\nK: '

  this.on('mount', function() {
    new abcjs.Editor('abcEditor', {
      canvas_id: 'canvas',
      generate_midi: true,
      midi_id: "midi",
      generate_warnings: true,
      warnings_id: 'warnings'
    })
  })

</abc-editor>
