<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      Opened Expositions
    </h1>
    <div v-if="!expos.length">
      There are no opened expos yet. They will appear soon, we hope.
    </div>
    <div v-else class="expo" v-for="(item, index) in expos" :key="index">
      <b>Name: </b>{{item.name}}<br>
      <b>Info: </b>{{item.info}}<br>
      <b>StartTime: </b>{{item.startTime}}<br>
      <b>EndTime: </b>{{item.endTime}}<br>
      <b>TicketPrice: </b>{{item.ticketPrice}} <button type="button" class="btn btn-primary" @click="() => closeExpo(item)">Close</button>
      <br>
    </div>
  </div>
</template>

<script>
import OwnerService from '../../service/owner.service';

const EXPO_TEMPLATE = {
  name: '',
  info: '',
  artist: '',
  startTime: '',
  endTime: '',
  ticketPrice: '',
  id: '',
};

export default {
  name: "OpenedExpos",
  data() {
    return {
      selected: '',
      expos: [],
      modalIsOpen: false,
      currentExpo: EXPO_TEMPLATE
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
      OwnerService.getMe(this.currentUser.id).then(({data}) => {
        OwnerService.getOpenedExpos(data.id).then(({data}) => {
          this.expos = data;
        }).catch(() => {
          console.error('Error loading positions')
        })
      });
    },
    closeExpo(expo) {
      OwnerService.getMe(this.currentUser.id).then(({data}) => {
        OwnerService.closeExpo(data.id, {
          ...expo
        }).then((result) => {
          console.log(result);
        })
      });

      this.fetchExpos();
    },
  }
}
</script>