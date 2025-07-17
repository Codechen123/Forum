<template>
  <div class="post-detail">
    <el-card v-if="post" class="post-card">
      <div class="post-header">
        <h1 class="post-title">{{ post.title }}</h1>
        <div class="post-meta">
          <el-tag v-if="post.category" size="small" :color="post.category.color">{{ post.category.name }}</el-tag>
          <span class="author">by {{ post.author.username }}</span>
          <span class="date">{{ formatDate(post.createdAt) }}</span>
        </div>
      </div>
      <div class="post-content">{{ post.content }}</div>
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
        <el-button type="text" @click="handleLike" :loading="likeLoading">
          <el-icon>
            <Star />
          </el-icon> 点赞
        </el-button>
      </div>
    </el-card>
    <el-card class="comments-card">
      <template #header>
        <div class="comments-header">
          <span>评论 ({{ comments.length }})</span>
        </div>
      </template>
      <div v-if="isAuthenticated" class="comment-form">
        <el-form :model="commentData" :rules="commentRules" ref="commentForm" @submit.prevent="handleComment">
          <el-form-item prop="content">
            <el-input v-model="commentData.content" type="textarea" :rows="3" placeholder="写下你的评论..." />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleComment" :loading="commentLoading">发表评论</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-else class="login-tip">请 <router-link to="/login">登录</router-link> 后参与评论</div>
      <div class="comments-list">
        <template v-if="!commentsLoading && comments.length === 0">
          <div class="empty-tip">暂无评论</div>
        </template>
        <div v-else>
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-meta">
              <span class="author">{{ comment.author.username }}</span>
              <span class="date">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { postAPI } from '@/api/posts'
import { commentAPI } from '@/api/comments'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import { View, Star, ChatLineSquare } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

export default {
  name: 'PostDetail',
  components: { View, Star, ChatLineSquare },
  setup() {
    const route = useRoute()
    const postId = route.params.id
    const post = ref(null)
    const comments = ref([])
    const commentsLoading = ref(true)
    const likeLoading = ref(false)
    const commentLoading = ref(false)
    const commentData = ref({ content: '' })
    const commentForm = ref(null)
    const authStore = useAuthStore()
    const isAuthenticated = authStore.isAuthenticated

    const commentRules = {
      content: [
        { required: true, message: '评论不能为空', trigger: 'blur' },
        { min: 2, message: '评论至少2个字', trigger: 'blur' }
      ]
    }

    const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

    const fetchPost = async () => {
      try {
        const res = await postAPI.getPostById(postId)
        post.value = res.data
      } catch (e) {
        post.value = null
      }
    }

    const fetchComments = async () => {
      commentsLoading.value = true
      try {
        const res = await commentAPI.getCommentsByPost(postId)
        comments.value = res.data
      } catch (e) {
        comments.value = []
      }
      commentsLoading.value = false
    }

    const handleLike = async () => {
      if (!isAuthenticated) {
        ElMessage.warning('请先登录')
        return
      }
      likeLoading.value = true
      try {
        await postAPI.likePost(postId)
        ElMessage.success('点赞成功')
        await fetchPost()
      } catch (e) {
        ElMessage.error('点赞失败')
      }
      likeLoading.value = false
    }

    const handleComment = async () => {
      if (!commentForm.value) return
      try {
        await commentForm.value.validate()
        commentLoading.value = true
        await commentAPI.createComment({ postId, content: commentData.value.content })
        ElMessage.success('评论成功')
        commentData.value.content = ''
        await fetchComments()
        await fetchPost()
      } catch (e) {
        ElMessage.error('评论失败')
      }
      commentLoading.value = false
    }

    onMounted(async () => {
      await fetchPost()
      await fetchComments()
    })

    return {
      post,
      comments,
      commentsLoading,
      likeLoading,
      commentLoading,
      commentData,
      commentForm,
      isAuthenticated,
      commentRules,
      formatDate,
      handleLike,
      handleComment
    }
  }
}
</script>

<style scoped>
.post-detail {
  max-width: 900px;
  margin: 0 auto;
}

.post-card {
  margin-bottom: 24px;
}

.post-header {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.post-title {
  font-size: 28px;
  color: #409eff;
  margin: 0;
}

.post-meta {
  font-size: 14px;
  color: #888;
  display: flex;
  gap: 10px;
  align-items: center;
}

.post-content {
  margin: 18px 0 8px 0;
  font-size: 17px;
  color: #333;
  line-height: 1.7;
}

.post-stats {
  font-size: 13px;
  color: #aaa;
  display: flex;
  gap: 18px;
  align-items: center;
  margin-top: 8px;
}

.comments-card {
  margin-bottom: 24px;
}

.comments-header {
  font-size: 18px;
  font-weight: bold;
}

.comment-form {
  margin-bottom: 18px;
}

.login-tip {
  color: #888;
  margin-bottom: 18px;
}

.comments-list {
  min-height: 80px;
}

.comment-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}

.comment-meta {
  font-size: 13px;
  color: #888;
  margin-bottom: 4px;
  display: flex;
  gap: 10px;
}

.comment-content {
  font-size: 15px;
  color: #444;
}

.empty-tip {
  text-align: center;
  color: #aaa;
  padding: 24px 0;
  font-size: 15px;
}
</style>
