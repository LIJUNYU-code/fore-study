<template>
  <div class="sidebar">
    <div class="logo-area">
      <el-icon :size="28" color="#409eff"><Shop /></el-icon>
      <span class="logo-text">ForeShop</span>
    </div>

    <el-menu
      :default-active="activeMenu"
      :collapse="false"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409eff"
      router
    >
      <el-menu-item index="/home">
        <el-icon><HomeFilled /></el-icon>
        <template #title>首页</template>
      </el-menu-item>

      <el-sub-menu index="product-group">
        <template #title>
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </template>
        <el-menu-item index="/product">商品列表</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/cart">
        <el-icon><ShoppingCart /></el-icon>
        <template #title>购物车</template>
      </el-menu-item>

      <el-menu-item index="/order">
        <el-icon><List /></el-icon>
        <template #title>我的订单</template>
      </el-menu-item>

      <el-sub-menu index="seckill-group">
        <template #title>
          <el-icon><Timer /></el-icon>
          <span>秒杀专区</span>
        </template>
        <el-menu-item index="/seckill">秒杀活动</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/profile">
        <el-icon><User /></el-icon>
        <template #title>个人中心</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => {
  const path = route.path
  if (path.startsWith('/product') || path.startsWith('/cart')) return path
  if (path.startsWith('/order')) return '/order'
  if (path.startsWith('/seckill')) return '/seckill'
  return path
})
</script>

<style scoped>
.sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo-area {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  flex-shrink: 0;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 1px;
}

.el-menu {
  border-right: none !important;
  flex: 1;
  overflow-y: auto;
}

.el-menu:not(.el-menu--collapse) {
  width: var(--sidebar-width);
}
</style>