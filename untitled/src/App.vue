<template>
  <div id="app">
    <Header />
    <main>
      <RegistrationForm />
    </main>
    <Footer />
  </div>
</template>

<script>
import Header from './components/Header.vue';
import RegistrationForm from './components/RegistrationForm.vue';
import Footer from './components/Footer.vue';

export default {
  name: 'App',
  components: {
    Header,
    RegistrationForm,
    Footer
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #f5f7fa;
}
</style>
