import api from "./index";

export const categoryAPI = {
  // 获取所有分类
  getAllCategories: () => {
    return api.get("/api/categories");
  },

  // 获取分类详情
  getCategoryById: (id) => {
    return api.get(`/api/categories/${id}`);
  },

  // 按名称获取分类
  getCategoryByName: (name) => {
    return api.get(`/api/categories/name/${name}`);
  },

  // 创建分类
  createCategory: (data) => {
    return api.post("/api/categories", data);
  },

  // 更新分类
  updateCategory: (id, data) => {
    return api.put(`/api/categories/${id}`, data);
  },

  // 删除分类
  deleteCategory: (id) => {
    return api.delete(`/api/categories/${id}`);
  },
};
