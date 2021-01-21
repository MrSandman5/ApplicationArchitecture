<template>
  <div>
    <div v-if="modalIsOpen" class="overlay"></div>
    <br>
    <h1>
      Artworks
      <button @click="createArtwork">Add</button>
    </h1>
    <div v-if="!artworks.length">
      There are no artworks yet. Add one?
    </div>
    <div v-else class="expo" v-for="(item, index) in artworks" :key="index">
      <b>Name: </b>{{item.name}}<br>
      <b>Info: </b>{{item.info}}<br>
      <br>
    </div>
    <div class="modal-wrapper" v-if="modalIsOpen">
      <span class="close" @click="modalIsOpen = false">Close</span>
      <form>
        <input type="hidden" :value="currentArtwork.id">
        <div class="form-group">
          <label for="name" class="col-form-label">Name</label>
          <input type="text" v-model="currentArtwork.name" class="form-control" id="name">
        </div>
        <div class="form-group">
          <label for="info" class="col-form-label">Info</label>
          <input type="text" v-model="currentArtwork.info" class="form-control" id="info">
        </div>
        <button type="button" class="btn btn-primary" @click="saveArtwork">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import ArtistService from '../../service/artist.service';
import BackendService from '../../service/backend.service';

const ARTWORK_TEMPLATE = {
  name: '',
  info: ''
};

export default {
  name: "Artworks",
  data() {
    return {
      selected: '',
      artworks: [],
      modalIsOpen: false,
      currentArtwork: ARTWORK_TEMPLATE
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    this.fetchArtworks();
  },
  methods: {
    fetchArtworks() {
      ArtistService.getMe(this.currentUser.id).then(({data}) => {
        ArtistService.getAllArtworks(data.id).then(({data}) => {
          this.artworks = data;
        }).catch(() => {
          console.error('Error loading artworks')
        })
      })
    },
    createArtwork() {
      this.modalIsOpen = true;
      this.selected = '';
      Object.assign(this.currentArtwork, ARTWORK_TEMPLATE);
    },
    saveArtwork() {
      if (!this.currentArtwork.id) {
        ArtistService.getMe(this.currentUser.id).then(({data}) => {
          ArtistService.addArtwork(data.id,
              {
                name: this.currentArtwork.name,
                info: this.currentArtwork.info
              }).then((result) => {
            console.log(result);
          })
        });
      }
      Object.assign(this.currentArtwork, ARTWORK_TEMPLATE);
      this.selected = '';
      this.modalIsOpen = false;
      this.fetchArtworks();
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