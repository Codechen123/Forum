import api from "./index";

export const userAPI = {
  // 获取用户信息
  getUserByUsername: (username) => {
    return api.get(`/api/users/${username}`);
  },

  // 获取用户统计信息
  getUserStats: (username) => {
    return api.get(`/api/users/${username}/stats`);
  },

  // 获取当前用户统计信息
  getCurrentUserStats: () => {
    return api.get("/api/users/me/stats");
  },

  // 更新用户资料
  updateProfile: (data) => {
    return api.put("/api/users/me/profile", data);
  },
};