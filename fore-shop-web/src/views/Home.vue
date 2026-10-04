<template>
  <div class="home-page">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statCards" :key="item.title">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" :style="{ backgroundColor: item.color + '20', color: item.color }">
            <el-icon :size="32"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-title">{{ item.title }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>热门商品</span>
              <el-button text type="primary" @click="$router.push('/product')">查看更多</el-button>
            </div>
          </template>
          <el-row :gutter="16">
            <el-col :span="8" v-for="p in hotProducts" :key="p.id">
              <div class="product-card" @click="$router.push(`/product/${p.id}`)">
                <div class="product-img">
                  <el-icon :size="48" color="#c0c4cc"><Picture /></el-icon>
                </div>
                <div class="product-name">{{ p.name }}</div>
                <div class="product-price">
                  <span class="price">¥{{ p.price }}</span>
                  <span class="original-price" v-if="p.originalPrice">¥{{ p.originalPrice }}</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>秒杀活动</span>
              <el-button text type="primary" @click="$router.push('/seckill')">更多</el-button>
            </div>
          </template>
          <div v-for="s in seckillList" :key="s.id" class="seckill-item" @click="$router.push(`/seckill/${s.id}`)">
            <div class="seckill-info">
              <div class="seckill-name">{{ s.productName || '商品' }}</div>
              <div class="seckill-prices">
                <span class="seckill-price">¥{{ s.seckillPrice }}</span>
                <span class="seckill-original">¥{{ s.originalPrice }}</span>
              </div>
            </div>
            <el-tag :type="s.status === 1 ? 'danger' : s.status === 0 ? 'warning' : 'info'" size="small">
              {{ s.status === 1 ? '进行中' : s.status === 0 ? '即将开始' : '已结束' }}
            </el-tag>
          </div>
          <el-empty v-if="!seckillList.length" description="暂无秒杀活动" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productApi } from '../api/product'
import { seckillApi } from '../api/seckill'

const hotProducts = ref([])
const seckillList = ref([])

const statCards = ref([
  { title: '商品总数', value: '-', icon: 'Goods', color: '#409eff' },
  { title: '秒杀活动', value: '-', icon: 'Timer', color: '#f56c6c' },
  { title: '购物车', value: '0', icon: 'ShoppingCart', color: '#e6a23c' },
  { title: '我的订单', value: '0', icon: 'List', color: '#67c23a' }
])

onMounted(async () => {
  try {
    const [productRes, seckillRes] = await Promise.all([
      productApi.list({ page: 1, size: 6 }),
      seckillApi.list()
    ])
    hotProducts.value = productRes.data || []
    seckillList.value = (seckillRes.data || []).slice(0, 4)
    statCards.value[0].value = hotProducts.value.length
    statCards.value[1].value = seckillList.value.length
  } catch {}
})
</script>

<style scoped>
.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
}

.stat-title {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.product-card {
  cursor: pointer;
  padding: 12px;
  border-radius: 8px;
  transition: all 0.3s;
  text-align: center;
}

.product-card:hover {
  background: #f5f7fa;
  transform: translateY(-2px);
}

.product-img {
  height: 120px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.product-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  margin-top: 4px;
}

.price {
  color: #f56c6c;
  font-size: 16px;
  font-weight: 700;
}

.original-price {
  color: #c0c4cc;
  font-size: 12px;
  text-decoration: line-through;
  margin-left: 6px;
}

.seckill-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s;
}

.seckill-item:hover {
  background: #f5f7fa;
}

.seckill-item:last-child {
  border-bottom: none;
}

.seckill-price {
  color: #f56c6c;
  font-weight: 700;
  font-size: 16px;
}

.seckill-original {
  color: #c0c4cc;
  font-size: 12px;
  text-decoration: line-through;
  margin-left: 6px;
}

.seckill-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}
</style>