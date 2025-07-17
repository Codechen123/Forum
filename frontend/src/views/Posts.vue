<template>
  <div class="posts-page">
    <div class="header-bar">
      <h1>帖子列表</h1>
      <el-button type="primary" @click="$router.push('/create-post')">发布新帖子</el-button>
    </div>
    <el-card>
      <div class="filter-bar">
        <el-radio-group v-model="selectedCategory" @change="handleCategoryChange">
          <el-radio-button :label="''">全部</el-radio-button>
          <el-radio-button v-for="cat in categories" :key="cat.id" :label="cat.id">{{ cat.name }}</el-radio-button>
        </el-radio-group>
      </div>
      <div v-loading="loading" class="posts-list">
        <template v-if="!loading && posts.length === 0">
          <div class="empty-tip">暂无帖子</div>
        </template>
        <div v-else>
          <div v-for="post in posts" :key="post.id" class="post-item">
            <div class="post-header">
              <router-link :to="`/posts/${post.id}`" class="post-title">{{ post.title }}</router-link>
              <div class="post-meta">
                <el-tag v-if="post.category" size="small" :color="post.category.color">{{ post.category.name }}</el-tag>
                <span class="author">by {{ post.author.username }}</span>
                <span class="date">{{ formatDate(post.createdAt) }}</span>
              </div>
            </div>
            <div class="post-content">{{ post.content?.substring(0, 120) }}...</div>
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
      <div class="pagination-bar" v-if="total > pageSize">
        <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize"
          :current-page="page + 1" @current-change="handlePageChange" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { postAPI } from '@/api/posts'
import { categoryAPI } from '@/api/categories'
import { View, Star, ChatLineSquare } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

export default {
  name: 'Posts',
  components: { View, Star, ChatLineSquare },
  setup() {
    const loading = ref(true)
    const posts = ref([])
    const categories = ref([])
    const selectedCategory = ref('')
    const page = ref(0)
    const pageSize = 10
    const total = ref(0)

    const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

    const fetchCategories = async () => {
      try {
        const res = await categoryAPI.getAllCategories()
        categories.value = res.data
      } catch (e) {
        categories.value = []
      }
    }

    const fetchPosts = async () => {
      loading.value = true
      try {
        let res
        if (selectedCategory.value) {
          res = await postAPI.getPostsByCategory(selectedCategory.value, { page: page.value, size: pageSize })
          posts.value = res.data.content
          total.value = res.data.totalElements
        } else {
          res = await postAPI.getAllPosts({ page: page.value, size: pageSize })
          posts.value = res.data.content
          total.value = res.data.totalElements
        }
      } catch (e) {
        posts.value = []
        total.value = 0
      }
      loading.value = false
    }

    const handleCategoryChange = () => {
      page.value = 0
      fetchPosts()
    }

    const handlePageChange = (val) => {
      page.value = val - 1
      fetchPosts()
    }

    onMounted(async () => {
      await fetchCategories()
      await fetchPosts()
    })

    return {
      loading,
      posts,
      categories,
      selectedCategory,
      page,
      pageSize,
      total,
      formatDate,
      handleCategoryChange,
      handlePageChange
    }
  }
}
</script>

<style scoped>
.posts-page {
  max-width: 1200px;
  margin: 0 auto;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-bar {
  margin-bottom: 20px;
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
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-title {
  font-size: 20px;
  color: #409eff;
  text-decoration: none;
  font-weight: bold;
}

.post-title:hover {
  text-decoration: underline;
}

.post-meta {
  font-size: 14px;
  color: #888;
  display: flex;
  gap: 10px;
  align-items: center;
}

.post-content {
  color: #444;
  margin: 8px 0 4px 0;
}

.post-stats {
  font-size: 13px;
  color: #aaa;
  display: flex;
  gap: 18px;
  align-items: center;
}

.empty-tip {
  text-align: center;
  color: #aaa;
  padding: 32px 0;
  font-size: 16px;
}

.pagination-bar {
  margin-top: 24px;
  text-align: center;
}
</style>
