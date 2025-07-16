import api from "./index";

export const postAPI = {
  // 获取所有帖子
  getAllPosts: (params = {}) => {
    return api.get("/api/posts", { params });
  },

  // 获取帖子详情
  getPostById: (id) => {
    return api.get(`/api/posts/${id}`);
  },

  // 创建帖子
  createPost: (data) => {
    return api.post("/api/posts", data);
  },

  // 更新帖子
  updatePost: (id, data) => {
    return api.put(`/api/posts/${id}`, data);
  },

  // 删除帖子
  deletePost: (id) => {
    return api.delete(`/api/posts/${id}`);
  },

  // 按分类获取帖子
  getPostsByCategory: (categoryId, params = {}) => {
    return api.get(`/api/posts/category/${categoryId}`, { params });
  },

  // 按用户获取帖子
  getPostsByUser: (username, params = {}) => {
    return api.get(`/api/posts/user/${username}`, { params });
  },

  // 搜索帖子
  searchPosts: (keyword, params = {}) => {
    return api.get("/api/posts/search", { params: { keyword, ...params } });
  },

  // 获取热门帖子
  getPopularPosts: () => {
    return api.get("/api/posts/popular");
  },

  // 点赞帖子
  likePost: (id) => {
    return api.post(`/api/posts/${id}/like`);
  },
};
