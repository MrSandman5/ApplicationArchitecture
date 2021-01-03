<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      New Expositions
      <button @click="createExpo">Create new</button>
    </h1>
    <div v-if="!expos.length">
      There are no new expos yet. Create one?
    </div>
    <div v-else class="expo" v-for="(item, index) in expos" :key="index">
      {{item.name}} <span class="expo-start badge badge-info" @click="() => startExpo(item)">Start</span>
<!--       / <span class="expo-edit" @click="() => editExpo(item)">Edit</span>-->
      <b>{{item.title}}</b><br>
    </div>
    <div class="modal-wrapper" v-if="modalIsOpen">
      <span class="close" @click="modalIsOpen = false">Close</span>
      <form>
        <input type="hidden" :value="currentExpo.id">
        <div class="form-group">
          <label for="name" class="col-form-label">Name</label>
          <input type="text" v-model="currentExpo.name" class="form-control" id="name">
        </div>
        <div class="form-group">
          <label for="info" class="col-form-label">Info</label>
          <input type="text" v-model="currentExpo.info" class="form-control" id="info">
        </div>
        <div class="form-group">
          <label for="artist" class="col-form-label">Artist</label>
          <input type="number" v-model="currentExpo.artist" class="form-control" id="artist">
        </div>
        <div class="form-group">
          <label for="startTime" class="col-form-label">StartTime</label>
          <input type="datetime-local" v-model="currentExpo.startTime" class="form-control" id="startTime">
        </div>
        <div class="form-group">
          <label for="endTime" class="col-form-label">EndTime</label>
          <input type="datetime-local" v-model="currentExpo.endTime" class="form-control" id="endTime">
        </div>
        <div class="form-group">
          <label for="ticketPrice" class="col-form-label">TicketPrice</label>
          <input type="number" step="0.1" v-model="currentExpo.ticketPrice" class="form-control" id="ticketPrice">
        </div>
        <button type="button" class="btn btn-primary" @click="saveExpo">Save</button>
      </form>
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
  name: "NewExpos",
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
        OwnerService.getNewExpos(data.id).then(({data}) => {
          this.expos = data;
        }).catch(() => {
          console.error('Error loading positions')
        })
      });
    },
    createExpo() {
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentExpo, EXPO_TEMPLATE);
    },
    // Что здесь происходит?
    // editExpo(expo) {
    //   OwnerService.getMe(this.currentUser.id).then(({data}) => {
    //     console.log(expo);
    //     OwnerService.editExpo(data.id, {
    //       expo : {name : expo.name},
    //       settings : expo.settings,
    //       data : expo.data
    //     }).then((result) => {
    //       console.log(result);
    //     })
    //   });
    //
    //   this.fetchExpos();
    // },
    saveExpo() {
      if (!this.currentExpo.id) {
        OwnerService.getMe(this.currentUser.id).then(({data}) => {
          OwnerService.createExpo(data.id, {
            name: this.currentExpo.name,
            info: this.currentExpo.info,
            artist: Number(this.currentExpo.artist),
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
    },
    startExpo(expo) {
      OwnerService.getMe(this.currentUser.id).then(({data}) => {
        OwnerService.startExpo(data.id, {
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

<style>
.position {
  border: 1px solid;
  padding: 20px;
  border-radius: 4px;
  margin: 20px 0;
  position: relative;
}

.position-edit {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 16px;
  cursor: pointer;
}

.overlay {
  background: rgba(0, 0, 0, .25);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  overflow: hidden;
}

.modal-wrapper {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid beige;
  border-radius: 3px;
  background: white;
  padding: 20px;
  min-width: 600px;
}

.close {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 20px;
  cursor: pointer;
}
</style>