<template>
  <div class="create-post">
    <h1>发布新帖子</h1>
    <el-card>
      <el-form :model="postData" :rules="rules" ref="postForm" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="postData.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="postData.categoryId" placeholder="请选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="postData.content" type="textarea" :rows="10" placeholder="请输入帖子内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">发布帖子</el-button>
          <el-button @click="$router.go(-1)">取消</el-button>
        </el-form-item>
      </el-form>
      <div v-if="!isAuthenticated" class="login-tip">请 <router-link to="/login">登录</router-link> 后发帖</div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { postAPI } from '@/api/posts'
import { categoryAPI } from '@/api/categories'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

export default {
  name: 'CreatePost',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const isAuthenticated = authStore.isAuthenticated
    const postData = reactive({
      title: '',
      content: '',
      categoryId: ''
    })
    const categories = ref([])
    const loading = ref(false)
    const postForm = ref(null)

    const rules = {
      title: [
        { required: true, message: '请输入标题', trigger: 'blur' },
        { min: 3, max: 100, message: '标题长度3-100字', trigger: 'blur' }
      ],
      categoryId: [
        { required: true, message: '请选择分类', trigger: 'change' }
      ],
      content: [
        { required: true, message: '请输入内容', trigger: 'blur' },
        { min: 10, message: '内容至少10个字', trigger: 'blur' }
      ]
    }

    const fetchCategories = async () => {
      try {
        const res = await categoryAPI.getAllCategories()
        categories.value = res.data
      } catch (e) {
        categories.value = []
      }
    }

    const handleSubmit = async () => {
      if (!isAuthenticated) {
        ElMessage.warning('请先登录')
        return
      }
      if (!postForm.value) return
      try {
        await postForm.value.validate()
        loading.value = true
        const res = await postAPI.createPost({
          title: postData.title,
          content: postData.content,
          categoryId: postData.categoryId
        })
        ElMessage.success('发帖成功')
        router.push(`/posts/${res.data.id}`)
      } catch (e) {
        ElMessage.error('发帖失败')
      }
      loading.value = false
    }

    onMounted(() => {
      fetchCategories()
    })

    return {
      postData,
      categories,
      loading,
      postForm,
      rules,
      handleSubmit,
      isAuthenticated
    }
  }
}
</script>

<style scoped>
.create-post {
  max-width: 800px;
  margin: 0 auto;
}

.login-tip {
  color: #888;
  margin-top: 18px;
}
</style>
