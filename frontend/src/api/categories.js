import api from "./index";

export const categoryAPI = {
  // 获取所有分类
  getAllCategories: () => {
    return api.get("/categories");
  },

  // 获取分类详情
  getCategoryById: (id) => {
    return api.get(`/categories/${id}`);
  },

  // 按名称获取分类
  getCategoryByName: (name) => {
    return api.get(`/categories/name/${name}`);
  },

  // 创建分类
  createCategory: (data) => {
    return api.post("/categories", data);
  },

  // 更新分类
  updateCategory: (id, data) => {
    return api.put(`/categories/${id}`, data);
  },

  // 删除分类
  deleteCategory: (id) => {
    return api.delete(`/categories/${id}`);
  },
};
