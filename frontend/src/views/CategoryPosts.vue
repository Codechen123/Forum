<template>
  <div class="category-posts">
    <div class="category-header" v-if="category">
      <h1>{{ category.name }}</h1>
      <p class="category-description">{{ category.description }}</p>
      <el-divider />
    </div>

    <div class="posts-container">
      <div class="posts-header">
        <h2>帖子列表</h2>
        <el-button type="primary" @click="$router.push('/create-post')" v-if="isAuthenticated">
          发布新帖子
        </el-button>
      </div>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="posts.length === 0" class="empty-container">
        <el-empty description="暂无帖子" />
      </div>

      <div v-else class="posts-list">
        <el-card 
          v-for="post in posts" 
          :key="post.id" 
          class="post-card"
          @click="$router.push(`/posts/${post.id}`)"
        >
          <div class="post-header">
            <div class="post-title">
              <h3>{{ post.title }}</h3>
              <el-tag v-if="post.pinned" type="warning" size="small">置顶</el-tag>
            </div>
            <div class="post-meta">
              <span class="author">{{ post.author.username }}</span>
              <span class="time">{{ formatDate(post.createdAt) }}</span>
            </div>
          </div>
          
          <div class="post-content">
            <p>{{ post.content.substring(0, 150) }}{{ post.content.length > 150 ? '...' : '' }}</p>
          </div>
          
          <div class="post-stats">
            <span><i class="el-icon-view"></i> {{ post.viewCount }}</span>
            <span><i class="el-icon-chat-dot-round"></i> {{ post.commentCount }}</span>
            <span><i class="el-icon-star-off"></i> {{ post.likeCount }}</span>
          </div>
        </el-card>
      </div>

      <div class="pagination-container" v-if="totalPages > 1">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="totalElements"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { categoryAPI } from '@/api/categories'
import { postAPI } from '@/api/posts'
import { ElMessage } from 'element-plus'

export default {
  name: 'CategoryPosts',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const authStore = useAuthStore()
    
    const category = ref(null)
    const posts = ref([])
    const loading = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalElements = ref(0)
    const totalPages = ref(0)
    
    const isAuthenticated = computed(() => authStore.isAuthenticated)
    
    const fetchCategory = async () => {
      try {
        const response = await categoryAPI.getCategoryById(route.params.id)
        category.value = response.data
      } catch (error) {
        ElMessage.error('获取分类信息失败')
        console.error('Error fetching category:', error)
      }
    }
    
    const fetchPosts = async (page = 1) => {
      loading.value = true
      try {
        const response = await postAPI.getPostsByCategory(route.params.id, {
          page: page - 1,
          size: pageSize.value
        })
        
        posts.value = response.data.content
        totalElements.value = response.data.totalElements
        totalPages.value = response.data.totalPages
        currentPage.value = page
      } catch (error) {
        ElMessage.error('获取帖子列表失败')
        console.error('Error fetching posts:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handlePageChange = (page) => {
      fetchPosts(page)
    }
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    onMounted(() => {
      fetchCategory()
      fetchPosts()
    })
    
    return {
      category,
      posts,
      loading,
      currentPage,
      pageSize,
      totalElements,
      totalPages,
      isAuthenticated,
      handlePageChange,
      formatDate
    }
  }
}
</script>

<style scoped>
.category-posts {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.category-header h1 {
  color: #409eff;
  margin-bottom: 10px;
}

.category-description {
  color: #666;
  font-size: 14px;
  margin-bottom: 0;
}

.posts-container {
  margin-top: 20px;
}

.posts-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.posts-header h2 {
  margin: 0;
  color: #333;
}

.loading-container,
.empty-container {
  margin: 40px 0;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-card {
  cursor: pointer;
  transition: all 0.3s ease;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.post-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.post-title h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.post-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 12px;
  color: #999;
}

.author {
  color: #409eff;
  font-weight: 500;
}

.post-content {
  margin-bottom: 15px;
}

.post-content p {
  margin: 0;
  color: #666;
  line-height: 1.6;
}

.post-stats {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #999;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style> 