import riot from 'riot'
import '../build/tags'
import 'bulma/bulma.sass'
import 'abcjs/abcjs-midi.css'
import 'font-awesome/css/font-awesome.css'
import './main.css'

const bus = riot.observable()
const busMixin = {bus}
riot.mixin(busMixin)
riot.mount('abc-application')

//bus.on('saveDocument', function(id, documentValue) {
//
//})
//
//bus.on('', function() {
//
//})
