<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      Tickets
      <button @click="createTicket">Add</button>
    </h1>
    <div v-if="!tickets.length">
      There are no tickets yet. Add one?
    </div>
    <div v-else class="ticket" v-for="(item, index) in tickets" :key="index">
        Expo: {{item.expo}}<br>
        Cost: {{item.artist}}<br>
        <br>
    </div>
    <div class="modal-wrapper" v-if="modalIsOpen">
      <span class="close" @click="modalIsOpen = false">Close</span>
      <form>
        <input type="hidden" :value="currentTicket.id">
        <div class="form-group">
          <label for="expo" class="col-form-label">Expo</label>
          <input type="text" v-model="currentTicket.expo" class="form-control" id="expo">
        </div>
        <button type="button" class="btn btn-primary" @click="saveTicket">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import ClientService from '../../service/client.service';

const TICKET_TEMPLATE = {
  expo: '',
  id: ''
};

export default {
  name: "Tickets",
  data() {
    return {
      selected: '',
      tickets: [],
      modalIsOpen: false,
      currentTicket: TICKET_TEMPLATE
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    this.fetchTickets();
  },
  methods: {
    fetchTickets() {
      ClientService.getMe(this.currentUser.id).then(({data}) => {
        ClientService.getTickets(data.id).then(({data}) => {
          this.tickets = data;
        }).catch(() => {
          console.error('Error loading tickets')
        })
      });
    },
    createTicket() {
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentTicket, TICKET_TEMPLATE);
    },
    saveTicket() {
      if (!this.currentTicket.id) {
        ClientService.getMe(this.currentUser.id).then(({data}) => {
          ClientService.addTicket(data.id,
              {name: this.currentTicket.expo}).then((result) => {
            console.log(result);
          })
        });
      }
      Object.assign(this.currentTicket, TICKET_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchTickets();
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