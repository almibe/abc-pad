<template>
  <div id="app">
    <Header></Header>
    <ABCEditor></ABCEditor>
    <LoadDialog></LoadDialog>
  </div>
</template>

<script>
import Header from './components/Header.vue'
import ABCEditor from './components/ABCEditor.vue'
import LoadDialog from './components/LoadDialog.vue'

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
