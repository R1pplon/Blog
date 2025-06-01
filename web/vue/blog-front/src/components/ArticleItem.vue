<template>
  <div class="article-item" @click="$emit('click')">
    <h2 class="title">{{ article.title }}</h2>
    <p class="preview">{{ article.preview }}</p>
    <div class="meta">
      <time :datetime="article.createTime">{{ formatDate(article.createTime) }}</time>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import dayjs from 'dayjs'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const formatDate = (dateString) => {
  return dayjs(dateString).format('YYYY年MM月DD日')
}
</script>

<style scoped>
.article-item {
  padding: 1.5rem;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  cursor: pointer;
}

.article-item:hover {
  transform: translateY(-2px);
}

.title {
  margin: 0 0 1rem;
  color: #2c3e50;
  font-size: 1.4rem;
}

.preview {
  margin: 0 0 1rem;
  color: #666;
  line-height: 1.6;
}

.meta {
  font-size: 0.9rem;
  color: #999;
}
</style>