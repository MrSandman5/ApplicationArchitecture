<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      Reservations
      <button @click="saveReservation">Create</button>
    </h1>
    <div v-if="!reservations.length">
      There are no reservations yet. Create one?
    </div>
      <div v-else class="reservation" v-for="(item, index) in reservations" :key="index">
          id: {{item.id}}<br>
          status: {{item.status}}<br>
        cost: {{item.cost}} <button type="button" class="btn btn-primary" @click="() => pay">Pay</button><br>
      </div>
    <div class="modal-wrapper" v-if="modalIsOpen">-->
      <span class="pay" @click="modalIsOpen = false">Close</span>
      <form>
        <input type="hidden" :value="currentReservation.id">
        <div class="form-group">
          <label for="owner" class="col-form-label">Owner</label>
          <input type="number" step="0.1" v-model="currentPayment.owner" class="form-control" id="owner">
        </div>
        <button type="button" class="btn btn-primary" @click="payForReservation">Pay</button>
      </form>
    </div>
  </div>
</template>

<script>
import ClientService from '../../service/client.service';

const RESERVATION_TEMPLATE = {
  status: '',
  cost: '',
  id: ''
};

const PAYMENT_TEMPLATE = {
  owner: ''
}

export default {
  name: "Reservations",
  data() {
    return {
      selected: '',
      reservations: [],
      modalIsOpen: false,
      currentReservation: RESERVATION_TEMPLATE,
      currentPayment : PAYMENT_TEMPLATE
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
    saveReservation() {
      if (!this.currentReservation.id) {
        ClientService.getMe(this.currentUser.id).then(({data}) => {
          ClientService.createReservation(data.id).then((data) => {
            console.log(data);
          })
        })
      }
      Object.assign(this.currentReservation, RESERVATION_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchReservations();
    },
    pay() {
      console.log("PAY")
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentPayment, PAYMENT_TEMPLATE);
    },
    payForReservation() {
      console.log("PAYFORRESERVATION")
      if (!this.currentReservation.id) {
        ClientService.getMe(this.currentUser.id).then(({data}) => {
          ClientService.payForReservation(data.id,
              {owner: this.currentPayment.owner}).then((result) => {
            console.log(result);
          })
        })
      }
      Object.assign(this.currentReservation, RESERVATION_TEMPLATE);
      Object.assign(this.currentPayment, PAYMENT_TEMPLATE);
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

.pay {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 20px;
  cursor: pointer;
}
</style>