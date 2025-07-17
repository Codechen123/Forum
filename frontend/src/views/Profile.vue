<template>
  <div class="profile-page">
    <h1>个人资料</h1>
    
    <el-card class="profile-card">
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
          <div class="stat-number">{{ stats?.postCount || 0 }}</div>
          <div class="stat-label">发帖数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats?.commentCount || 0 }}</div>
          <div class="stat-label">评论数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats?.likeCount || 0 }}</div>
          <div class="stat-label">获赞数</div>
        </div>
      </div>

      <el-divider />

      <div class="profile-actions">
        <el-button type="primary" @click="showEditDialog">编辑资料</el-button>
        <el-button @click="viewMyPosts">我的帖子</el-button>
        <el-button @click="viewMyComments">我的评论</el-button>
      </div>
    </el-card>

    <!-- 编辑资料对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑资料" width="500px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="80px">
        <el-form-item label="个人简介" prop="bio">
          <el-input
            v-model="editForm.bio"
            type="textarea"
            :rows="4"
            placeholder="请输入个人简介"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="头像URL" prop="avatar">
          <el-input
            v-model="editForm.avatar"
            placeholder="请输入头像URL（可选）"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateProfile" :loading="updating">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 我的帖子对话框 -->
    <el-dialog v-model="postsDialogVisible" title="我的帖子" width="800px">
      <div v-if="loadingPosts" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      <div v-else-if="myPosts.length === 0" class="empty-container">
        <el-empty description="暂无帖子" />
      </div>
      <div v-else class="posts-list">
        <div 
          v-for="post in myPosts" 
          :key="post.id" 
          class="post-item"
          @click="$router.push(`/posts/${post.id}`)"
        >
          <h4>{{ post.title }}</h4>
          <p class="post-meta">
            {{ formatDate(post.createdAt) }} | 
            {{ post.viewCount }} 浏览 | 
            {{ post.commentCount }} 评论
          </p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { userAPI } from '@/api/users'
import { postAPI } from '@/api/posts'
import { ElMessage } from 'element-plus'

export default {
  name: 'Profile',
  setup() {
    const authStore = useAuthStore()
    const user = computed(() => authStore.user)
    
    const stats = ref(null)
    const editDialogVisible = ref(false)
    const postsDialogVisible = ref(false)
    const updating = ref(false)
    const loadingPosts = ref(false)
    const myPosts = ref([])
    
    const editForm = ref({
      bio: '',
      avatar: ''
    })
    
    const editFormRef = ref(null)
    
    const editRules = {
      bio: [
        { max: 200, message: '个人简介不能超过200个字符', trigger: 'blur' }
      ],
      avatar: [
        { type: 'url', message: '请输入有效的URL', trigger: 'blur' }
      ]
    }
    
    const fetchUserStats = async () => {
      try {
        const response = await userAPI.getCurrentUserStats()
        stats.value = response.data
      } catch (error) {
        console.error('Error fetching user stats:', error)
      }
    }
    
    const showEditDialog = () => {
      editForm.value = {
        bio: user.value?.bio || '',
        avatar: user.value?.avatar || ''
      }
      editDialogVisible.value = true
    }
    
    const updateProfile = async () => {
      if (!editFormRef.value) return
      
      try {
        await editFormRef.value.validate()
        updating.value = true
        
        const response = await userAPI.updateProfile(editForm.value)
        
        // 更新本地用户信息
        authStore.user = response.data
        localStorage.setItem('user', JSON.stringify(response.data))
        
        ElMessage.success('资料更新成功')
        editDialogVisible.value = false
      } catch (error) {
        if (error.response?.data) {
          ElMessage.error(error.response.data)
        } else {
          ElMessage.error('更新失败，请重试')
        }
      } finally {
        updating.value = false
      }
    }
    
    const viewMyPosts = async () => {
      if (!user.value?.username) return
      
      loadingPosts.value = true
      postsDialogVisible.value = true
      
      try {
        const response = await postAPI.getPostsByUser(user.value.username, {
          page: 0,
          size: 20
        })
        myPosts.value = response.data.content
      } catch (error) {
        ElMessage.error('获取我的帖子失败')
        console.error('Error fetching my posts:', error)
      } finally {
        loadingPosts.value = false
      }
    }
    
    const viewMyComments = () => {
      // 这里可以添加查看我的评论的功能
      ElMessage.info('该功能正在开发中')
    }
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    }
    
    onMounted(() => {
      fetchUserStats()
    })
    
    return {
      user,
      stats,
      editDialogVisible,
      postsDialogVisible,
      updating,
      loadingPosts,
      myPosts,
      editForm,
      editFormRef,
      editRules,
      showEditDialog,
      updateProfile,
      viewMyPosts,
      viewMyComments,
      formatDate
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-page h1 {
  text-align: center;
  color: #409eff;
  margin-bottom: 30px;
}

.profile-card {
  padding: 20px;
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
  justify-content: center;
}

.loading-container,
.empty-container {
  margin: 20px 0;
}

.posts-list {
  max-height: 400px;
  overflow-y: auto;
}

.post-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.3s;
}

.post-item:hover {
  background-color: #f5f5f5;
}

.post-item h4 {
  margin: 0 0 8px 0;
  color: #333;
}

.post-meta {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .profile-page {
    padding: 10px;
  }
  
  .profile-info {
    flex-direction: column;
    text-align: center;
  }
  
  .user-stats {
    flex-direction: column;
    gap: 15px;
  }
  
  .profile-actions {
    flex-direction: column;
  }
}
</style>