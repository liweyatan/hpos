<template>
  <div id="app">
    <Header v-if="showNav" />
    <main>
      <router-view />
    </main>
    <Footer v-if="showNav" />
  </div>
</template>

<script>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

export default {
  name: 'App',
  components: { Header, Footer },
  computed: {
    showNav() {
      return this.$route.path !== '/login'
    },
  },
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }
</style>
