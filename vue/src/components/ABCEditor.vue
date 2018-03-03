<template>
  <div className="container">
    <div className="columns">
      <div className="column">
        <textarea id="abcEditor" className="textarea code" rows="20">{abcDoc}</textarea>
        <div id="warnings"></div>
      </div>
      <div className="column">
        <div id="canvas"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ABCEditor',
  data () {
    return {
      abcDoc: ''
    }
  }
}
</script>

<style scoped>
</style>
