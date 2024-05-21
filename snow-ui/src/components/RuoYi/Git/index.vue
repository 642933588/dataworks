<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'snowGit',
  data() {
    return {
      url: 'https://gitee.com/y_project/snow-Cloud'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
