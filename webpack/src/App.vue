<template>
  <div id="app">
    <Header></Header>
    <ABCEditor></ABCEditor>
  </div>
</template>

<script>
import Header from './components/Header.vue'
import ABCEditor from './components/ABCEditor.vue'

export default {
  name: 'App',
  components: {
    Header,
    ABCEditor
  }
}
</script>

<style>
</style>
