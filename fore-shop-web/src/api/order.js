import request from './index'

export const orderApi = {
  create: (data) => request.post('/order', data),
  getById: (id) => request.get(`/order/${id}`),
  list: (params) => request.get('/order/list', { params }),
  cancel: (id) => request.put(`/order/${id}/cancel`),
  pay: (id) => request.put(`/order/${id}/pay`)
}