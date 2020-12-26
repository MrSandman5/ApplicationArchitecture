<template>
  <div id="app">
    <b-navbar style="width: 100%" type="dark" variant="dark">
      <b-navbar-brand id="nav-brand" href="#">Java+Spring+Vue</b-navbar-brand>
      <router-link to="/admin" class="nav-link text-light" v-if="this.$store.getters.isAuthenticated && this.$store.getters.isAdmin">Admin</router-link>
      <router-link to="/sign-up" class="nav-link text-light" v-if="!this.$store.getters.isAuthenticated">Sign Up</router-link>
      <router-link to="/login" class="nav-link text-light" v-if="!this.$store.getters.isAuthenticated">Sign In</router-link>
      <a href="#" class="nav-link text-light" v-if="this.$store.getters.isAuthenticated" v-on:click="logout">Logout </a>
    </b-navbar>
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'app',
  methods: {
    logout() {
      this.$store.dispatch('logout');
      this.$router.push('/')
    }
  }
}
</script>

<style>
</style>


