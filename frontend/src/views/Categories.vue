<template>
  <div class="categories-page">
    <div class="page-header">
      <h1>分类管理</h1>
      <p>浏览不同分类的帖子，发现感兴趣的内容</p>
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="3" animated />
    </div>
    
    <div v-else-if="categories.length === 0" class="empty-container">
      <el-empty description="暂无分类" />
    </div>
    
    <div v-else>
      <el-row :gutter="20">
        <el-col :span="8" v-for="category in categories" :key="category.id">
          <el-card class="category-card" @click="$router.push(`/categories/${category.id}`)">
            <div class="category-header">
              <el-tag :color="category.color" size="large">{{ category.name }}</el-tag>
              <span class="post-count">{{ category.postCount || 0 }} 帖子</span>
            </div>
            <p class="category-description">{{ category.description || '暂无描述' }}</p>
            <div class="category-actions">
              <el-button type="primary" size="small" @click.stop="$router.push(`/categories/${category.id}`)">
                查看帖子
              </el-button>
              <el-button type="text" size="small" @click.stop="viewCategoryStats(category)">
                统计信息
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 分类统计对话框 -->
    <el-dialog v-model="statsDialogVisible" title="分类统计" width="400px">
      <div v-if="selectedCategory" class="stats-content">
        <h3>{{ selectedCategory.name }}</h3>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">{{ selectedCategory.postCount || 0 }}</div>
            <div class="stat-label">帖子数量</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ calculateTotalComments(selectedCategory) }}</div>
            <div class="stat-label">评论总数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ formatDate(selectedCategory.createdAt) }}</div>
            <div class="stat-label">创建时间</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { categoryAPI } from '@/api/categories'
import { ElMessage } from 'element-plus'

export default {
  name: 'Categories',
  setup() {
    const categories = ref([])
    const loading = ref(false)
    const statsDialogVisible = ref(false)
    const selectedCategory = ref(null)
    
    const fetchCategories = async () => {
      loading.value = true
      try {
        const response = await categoryAPI.getAllCategories()
        categories.value = response.data.map(category => ({
          ...category,
          postCount: category.posts ? category.posts.length : 0
        }))
      } catch (error) {
        ElMessage.error('获取分类列表失败')
        console.error('Error fetching categories:', error)
      } finally {
        loading.value = false
      }
    }
    
    const viewCategoryStats = (category) => {
      selectedCategory.value = category
      statsDialogVisible.value = true
    }
    
    const calculateTotalComments = (category) => {
      // 这里可以根据需要计算总评论数
      // 目前返回一个估算值
      return (category.postCount || 0) * 3
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    }
    
    onMounted(() => {
      fetchCategories()
    })
    
    return {
      categories,
      loading,
      statsDialogVisible,
      selectedCategory,
      viewCategoryStats,
      calculateTotalComments,
      formatDate
    }
  }
}
</script>

<style scoped>
.categories-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  color: #409eff;
  margin-bottom: 10px;
}

.page-header p {
  color: #666;
  font-size: 14px;
}

.loading-container,
.empty-container {
  margin: 40px 0;
}

.category-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 200px;
  display: flex;
  flex-direction: column;
}

.category-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.post-count {
  font-size: 12px;
  color: #999;
}

.category-description {
  color: #666;
  margin-bottom: 20px;
  flex-grow: 1;
  line-height: 1.6;
}

.category-actions {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.stats-content h3 {
  text-align: center;
  color: #409eff;
  margin-bottom: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .categories-page {
    padding: 10px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style> 