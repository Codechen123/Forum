<template>
  <div class="create-post">
    <h1>发布新帖子</h1>
    <el-card>
      <el-form :model="postData" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="postData.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="postData.categoryId" placeholder="请选择分类">
            <el-option label="技术讨论" value="1" />
            <el-option label="生活分享" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="postData.content"
            type="textarea"
            :rows="10"
            placeholder="请输入帖子内容"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">发布帖子</el-button>
          <el-button @click="$router.go(-1)">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'CreatePost',
  setup() {
    const postData = reactive({
      title: '',
      content: '',
      categoryId: ''
    })
    
    const handleSubmit = () => {
      ElMessage.success('帖子发布功能正在开发中...')
    }
    
    return {
      postData,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.create-post {
  max-width: 800px;
  margin: 0 auto;
}
</style> 