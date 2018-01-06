import Vue from 'vue'
import { load } from './store.js'
import ABCEditor from './ABCEditor.vue'
import './main.scss'

new Vue({
  el: '#app',
  render(h) {
    return h(ABCEditor)
  },
  mounted() {
    load()
  }
})
