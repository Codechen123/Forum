<template>
  <div class="categories-page">
    <h1>分类管理</h1>
    <el-card>
      <p>这里将显示所有分类的列表，可以浏览不同分类的帖子。</p>
      <el-row :gutter="20">
        <el-col :span="8" v-for="category in mockCategories" :key="category.id">
          <el-card class="category-card">
            <div class="category-header">
              <el-tag :color="category.color" size="large">{{ category.name }}</el-tag>
              <span class="post-count">{{ category.postCount }} 帖子</span>
            </div>
            <p class="category-description">{{ category.description }}</p>
            <el-button type="primary" size="small" @click="$router.push(`/categories/${category.id}`)">
              查看帖子
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'Categories',
  setup() {
    const mockCategories = ref([
      { id: 1, name: '技术讨论', description: '分享技术经验和讨论', color: '#409eff', postCount: 25 },
      { id: 2, name: '生活分享', description: '分享生活中的点点滴滴', color: '#67c23a', postCount: 18 },
      { id: 3, name: '学习交流', description: '学习资源和经验分享', color: '#e6a23c', postCount: 32 }
    ])
    
    return {
      mockCategories
    }
  }
}
</script>

<style scoped>
.categories-page {
  max-width: 1200px;
  margin: 0 auto;
}

.category-card {
  margin-bottom: 20px;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.post-count {
  font-size: 12px;
  color: #999;
}

.category-description {
  color: #666;
  margin-bottom: 15px;
}
</style> 