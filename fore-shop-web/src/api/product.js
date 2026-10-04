import request from './index'

export const productApi = {
  create: (data) => request.post('/product', data),
  getById: (id) => request.get(`/product/${id}`),
  list: (params) => request.get('/product/list', { params }),
  update: (id, data) => request.put(`/product/${id}`, data),
  toggleStatus: (id) => request.put(`/product/${id}/status`)
}

export const categoryApi = {
  create: (data) => request.post('/category', data),
  tree: () => request.get('/category/tree'),
  update: (id, data) => request.put(`/category/${id}`, data),
  delete: (id) => request.delete(`/category/${id}`)
}