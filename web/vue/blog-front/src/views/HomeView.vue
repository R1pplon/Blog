<template>

  <div class="home-container">
    <!-- 主页内容 -->
    <main class="home-content">
      <section class="profile-section">
        <div class="profile-card">
          <img
              src="../assets/avatar/default.jpg"
              alt="作者头像"
              class="avatar"
          />
          <h1 class="name">R1pple</h1>
          <p class="bio">全栈开发者 | 开源爱好者 | 技术写作者</p>

          <div class="contact-info">
            <div class="contact-item">
              <span class="icon">📧</span>
              <a href="mailto:zhangsan@example.com">zhangsan@example.com</a>
            </div>
            <div class="contact-item">
              <span class="icon">🐱</span>
              <a
                  href="https://github.com/R1pple"
                  target="_blank"
                  rel="noopener noreferrer"
              >
                GitHub
              </a>
            </div>
          </div>
        </div>
      </section>

      <section class="intro-section">
        <h2>个人简介</h2>
        <div class="intro-content">
          <p>拥有5年全栈开发经验，专注于：</p>
          <ul>
            <li>前端技术：Vue/React生态</li>
            <li>后端开发：Node.js/Go</li>
            <li>云原生技术：Docker/Kubernetes</li>
          </ul>
          <p>热爱技术分享，累计创作技术文章200+篇</p>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f8f9fa;
}

.nav-links a {
  color: #4a4a4a;
  text-decoration: none;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: #3498db;
}

.nav-links a.router-link-exact-active {
  color: #3498db;
  border-bottom: 2px solid #3498db;
}

/* 主页内容样式 */
.home-content {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 0 2rem;
}

.profile-section {
  margin-bottom: 3rem;
}

.profile-card {
  background: white;
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  text-align: center;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin: 0 auto 1.5rem;
  object-fit: cover;
}

.name {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.bio {
  color: #7f8c8d;
  margin-bottom: 1.5rem;
}

.contact-info {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-top: 1.5rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.contact-item a {
  color: #3498db;
  text-decoration: none;
}

.contact-item a:hover {
  text-decoration: underline;
}

.intro-section {
  background: white;
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.intro-section h2 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
}

.intro-content {
  line-height: 1.8;
  color: #4a4a4a;
}

.intro-content ul {
  padding-left: 2rem;
  margin: 1rem 0;
}

.intro-content li {
  margin-bottom: 0.5rem;
}

@media (max-width: 768px) {


  .home-content {
    padding: 0 1rem;
  }

  .profile-card,
  .intro-section {
    padding: 1.5rem;
  }

  .contact-info {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>
<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
</script>