import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const routes = [
  {
    path: "/",
    name: "Home",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue"),
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/views/Register.vue"),
  },
  {
    path: "/posts",
    name: "Posts",
    component: () => import("@/views/Posts.vue"),
  },
  {
    path: "/posts/:id",
    name: "PostDetail",
    component: () => import("@/views/PostDetail.vue"),
  },
  {
    path: "/create-post",
    name: "CreatePost",
    component: () => import("@/views/CreatePost.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/categories",
    name: "Categories",
    component: () => import("@/views/Categories.vue"),
  },
  {
    path: "/categories/:id",
    name: "CategoryPosts",
    component: () => import("@/views/CategoryPosts.vue"),
  },
  {
    path: "/profile",
    name: "Profile",
    component: () => import("@/views/Profile.vue"),
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next("/login");
  } else {
    next();
  }
});

export default router;
