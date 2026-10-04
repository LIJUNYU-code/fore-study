import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', noLayout: true }
  },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'product',
        name: 'ProductList',
        component: () => import('../views/product/ProductList.vue'),
        meta: { title: '商品列表' }
      },
      {
        path: 'product/:id',
        name: 'ProductDetail',
        component: () => import('../views/product/ProductDetail.vue'),
        meta: { title: '商品详情' }
      },
      {
        path: 'cart',
        name: 'CartList',
        component: () => import('../views/cart/CartList.vue'),
        meta: { title: '购物车' }
      },
      {
        path: 'order',
        name: 'OrderList',
        component: () => import('../views/order/OrderList.vue'),
        meta: { title: '我的订单' }
      },
      {
        path: 'order/:id',
        name: 'OrderDetail',
        component: () => import('../views/order/OrderDetail.vue'),
        meta: { title: '订单详情' }
      },
      {
        path: 'seckill',
        name: 'SeckillList',
        component: () => import('../views/seckill/SeckillList.vue'),
        meta: { title: '秒杀活动' }
      },
      {
        path: 'seckill/:id',
        name: 'SeckillDetail',
        component: () => import('../views/seckill/SeckillDetail.vue'),
        meta: { title: '秒杀详情' }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('../views/user/UserProfile.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || 'ForeShop'} - ForeShop`
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router