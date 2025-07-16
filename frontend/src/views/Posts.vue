<template>
  <div class="posts-page">
    <h1>帖子列表</h1>
    <el-card>
      <p>这里将显示所有帖子的列表，包括搜索、分页等功能。</p>
      <el-button type="primary" @click="$router.push('/create-post')">发布新帖子</el-button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Posts'
}
</script>

<style scoped>
.posts-page {
  max-width: 1200px;
  margin: 0 auto;
}
</style> 