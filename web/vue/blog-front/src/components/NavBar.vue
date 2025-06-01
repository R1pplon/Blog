<template>
  <nav class="navbar">
    <div class="nav-content">
      <router-link to="/" class="logo">技术博客</router-link>
      <div class="nav-links">
        <router-link
            v-for="link in links"
            :key="link.path"
            :to="link.path"
            class="nav-link"
        >
          {{ link.title }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
const links = [
  { path: '/blogs', title: '文章列表' },
  { path: '/about', title: '关于作者' }
]
</script>

<style scoped>
.navbar {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: #4a4a4a;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.nav-link:hover {
  background-color: #f8f9fa;
  color: #3498db;
}

.nav-link.router-link-exact-active {
  color: #3498db;
  font-weight: 500;
  position: relative;
}

.nav-link.router-link-exact-active::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  right: 0;
  height: 2px;
  background: #3498db;
}

@media (max-width: 768px) {
  .nav-content {
    padding: 1rem;
  }

  .nav-links {
    gap: 1rem;
  }

  .nav-link {
    padding: 0.5rem;
  }
}
</style>
