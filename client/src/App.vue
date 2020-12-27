<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <router-link to="/home" class="navbar-brand">
        Art Gallery
      </router-link>
      <div class="navbar-nav mr-auto">
        <li v-for="(item, index) in getLinks()">
          <router-link :to="item.url" :key="index" class="nav-link">{{item.name}}</router-link>
        </li>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus"/>
            Sign Up
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt"/>
            Login
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user"/>
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt"/>
            LogOut
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>

<script>

export default {
  name: 'app',
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    getRole() {
      if (!this.currentUser) {
        return;
      }

      const roles = this.currentUser.roles;

      if (roles.includes('ROLE_OWNER')) {
        return 'OWNER'
      }

      if (roles.includes('ROLE_ARTIST')) {
        return 'ARTIST'
      }

      return 'CLIENT'
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
      }

      return false;
    },
    showOwnerBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_OWNER');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    },
    getLinks() {
      switch (this.getRole) {
        case 'ARTIST':
          /*return [{
            name: 'Upcoming interviews',
            url: '/upcoming-interviews'
          }];*/
        case 'CLIENT':
          /*return [{
            name: 'Open positions',
            url: '/open-positions'
          }, {
            name: 'My applications',
            url: '/my-applications'
          }, {
            name: 'My interviews',
            url: '/my-interviews'
          }];*/
        case 'OWNER':
          /*return [{
            name: 'Positions',
            url: '/positions'
          }, {
            name: 'Rotation applications',
            url: '/rotation-applications'
          },  {
            name: 'External applications',
            url: '/external-applications'
          }];*/
        default:
          return []
      }
    }
  }
}
</script>

<style>
</style>


