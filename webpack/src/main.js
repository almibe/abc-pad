import Vue from 'vue'
import App from './App'
import ABCJS from '../node_modules/abcjs/bin/abcjs_editor_latest-min.js'
// import router from './router' // TODO add back for #19
import '../node_modules/bulma/css/bulma.css'
import './main.css'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  // router, //TODO add back for #19
  render: h => h(App)
})

window.onload = function () {
  new ABCJS.Editor('abcEditor', { canvas_id: 'canvas', generate_midi: false, warnings_id: 'warnings' })
}
