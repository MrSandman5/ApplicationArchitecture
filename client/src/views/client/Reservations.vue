<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      Reservations
      <button @click="createReservation">Add</button>
    </h1>
    <div v-if="!reservations.length">
      There are no reservations yet. Create one?
    </div>
      <div v-else class="expo" v-for="(item, index) in reservations" :key="index">
          artist: {{item.artist}}<br>
          expo: {{item.expo}}<br>
          reservation: {{item.reservation}}<br>
          <br>
      </div>
    <div class="modal-wrapper" v-if="modalIsOpen">
      <span class="close" @click="modalIsOpen = false">Close</span>
      <form>
        <input type="hidden" :value="currentReservation.id">
        <div class="form-group">
          <label for="cost" class="col-form-label">Cost</label>
          <input type="number" step="0.1" v-model="currentReservation.cost" class="form-control" id="cost">
        </div>
        <div class="form-group">
          <label for="Time" class="col-form-label">Time</label>
          <input type="datetime-local" v-model="currentReservation.time" class="form-control" id="Time">
        </div>
        <div class="form-group">
          <label for="Status" class="col-form-label">Status</label>
          <input type="text" v-model="currentReservation.status" class="form-control" id="Status">
        </div>
        <button type="button" class="btn btn-primary" @click="saveReservation">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import ClientService from '../../service/client.service';

const RESERVATION_TEMPLATE = {
  cost: '',
  status: '',
  time: '',
  id: '',
};

export default {
  name: "Reservations",
  data() {
    return {
      selected: '',
      reservations: [],
      modalIsOpen: false,
      currentReservation: RESERVATION_TEMPLATE
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    this.fetchReservations();
  },
  methods: {
    fetchReservations() {
      ClientService.getMe(this.currentUser.id).then(({data}) => {
        ClientService.getNewReservations(data.id).then(({data}) => {
          this.reservations = data;
        }).catch(() => {
          console.error('Error loading tickets')
        })
      })
    },
    createReservation() {
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentReservation, RESERVATION_TEMPLATE);
    },
    saveReservation() {
      if (!this.currentReservation.id) {
        ClientService.createReservation(this.currentUser.id).then((data) => {
          console.log(data);
        })
      }
      Object.assign(this.currentReservation, RESERVATION_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchReservations();
    },
    payForReservation() {
      if (!this.currentReservation.id) {
        ClientService.getMe(this.currentUser.id).then(({data}) => {
          ClientService.payForReservation(data.id,
              {
                owner: 1,
                reservation: {reservationId: this.currentReservation.id}
              }).then((result) => {
            console.log(result);
          })
        })
      }
      Object.assign(this.currentReservation, RESERVATION_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchReservations();
    }
  }
}
</script>

<style>
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