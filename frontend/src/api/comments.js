import api from "./index";

export const commentAPI = {
  // 获取帖子的评论
  getCommentsByPost: (postId) => {
    return api.get(`/comments/post/${postId}`);
  },

  // 分页获取帖子的评论
  getCommentsByPostPaged: (postId, params = {}) => {
    return api.get(`/comments/post/${postId}/page`, { params });
  },

  // 获取用户的评论
  getCommentsByUser: (username, params = {}) => {
    return api.get(`/comments/user/${username}`, { params });
  },

  // 获取评论详情
  getCommentById: (id) => {
    return api.get(`/comments/${id}`);
  },

  // 创建评论
  createComment: (data) => {
    return api.post("/comments", data);
  },

  // 更新评论
  updateComment: (id, data) => {
    return api.put(`/comments/${id}`, data);
  },

  // 删除评论
  deleteComment: (id) => {
    return api.delete(`/comments/${id}`);
  },

  // 点赞评论
  likeComment: (id) => {
    return api.post(`/comments/${id}/like`);
  },

  // 获取评论的回复
  getRepliesByParent: (parentId) => {
    return api.get(`/comments/${parentId}/replies`);
  },
};
