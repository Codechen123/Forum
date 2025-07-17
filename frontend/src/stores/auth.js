import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "@/api/index";

export const useAuthStore = defineStore("auth", () => {
  const token = ref(localStorage.getItem("token") || "");
  const user = ref(JSON.parse(localStorage.getItem("user") || "null"));

  const isAuthenticated = computed(() => !!token.value);

  const login = async (credentials) => {
    try {
      const response = await api.post("/auth/login", credentials);
      const { token: newToken, user: newUser } = response.data;

      token.value = newToken;
      user.value = newUser;

      localStorage.setItem("token", newToken);
      localStorage.setItem("user", JSON.stringify(newUser));

      return response.data;
    } catch (error) {
      throw error.response?.data || "登录失败";
    }
  };

  const register = async (userData) => {
    try {
      const response = await api.post("/auth/register", userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || "注册失败";
    }
  };

  const logout = () => {
    token.value = "";
    user.value = null;

    localStorage.removeItem("token");
    localStorage.removeItem("user");
  };

  const initAuth = () => {
    // 初始化时不需要设置axios header，因为已经在api/index.js中的拦截器中处理
  };

  return {
    token,
    user,
    isAuthenticated,
    login,
    register,
    logout,
    initAuth,
  };
});
