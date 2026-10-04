import request from './index'

export const cartApi = {
  add: (productId, quantity = 1) => request.post('/cart', null, { params: { productId, quantity } }),
  list: () => request.get('/cart/list'),
  updateQuantity: (id, quantity) => request.put(`/cart/${id}/quantity`, null, { params: { quantity } }),
  toggleSelected: (id) => request.put(`/cart/${id}/selected`),
  selectAll: (selected) => request.put('/cart/select-all', null, { params: { selected } }),
  delete: (id) => request.delete(`/cart/${id}`),
  clear: () => request.delete('/cart/clear')
}