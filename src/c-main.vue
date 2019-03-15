<template>
  <section class="section">
    <div class="container">
      <c-header></c-header>
      <c-menu></c-menu>
      <c-editor></c-editor>
    </div>
  </section>
</template>

<script>
import CHeader from './c-header';
import CMenu from './c-menu';
import CEditor from './c-editor';

export default {
  name: 'c-main',
  data () {
    return {
       abcDocument: ''
    }
  },
  components: {
    CHeader,
    CMenu,
    CEditor
  }
}
</script>
