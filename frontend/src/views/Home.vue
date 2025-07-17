<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="18">
        <div class="main-content">
          <div class="welcome-banner">
            <el-card>
              <div class="banner-content">
                <h1>欢迎来到论坛</h1>
                <p>在这里分享您的想法，与他人交流讨论</p>
                <el-button type="primary" size="large" @click="$router.push('/posts')">
                  浏览帖子
                </el-button>
              </div>
            </el-card>
          </div>

          <div class="recent-posts">
            <el-card>
              <template #header>
                <div class="section-header">
                  <h3>最新帖子</h3>
                  <el-button type="text" @click="$router.push('/posts')">查看更多</el-button>
                </div>
              </template>

              <div v-loading="loading" class="posts-list">
                <template v-if="!loading && posts.length === 0">
                  <div class="empty-tip">暂无帖子</div>
                </template>
                <div v-else>
                  <div v-for="post in posts" :key="post.id" class="post-item">
                    <div class="post-header">
                      <router-link :to="`/posts/${post.id}`" class="post-title">
                        {{ post.title }}
                      </router-link>
                      <div class="post-meta">
                        <el-tag v-if="post.category" size="small" :color="post.category.color">
                          {{ post.category.name }}
                        </el-tag>
                        <span class="author">by {{ post.author.username }}</span>
                        <span class="date">{{ formatDate(post.createdAt) }}</span>
                      </div>
                    </div>
                    <div class="post-content">
                      {{ post.content.substring(0, 200) }}...
                    </div>
                    <div class="post-stats">
                      <span><el-icon>
                          <View />
                        </el-icon> {{ post.viewCount }}</span>
                      <span><el-icon>
                          <Star />
                        </el-icon> {{ post.likeCount }}</span>
                      <span><el-icon>
                          <ChatLineSquare />
                        </el-icon> {{ post.commentCount }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="sidebar">
          <el-card class="stats-card">
            <template #header>
              <h4>社区统计</h4>
            </template>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ stats.totalPosts }}</div>
                <div class="stat-label">总帖子数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ stats.totalUsers }}</div>
                <div class="stat-label">用户数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ stats.totalComments }}</div>
                <div class="stat-label">评论数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ categories.length }}</div>
                <div class="stat-label">分类数</div>
              </div>
            </div>
          </el-card>

          <el-card class="categories-card">
            <template #header>
              <h4>热门分类</h4>
            </template>
            <div class="categories-list">
              <template v-if="!loading && categories.length === 0">
                <div class="empty-tip">暂无分类</div>
              </template>
              <div v-else>
                <div v-for="category in categories" :key="category.id" class="category-item">
                  <router-link :to="`/categories/${category.id}`">
                    <el-tag :color="category.color">{{ category.name }}</el-tag>
                    <span class="post-count">{{ category.postCount }} 帖子</span>
                  </router-link>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="popular-posts-card">
            <template #header>
              <h4>热门帖子</h4>
            </template>
            <div class="popular-posts">
              <template v-if="!loading && popularPosts.length === 0">
                <div class="empty-tip">暂无热门帖子</div>
              </template>
              <div v-else>
                <div v-for="post in popularPosts" :key="post.id" class="popular-post-item">
                  <router-link :to="`/posts/${post.id}`">
                    <div class="post-title">{{ post.title }}</div>
                    <div class="post-stats">
                      <span><el-icon>
                          <View />
                        </el-icon> {{ post.viewCount }}</span>
                      <span><el-icon>
                          <Star />
                        </el-icon> {{ post.likeCount }}</span>
                    </div>
                  </router-link>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { postAPI } from '@/api/posts'
import { categoryAPI } from '@/api/categories'
import { View, Star, ChatLineSquare } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

export default {
  name: 'Home',
  components: {
    View,
    Star,
    ChatLineSquare
  },
  setup() {
    const loading = ref(true)
    const posts = ref([])
    const categories = ref([])
    const popularPosts = ref([])
    const stats = ref({
      totalPosts: 0,
      totalUsers: 0,
      totalComments: 0
    })

    const formatDate = (date) => {
      return dayjs(date).format('YYYY-MM-DD HH:mm')
    }

    const fetchPosts = async () => {
      try {
        const response = await postAPI.getAllPosts({ page: 0, size: 10 })
        posts.value = response.data.content
        stats.value.totalPosts = response.data.totalElements
      } catch (error) {
        console.error('Failed to fetch posts:', error)
      }
    }

    // mock 用户数和评论数
    const fetchStats = () => {
      stats.value.totalUsers = 0 // TODO: 后端补充接口后替换
      stats.value.totalComments = 0 // TODO: 后端补充接口后替换
    }

    const fetchCategories = async () => {
      try {
        const response = await categoryAPI.getAllCategories()
        categories.value = response.data
      } catch (error) {
        console.error('Failed to fetch categories:', error)
      }
    }

    const fetchPopularPosts = async () => {
      try {
        const response = await postAPI.getPopularPosts()
        popularPosts.value = response.data
      } catch (error) {
        console.error('Failed to fetch popular posts:', error)
      }
    }

    onMounted(async () => {
      loading.value = true
      await Promise.all([
        fetchPosts(),
        fetchCategories(),
        fetchPopularPosts()
      ])
      fetchStats()
      loading.value = false
    })

    return {
      loading,
      posts,
      categories,
      popularPosts,
      stats,
      formatDate
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-banner {
  margin-bottom: 20px;
}

.banner-content {
  text-align: center;
  padding: 40px 0;
}

.banner-content h1 {
  font-size: 32px;
  color: #409eff;
  margin-bottom: 16px;
}

.banner-content p {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.posts-list {
  min-height: 400px;
}

.post-item {
  padding: 16px 0;
  border-bottom: 1px solid #e4e4e4;
}

.post-item:last-child {
  border-bottom: none;
}

.post-header {
  margin-bottom: 8px;
}

.post-title {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
  text-decoration: none;
  margin-bottom: 4px;
  display: block;
}

.post-title:hover {
  text-decoration: underline;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #999;
}

.post-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 8px;
}

.post-stats {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #999;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.categories-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-item a {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-decoration: none;
  padding: 8px 0;
}

.category-item a:hover {
  background-color: #f5f5f5;
}

.post-count {
  font-size: 12px;
  color: #999;
}

.popular-posts {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.popular-post-item a {
  display: block;
  text-decoration: none;
  padding: 8px 0;
  border-bottom: 1px solid #e4e4e4;
}

.popular-post-item a:hover {
  background-color: #f5f5f5;
}

.popular-post-item .post-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.popular-post-item .post-stats {
  font-size: 12px;
  color: #999;
  justify-content: flex-start;
}

.empty-tip {
  text-align: center;
  color: #aaa;
  padding: 32px 0;
  font-size: 16px;
}
</style>