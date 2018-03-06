import Vue from 'vue'
import Router from 'vue-router'
import ABCEditor from '@/components/ABCEditor'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'NewDocument',
      component: ABCEditor
    }
  ]
})
