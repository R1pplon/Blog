<template>
  <div class="container">

    <h1>技术博客</h1>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 错误提示 -->
    <div v-if="error" class="error">
      加载失败：{{ error }}
      <button @click="fetchArticles">重试</button>
    </div>

    <!-- 数据展示 -->
    <ul v-else-if="articles.length">
      <li
          v-for="article in articles"
          :key="article.id"
          @click="goToArticle(article.id)"
          class="article-item"
      >
        <article-item :article="article" />
      </li>
    </ul>

    <!-- 空状态 -->
    <div v-else class="empty">暂无文章</div>
  </div>
</template>

<script setup>
import NavBar from '@/components/NavBar.vue'
import { ref, onMounted } from "vue";
import ArticleItem from "@/components/ArticleItem.vue";
import axios from 'axios';
import { useRouter } from 'vue-router';
import router from "@/router/index.js";

const goToArticle = (id) => {
  router.push({
    name: 'article',
    params: { id } // 使用路由名称和params更规范
  });
};

// 创建axios实例
const api = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  timeout: 10000
});

// 响应式数据
const articles = ref([]);
const loading = ref(true);
const error = ref(null);

// 获取文章数据
const fetchArticles = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await api.get('/Articles');

    if (response.data.code === 1) {
      articles.value = response.data.data || [];
    } else {
      throw new Error(response.data.message || '未知错误');
    }
  } catch (err) {
    error.value = err.message || '请求失败';
    articles.value = [];  // 清空数据
  } finally {
    loading.value = false;
  }
};

// 生命周期钩子
onMounted(fetchArticles);
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  /* 增加顶部间距避免导航栏遮挡 */
  padding: 40px 20px 20px;
}
.article-item {
  cursor: pointer;
  transition: background-color 0.2s;
}

.article-item:hover {
  background-color: #f8f9fa;
}

.loading, .error, .empty {
  padding: 20px;
  text-align: center;
  background: #f5f5f5;
  border-radius: 4px;
  margin: 20px 0;
}

.error {
  color: #ff4444;
  background: #ffeaea;
}

.error button {
  margin-left: 10px;
  padding: 5px 10px;
  background: #fff;
  border: 1px solid #ff4444;
  border-radius: 3px;
  cursor: pointer;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 20px;
}

li:last-child {
  border-bottom: none;
}
</style>