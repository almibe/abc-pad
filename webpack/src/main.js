import Vue from 'vue'
import store from './store.js'
import App from './App.vue'

Vue.config.productionTip = false

new Vue({
  el: '#app',
  store,
  components: { App },
  render: h => h(App)
})
