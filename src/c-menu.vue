<template>
  <div>
    <a class="button" @click="newDoc">New</a>
    <a class="button" @click="save">Save</a>
    <a class="button" @click="saveAs">Save As</a>
    <a class="button" @click="load">Load</a>
  </div>
</template>

<script>
import { EventBus } from './event-bus.js';

export default {
  name: 'c-menu',
  data () {
    return {
    }
  },
  methods: {
    newDoc: function() { EventBus.$emit('new-doc'); },
    save: function() { EventBus.$emit('save-doc'); },
    saveAs: function() { EventBus.$emit('save-doc-as'); },
    load: function() { EventBus.$emit('load-doc'); }
  }
}
</script>
