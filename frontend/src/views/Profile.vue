<template>
  <div class="profile-page">
    <h1>个人资料</h1>
    <el-card>
      <div class="profile-info">
        <el-avatar :size="80" :src="user?.avatar">
          {{ user?.username?.charAt(0)?.toUpperCase() }}
        </el-avatar>
        <div class="user-details">
          <h2>{{ user?.username }}</h2>
          <p>{{ user?.email }}</p>
          <p>{{ user?.bio || '暂无个人简介' }}</p>
          <el-tag>{{ user?.role }}</el-tag>
        </div>
      </div>

      <el-divider />

      <div class="user-stats">
        <div class="stat-item">
          <div class="stat-number">0</div>
          <div class="stat-label">发帖数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">0</div>
          <div class="stat-label">评论数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">0</div>
          <div class="stat-label">获赞数</div>
        </div>
      </div>

      <el-divider />

      <div class="profile-actions">
        <el-button type="primary">编辑资料</el-button>
        <el-button>我的帖子</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'Profile',
  setup() {
    const authStore = useAuthStore()
    const user = computed(() => authStore.user)

    return {
      user
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 800px;
  margin: 0 auto;
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.user-details h2 {
  margin: 0 0 8px 0;
  color: #409eff;
}

.user-details p {
  margin: 4px 0;
  color: #666;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
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

.profile-actions {
  display: flex;
  gap: 10px;
}
</style>