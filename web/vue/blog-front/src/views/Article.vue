<template>
  <div class="article-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>正在加载文章...</p>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="error" class="error-state">
      <div class="error-icon">⚠️</div>
      <h3>加载失败</h3>
      <p>{{ error }}</p>
      <button @click="fetchArticle" class="retry-btn">重试</button>
    </div>

    <!-- 内容展示 -->
    <div v-else-if="article" class="article-content">
      <!-- 返回按钮 -->
      <router-link to="/blogs" class="back-link">← 返回列表</router-link>

      <!-- 文章元信息 -->
      <div class="meta-info">
        <h1 class="title">{{ article.title }}</h1>
        <div class="sub-meta">
          <span class="author">作者ID: {{ article.authorId }}</span>
          <time class="time">发布于{{ formatTime(article.createTime) }}</time>
          <span class="update-time">最后更新：{{ formatTime(article.updateTime) }}</span>
        </div>
      </div>

      <!-- Markdown内容 -->
      <div class="markdown-body" v-html="renderMarkdown(article.content)"></div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <h3>文章不存在</h3>
      <router-link to="/" class="back-link">返回首页</router-link>
    </div>
    <!-- 评论功能模块 -->
    <div class="comment-section">
      <!-- 评论表单 -->
      <div class="comment-form">
        <h3>发表评论</h3>
        <textarea
            v-model="newComment.content"
            placeholder="输入您的评论..."
            :disabled="isSubmitting"
        ></textarea>
        <button
            @click="submitComment"
            :disabled="isSubmitting || !newComment.content.trim()"
        >
          {{ isSubmitting ? '提交中...' : '发表评论' }}
        </button>
      </div>

      <!-- 评论列表 -->
      <div class="comment-list">
        <div v-if="commentsLoading" class="loading-state">加载评论中...</div>
        <div v-else-if="commentsError" class="error-state">{{ commentsError }}</div>
        <template v-else>
          <div
              v-for="comment in comments"
              :key="comment.id"
              class="comment-item"
              :class="{ 'is-reply': comment.parentId }"
          >
            <div class="comment-header">
              <img
                  :src="comment.user.avatar"
                  class="user-avatar"
                  alt="用户头像"
              >
              <div class="user-info">
                <span class="user-name">{{ comment.user.nickname }}</span>
                <time class="comment-time">
                  {{ formatTime(comment.createTime) }}
                </time>
              </div>
            </div>
            <div class="comment-content">
              {{ comment.content }}
            </div>
            <div class="comment-actions">
              <button @click="showReplyForm(comment.id)">回复</button>
            </div>

            <!-- 回复表单 -->
            <div
                v-if="activeReplyId === comment.id"
                class="reply-form"
            >
              <textarea
                  v-model="replyContent"
                  placeholder="输入回复内容..."
              ></textarea>
              <button @click="submitReply(comment.id)">提交回复</button>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { marked } from 'marked'
import hljs from 'highlight.js'
import DOMPurify from 'dompurify'

// 初始化配置
marked.setOptions({
  highlight: (code, lang) => {
    return hljs.highlightAuto(code).value
  },
  breaks: true
})

const route = useRoute()
const article = ref(null)
const loading = ref(true)
const error = ref(null)

const api = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  timeout: 10000
})

// Markdown渲染方法（带安全过滤）
const renderMarkdown = (content) => {
  // 使用行分割去除首行
  const lines = content.split(/\r?\n/) // 兼容不同换行符
  lines.shift()
  const processed = lines.join('\n')
  return DOMPurify.sanitize(marked.parse(processed || ''))
}



// 时间格式化方法
const formatTime = (isoString) => {
  const date = new Date(isoString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const fetchArticle = async () => {
  try {
    loading.value = true
    error.value = null

    const response = await api.get(`/Articles/${route.params.id}`)

    if (response.data.code === 1 && response.data.data) {
      article.value = response.data.data
    } else {
      throw new Error(response.data.msg || '文章不存在')
    }
  } catch (err) {
    error.value = err.response?.data?.msg || err.message || '加载失败'
    article.value = null
  } finally {
    loading.value = false
  }
}

// 新增评论相关状态
const comments = ref([])
const commentsLoading = ref(false)
const commentsError = ref(null)
const newComment = ref({
  content: '',
  parentId: null
})
const isSubmitting = ref(false)
const activeReplyId = ref(null)
const replyContent = ref('')

// 获取文章评论
const fetchComments = async () => {
  try {
    commentsLoading.value = true
    commentsError.value = null
    const response = await api.get(`/Comments/article/${route.params.id}`)

    if (response.data.code === 1) {
      // 获取用户信息并合并到评论数据
      const commentsWithUser = await Promise.all(
          response.data.data.map(async comment => {
            const userResponse = await api.get(`/Users/${comment.userId}`)
            return {
              ...comment,
              user: userResponse.data.data
            }
          })
      )
      comments.value = commentsWithUser
    }
  } catch (err) {
    commentsError.value = '评论加载失败，请稍后重试'
    console.error('加载评论失败:', err)
  } finally {
    commentsLoading.value = false
  }
}

// 提交新评论
const submitComment = async () => {
  try {
    isSubmitting.value = true
    const payload = {
      articleId: route.params.id,
      userId: 3, // 假设当前用户ID，实际应从登录状态获取
      content: newComment.value.content,
      parentId: newComment.value.parentId
    }

    const response = await axios.post('/Comments/add', payload)
    if (response.data.code === 1) {
      // 清空表单并刷新评论
      newComment.value.content = ''
      await fetchComments()
    }
  } catch (err) {
    alert('评论提交失败，请重试')
    console.error('提交评论失败:', err)
  } finally {
    isSubmitting.value = false
  }
}

// 显示回复表单
const showReplyForm = (commentId) => {
  activeReplyId.value = commentId === activeReplyId.value ? null : commentId
}

// 提交回复
const submitReply = async (parentId) => {
  if (!replyContent.value.trim()) return

  try {
    const payload = {
      articleId: route.params.id,
      userId: 3, // 假设当前用户ID
      parentId: parentId,
      content: replyContent.value
    }

    const response = await axios.post('/Comments/add', payload)
    if (response.data.code === 1) {
      replyContent.value = ''
      activeReplyId.value = null
      await fetchComments()
    }
  } catch (err) {
    alert('回复提交失败')
    console.error('提交回复失败:', err)
  }
}

// 初始化时获取评论
onMounted(() => {
  fetchComments()
})
onMounted(fetchArticle)
</script>

<style scoped>
.article-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

/* 加载状态样式 */
.loading-state {
  text-align: center;
  padding: 2rem;
}
.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 错误状态 */
.error-state {
  text-align: center;
  padding: 2rem;
  color: #e74c3c;
}
.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}
.retry-btn {
  margin-top: 1rem;
  padding: 0.5rem 1.5rem;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 文章内容样式 */
.back-link {
  display: inline-block;
  margin-bottom: 2rem;
  color: #3498db;
  text-decoration: none;
}

.meta-info {
  margin-bottom: 2rem;
}
.title {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}
.sub-meta {
  color: #7f8c8d;
  font-size: 0.9rem;
}
.sub-meta > * + * {
  margin-left: 1rem;
}

/* Markdown内容样式 */
.markdown-body {
  line-height: 1.6;
}

.markdown-body :deep(h1) {
  font-size: 1.8rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}

.markdown-body :deep(code) {
  background-color: #f5f5f5;
  padding: 0.2em 0.4em;
  border-radius: 3px;
}

.markdown-body :deep(pre) {
  background-color: #f8f8f8;
  padding: 1em;
  border-radius: 4px;
  overflow-x: auto;
}

.markdown-body :deep(blockquote) {
  border-left: 4px solid #ddd;
  padding-left: 1em;
  color: #777;
}

.markdown-body :deep(ul) {
  padding-left: 2em;
  list-style-type: disc;
}

.markdown-body :deep(a) {
  color: #3498db;
  text-decoration: none;
}
.markdown-body :deep(a:hover) {
  text-decoration: underline;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 3rem;
}
.comment-section {
  margin-top: 3rem;
  border-top: 1px solid #eee;
  padding-top: 2rem;
}

.comment-form {
  margin-bottom: 2rem;
}
.comment-form h3 {
  margin-bottom: 1rem;
}
.comment-form textarea {
  width: 100%;
  height: 100px;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 1rem;
}
.comment-form button {
  background: #3498db;
  color: white;
  padding: 0.5rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.comment-form button:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.comment-list {
  max-width: 700px;
  margin: 0 auto;
}

.comment-item {
  background: #fff;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.comment-item.is-reply {
  margin-left: 2rem;
  background: #f8f9fa;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 1rem;
}
.user-name {
  font-weight: 500;
  margin-right: 0.5rem;
}
.comment-time {
  color: #7f8c8d;
  font-size: 0.9em;
}

.comment-content {
  color: #34495e;
  line-height: 1.6;
  margin-bottom: 0.5rem;
}

.comment-actions button {
  background: none;
  border: none;
  color: #3498db;
  cursor: pointer;
  padding: 0.3rem 0.5rem;
}
.comment-actions button:hover {
  text-decoration: underline;
}

.reply-form {
  margin-top: 1rem;
  padding-left: 1rem;
  border-left: 2px solid #3498db;
}
.reply-form textarea {
  width: 100%;
  height: 80px;
  margin: 0.5rem 0;
}
</style>
