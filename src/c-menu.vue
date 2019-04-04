<template>
  <div>
    <a class="button" @click="newDoc">New</a>
    <a class="button" @click="save">Save</a>
    <a class="button" @click="saveAs">Save As</a>
    <a class="button" @click="load">Load</a>
  </div>
</template>

<script>
export default {
  name: 'c-menu',
  data () {
    return {
    }
  },
  methods: {
    newDoc: function() { /*TODO*/ },
    save: function() { /*TODO*/ },
    saveAs: function() { /*TODO*/ },
    load: function() { /*TODO*/ }
  }
}
</script>
