<template>
  <MainLayout>
    <section class="about-section">
      <h1>Добро пожаловать в Impulse</h1>
      <p>Мы помогаем людям становиться сильнее, здоровее и увереннее в себе.</p>
    </section>

    <!-- Карусель -->
    <ExerciseCarousel />

    <!-- Инфо блоки -->
    <section class="info-section">
      <div class="info-card" v-for="(block, index) in blocks" :key="index">
        <h2>{{ block.title }}</h2>
        <p>{{ block.text }}</p>
      </div>
    </section>
  </MainLayout>
</template>


<script setup>
import MainLayout from '@/components/blocks/MainLayout.vue'
import ExerciseCarousel from '@/components/carousel/ExerciseCarousel.vue'
import { onMounted } from 'vue'

const blocks = [
  {
    title: 'Индивидуальные тренировки',
    text: 'Наша команда профессиональных тренеров создаст индивидуальный план занятий, полностью соответствующий вашему уровню подготовки, целям и образу жизни. Мы учитываем особенности тела, привычки и пожелания каждого клиента.'
  },
  {
    title: 'Современное оборудование',
    text: 'Залы оснащены лучшими тренажёрами от мировых производителей. Вы можете быть уверены в их надёжности, безопасности и эффективности при любом уровне подготовки.'
  },
  {
    title: 'Поддержка и сопровождение',
    text: 'Мы не просто тренируем — мы сопровождаем на пути к успеху. Онлайн-консультации, контроль прогресса и психологическая поддержка обеспечат стабильный и комфортный путь к вашей цели.'
  }
]

const reveal = () => {
  const cards = document.querySelectorAll('.info-card')
  const triggerBottom = window.innerHeight * 0.85
  cards.forEach((card) => {
    const boxTop = card.getBoundingClientRect().top
    if (boxTop < triggerBottom) {
      card.classList.add('show')
    }
  })
}

onMounted(() => {
  window.addEventListener('scroll', reveal)
  reveal()
})
</script>
<style src="@/assets/home.css" scoped></style>
