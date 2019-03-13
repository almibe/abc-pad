<template>
  <c-header></c-header>
</template>

<script>
import CHeader from './c-header';

export default {
  name: 'c-main',
  data () {
    return {
       abcDocument: ''
    }
  },
  components: {
    CHeader
  }
}
</script>
