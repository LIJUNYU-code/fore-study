import request from './index'

export const seckillApi = {
  list: () => request.get('/seckill/list'),
  getById: (id) => request.get(`/seckill/${id}`),
  execute: (seckillActivityId) => request.post('/seckill/execute', null, { params: { seckillActivityId } }),
  getResult: (seckillActivityId) => request.get('/seckill/result', { params: { seckillActivityId } })
}