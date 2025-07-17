import api from "./index";

export const postAPI = {
  // 获取所有帖子
  getAllPosts: (params = {}) => {
    return api.get("/posts", { params });
  },

  // 获取帖子详情
  getPostById: (id) => {
    return api.get(`/posts/${id}`);
  },

  // 创建帖子
  createPost: (data) => {
    return api.post("/posts", data);
  },

  // 更新帖子
  updatePost: (id, data) => {
    return api.put(`/posts/${id}`, data);
  },

  // 删除帖子
  deletePost: (id) => {
    return api.delete(`/posts/${id}`);
  },

  // 按分类获取帖子
  getPostsByCategory: (categoryId, params = {}) => {
    return api.get(`/posts/category/${categoryId}`, { params });
  },

  // 按用户获取帖子
  getPostsByUser: (username, params = {}) => {
    return api.get(`/posts/user/${username}`, { params });
  },

  // 搜索帖子
  searchPosts: (keyword, params = {}) => {
    return api.get("/posts/search", { params: { keyword, ...params } });
  },

  // 获取热门帖子
  getPopularPosts: () => {
    return api.get("/posts/popular");
  },

  // 点赞帖子
  likePost: (id) => {
    return api.post(`/posts/${id}/like`);
  },
};
