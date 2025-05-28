@import './main.css';
<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'

import { Navigation, Pagination, Autoplay } from 'swiper/modules'

const exercises = [
  {
    name: 'Приседания',
    image: 'https://medaboutme.ru/upload/medialibrary/949/shutterstock_1223066476.jpg'
  },
  {
    name: 'Отжимания',
    image: 'https://avatars.mds.yandex.net/i?id=2a7b69f9bcfd00be57e1be83b0a06628_l-4291359-images-thumbs&n=13'
  },
  {
    name: 'Планка',
    image: 'https://i.pinimg.com/originals/b0/27/c4/b027c43fc52edb7560bbd803ee901fd0.jpg'
  },
  {
    name: 'Скручивания',
    image: 'https://avatars.mds.yandex.net/i?id=12479403b4a95355ebe75c364438bca6556644e4-9149104-images-thumbs&n=13'
  },
  {
    name: 'Выпады',
    image: 'https://avatars.mds.yandex.net/i?id=f3d932c3c258261cc606ca235129968b_l-5334334-images-thumbs&n=13'
  }
]
</script>

<template>
  <div class="carousel-container">
    <Swiper
        :modules="[Navigation, Pagination, Autoplay]"
        :navigation="true"
        :pagination="{ clickable: true }"
        :loop="true"
        :autoplay="{ delay: 3000 }"
        class="my-swiper"
    >
      <SwiperSlide v-for="(ex, index) in exercises" :key="index">
        <div class="slide-content">
          <img :src="ex.image" :alt="ex.name" />
          <h2 class="title">{{ ex.name }}</h2>
        </div>
      </SwiperSlide>
    </Swiper>
  </div>
</template>

<style scoped>
.carousel-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.my-swiper {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.slide-content {
  position: relative;
  text-align: center;
}

.slide-content img {
  width: 100%;
  height: auto;
  display: block;
}

.title {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 2rem;
  background: rgba(0, 0, 0, 0.5);
  color: var(--vt-c-green);
  padding: 0.5rem 1rem;
  border-radius: 8px;
}
.swiper-button-next,
.swiper-button-prev {
  color:var(--vt-c-green) !important;
}

.swiper-button-next::after,
.swiper-button-prev::after {
  font-size: 32px;
}
</style>
