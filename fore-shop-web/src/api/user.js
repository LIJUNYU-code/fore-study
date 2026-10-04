import request from './index'

export const userApi = {
  register: (data) => request.post('/user/register', data),
  login: (data) => request.post('/user/login', data),
  getUserById: (id) => request.get(`/user/${id}`),
  updateUser: (id, data) => request.put(`/user/${id}`, data),
  changePassword: (id, data) => request.put(`/user/${id}/password`, null, { params: data })
}