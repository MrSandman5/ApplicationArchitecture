<template>
  <div id="app">
    <ArtGallery/>
    <footer class="info">
      <p>RESTful art gallery application (Java + Spring Boot + Vue.js)</p>
    </footer>
  </div>
</template>

<script>
import ArtGallery from './components/ArtGallery'

const app = {
  name: 'app',
  components: {
    ArtGallery
  },
  data: () => {
    return {}
  }
}

export default app
</script>

<style>
/*#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}*/
[v-cloak] { display: none; }
</style>
