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
      {{item.name}}
      {{item.info}}
      {{item.startTime}}
      {{item.endTime}}
      {{item.ticketPrice}} <button type="button" class="btn btn-primary" @click="() => closeExpo(item)">Close</button>
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
    /*createExpo() {
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentExpo, EXPO_TEMPLATE);
    },
    saveExpo() {
      if (!this.currentExpo.id) {
        OwnerService.getMe(this.currentUser.id).then(({data}) => {
          OwnerService.createExpo(data.id, {
            name: this.currentExpo.name,
            info: this.currentExpo.info,
            artist: this.currentExpo.artist,
            startTime: this.currentExpo.startTime,
            endTime: this.currentExpo.endTime,
            ticketPrice: this.currentExpo.ticketPrice
          }).then((result) => {
             console.log(result);
          })
        })
      }

      Object.assign(this.currentExpo, EXPO_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchExpos();
    },*/
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