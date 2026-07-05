<template>
  <div id="app">
    <Header v-if="showNav" />
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script>
import Header from './components/Header.vue'

export default {
  name: 'App',
  components: { Header },
  computed: {
    showNav() {
      return true
    },
  },
}
</script>

<style>
@import './assets/main.css';

.main-content { min-height: calc(100vh - 60px); }
</style>
