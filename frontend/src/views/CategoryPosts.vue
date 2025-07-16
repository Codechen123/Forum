<template>
  <div class="category-posts">
    <h1>分类帖子</h1>
    <el-card>
      <p>这里将显示特定分类下的所有帖子。</p>
      <p>分类ID: {{ $route.params.id }}</p>
      <el-button type="primary" @click="$router.go(-1)">返回</el-button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CategoryPosts'
}
</script>

<style scoped>
.category-posts {
  max-width: 1200px;
  margin: 0 auto;
}
</style> 