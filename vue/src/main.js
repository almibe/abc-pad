import Vue from 'vue'
import App from './App'
// import router from './router' // TODO add back for #19
import '../node_modules/bulma/css/bulma.css'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  // router, //TODO add back for #19
  render: h => h(App)
})
