import api from "./index";

export const commentAPI = {
  // 获取帖子的评论
  getCommentsByPost: (postId) => {
    return api.get(`/api/comments/post/${postId}`);
  },

  // 分页获取帖子的评论
  getCommentsByPostPaged: (postId, params = {}) => {
    return api.get(`/api/comments/post/${postId}/page`, { params });
  },

  // 获取用户的评论
  getCommentsByUser: (username, params = {}) => {
    return api.get(`/api/comments/user/${username}`, { params });
  },

  // 获取评论详情
  getCommentById: (id) => {
    return api.get(`/api/comments/${id}`);
  },

  // 创建评论
  createComment: (data) => {
    return api.post("/api/comments", data);
  },

  // 更新评论
  updateComment: (id, data) => {
    return api.put(`/api/comments/${id}`, data);
  },

  // 删除评论
  deleteComment: (id) => {
    return api.delete(`/api/comments/${id}`);
  },

  // 点赞评论
  likeComment: (id) => {
    return api.post(`/api/comments/${id}/like`);
  },

  // 获取评论的回复
  getRepliesByParent: (parentId) => {
    return api.get(`/api/comments/${parentId}/replies`);
  },
};
