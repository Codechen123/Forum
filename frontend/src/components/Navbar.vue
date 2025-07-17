<template>
  <div class="navbar">
    <el-container>
      <el-header style="padding: 0; height: 60px;">
        <div class="nav-content">
          <div class="nav-left">
            <router-link to="/" class="logo">
              <el-icon size="24"><House /></el-icon>
              <span>论坛</span>
            </router-link>
            <el-menu
              :default-active="activeIndex"
              mode="horizontal"
              @select="handleSelect"
              background-color="#409eff"
              text-color="#fff"
              active-text-color="#ffd04b"
            >
              <el-menu-item index="1">
                <router-link to="/">首页</router-link>
              </el-menu-item>
              <el-menu-item index="2">
                <router-link to="/posts">帖子</router-link>
              </el-menu-item>
              <el-sub-menu index="3">
                <template #title>分类</template>
                <el-menu-item index="3-0">
                  <router-link to="/categories">所有分类</router-link>
                </el-menu-item>
                <template v-if="categories.length > 0">
                  <el-menu-item 
                    v-for="category in categories.slice(0, 8)" 
                    :key="category.id" 
                    :index="`3-${category.id}`"
                  >
                    <router-link :to="`/categories/${category.id}`">
                      {{ category.name }}
                    </router-link>
                  </el-menu-item>
                </template>
                <el-menu-item v-else index="3-loading">
                  <span style="color: #999;">加载中...</span>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </div>
          
          <div class="nav-right">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索帖子..."
              prefix-icon="Search"
              @keyup.enter="handleSearch"
              style="width: 200px; margin-right: 20px;"
            />
            
            <div v-if="!isAuthenticated" class="auth-buttons">
              <el-button type="primary" @click="$router.push('/login')">登录</el-button>
              <el-button type="success" @click="$router.push('/register')">注册</el-button>
            </div>
            
            <div v-else class="user-menu">
              <el-button type="primary" @click="$router.push('/create-post')">
                <el-icon><Plus /></el-icon>
                发帖
              </el-button>
              <el-dropdown @command="handleCommand">
                <span class="user-info">
                  <el-avatar :size="30" :src="user?.avatar">
                    {{ user?.username?.charAt(0)?.toUpperCase() }}
                  </el-avatar>
                  <span class="username">{{ user?.username }}</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">
                      <el-icon><User /></el-icon>
                      个人资料
                    </el-dropdown-item>
                    <el-dropdown-item command="my-posts">
                      <el-icon><Document /></el-icon>
                      我的帖子
                    </el-dropdown-item>
                    <el-dropdown-item divided command="logout">
                      <el-icon><SwitchButton /></el-icon>
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
      </el-header>
    </el-container>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { categoryAPI } from '@/api/categories'
import { House, Plus, User, Document, SwitchButton, Search } from '@element-plus/icons-vue'

export default {
  name: 'Navbar',
  components: {
    House,
    Plus,
    User,
    Document,
    SwitchButton,
    Search
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const authStore = useAuthStore()
    
    const activeIndex = ref('1')
    const searchKeyword = ref('')
    const categories = ref([])
    
    const isAuthenticated = computed(() => authStore.isAuthenticated)
    const user = computed(() => authStore.user)
    
    const handleSelect = (key) => {
      console.log('Selected menu:', key)
    }
    
    const handleSearch = () => {
      if (searchKeyword.value.trim()) {
        router.push({
          name: 'Posts',
          query: { search: searchKeyword.value }
        })
      }
    }
    
    const handleCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'my-posts':
          router.push({
            name: 'Posts',
            query: { user: user.value.username }
          })
          break
        case 'logout':
          authStore.logout()
          router.push('/')
          break
      }
    }
    
    const fetchCategories = async () => {
      try {
        const response = await categoryAPI.getAllCategories()
        categories.value = response.data
      } catch (error) {
        console.error('Failed to fetch categories:', error)
        categories.value = []
      }
    }
    
    onMounted(() => {
      authStore.initAuth()
      fetchCategories()
    })
    
    return {
      activeIndex,
      searchKeyword,
      categories,
      isAuthenticated,
      user,
      handleSelect,
      handleSearch,
      handleCommand
    }
  }
}
</script>

<style scoped>
.navbar {
  background-color: #409eff;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.nav-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  color: white;
  text-decoration: none;
  margin-right: 20px;
  font-size: 18px;
  font-weight: bold;
}

.logo span {
  margin-left: 8px;
}

.nav-right {
  display: flex;
  align-items: center;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
}

.username {
  margin-left: 8px;
}

.el-menu--horizontal {
  border-bottom: none;
}

.el-menu--horizontal .el-menu-item {
  border-bottom: none;
}

.el-menu--horizontal .el-menu-item a {
  color: inherit;
  text-decoration: none;
}

.el-menu--horizontal .el-sub-menu .el-sub-menu__title {
  color: white;
}

.el-menu--horizontal .el-sub-menu .el-menu-item a {
  color: #333;
  text-decoration: none;
}

.el-menu--horizontal .el-sub-menu .el-menu-item a:hover {
  color: #409eff;
}
</style> 