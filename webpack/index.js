import abcjs from 'abcjs/midi'
import './main.css'
import 'abcjs/abcjs-midi.css'
require('./main.scss')
//import 'font-awesome/css/font-awesome.css'

new abcjs.Editor('abcEditor', {
  canvas_id: 'canvas',
  generate_midi: true,
  midi_id: "midi",
  generate_warnings: true,
  warnings_id: 'warnings'
})
