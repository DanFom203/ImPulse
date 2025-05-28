<template>
  <footer class="footer">
    <p class="footer_inner">ImPulse</p>
  </footer>
</template>

<script>
export default {
  name: 'DefaultFooter'
}
</script>

<style scoped>
.footer {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  border-top: 1px solid black;
}

.footer_inner {
  opacity: 75;
  margin: 14px auto;
}
</style>
