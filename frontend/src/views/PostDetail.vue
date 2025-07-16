<template>
  <div class="post-detail">
    <h1>帖子详情</h1>
    <el-card>
      <p>这里将显示帖子的详细内容、评论等。</p>
      <p>帖子ID: {{ $route.params.id }}</p>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PostDetail'
}
</script>

<style scoped>
.post-detail {
  max-width: 1200px;
  margin: 0 auto;
}
</style> 