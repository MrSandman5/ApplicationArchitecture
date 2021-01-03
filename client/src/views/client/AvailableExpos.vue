<template>
  <div>
    <br>
    <h1>Available expos</h1>
    <div v-if="!expos.length">
      There are no available expos
    </div>
    <div v-else class="expo" v-for="(item, index) in expos" :key="index">
      <b>{{item.name}}</b><br>
      Info: {{item.info}}<br>
    </div>
  </div>
</template>

<script>
import ClientService from '../../service/client.service';

export default {
  name: "AvailableExpos",
  data() {
    return {
      expos: [],
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    this.fetchExpos();
  },
  methods: {
    fetchExpos() {
      ClientService.getMe(this.currentUser.id).then(({data}) => {
        ClientService.getExpos(data.id).then(({result}) => {
          this.expos = result;
        }).catch(() => {
          console.error('Error loading expos')
        })
      });
    }
  }
}
</script>