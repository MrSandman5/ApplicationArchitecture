<template>
  <div class="container">
    <header class="jumbotron">
      <h3>
        User: <strong>{{currentUser.username}}</strong>
      </h3>
    </header>
    <p>
      <strong>Id:</strong>
      {{currentUser.id}}
    </p>
    <p>
      <strong>Email:</strong>
      {{currentUser.email}}
    </p>
    <p>
      <strong>First name:</strong>
      {{currentUser.firstName}}
    </p>
    <p>
      <strong>Last name:</strong>
      {{currentUser.lastName}}
    </p>
    <p>
      <strong>Authority:</strong>
      {{currentUser.roles[0]}}
    </p>
  </div>
</template>

<script>
import ClientService from '../../service/client.service';
import OwnerService from '../../service/owner.service';
import ArtistService from '../../service/artist.service';

export default {
  name: 'Profile',
  data: () => {
    return {
      userData: {}
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
    this.getProfileInfo();
  },
  methods: {
    getProfileInfo() {
      const roles = this.currentUser.roles;

      if (roles.includes('ROLE_CLIENT')) {
        ClientService.getMe(this.currentUser.id).then(({data}) => {
          this.userData = data;
        })
      } else if (roles.includes('ROLE_OWNER')) {
        OwnerService.getMe(this.currentUser.id).then(({data}) => {
          this.userData = data;
        })
      } else if (roles.includes('ROLE_ARTIST')) {
        ArtistService.getMe(this.currentUser.id).then(({data}) => {
          this.userData = data;
        })
      }
    }
  }
};
</script>