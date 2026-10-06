<template>
  <div class="product-detail-page">
    <el-card shadow="never" v-loading="loading">
      <el-row :gutter="40" v-if="product">
        <el-col :span="10">
          <div class="detail-image">
            <el-icon :size="120" color="#c0c4cc"><Picture /></el-icon>
          </div>
        </el-col>
        <el-col :span="14">
          <h2 class="detail-name">{{ product.name }}</h2>
          <p class="detail-desc">{{ product.description || '暂无描述' }}</p>

          <div class="price-area">
            <span class="label">售价</span>
            <span class="detail-price">¥{{ product.price }}</span>
            <span class="detail-original" v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
          </div>

          <div class="info-area">
            <span class="info-item">分类：{{ product.categoryName || '-' }}</span>
            <span class="info-item">库存：{{ product.stock }}</span>
            <span class="info-item">已售：{{ product.sales || 0 }}</span>
          </div>

          <el-divider />

          <div class="action-area">
            <el-input-number v-model="quantity" :min="1" :max="product.stock" size="large" />
            <el-button type="primary" size="large" :icon="ShoppingCart" @click="handleAddCart" style="margin-left: 16px">
              加入购物车
            </el-button>
            <el-button type="danger" size="large" :loading="submitting" @click="handleBuyNow">
              立即购买
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi } from '../../api/product'
import { cartApi } from '../../api/cart'
import { orderApi } from '../../api/order'
import { ShoppingCart } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const product = ref(null)
const quantity = ref(1)

async function loadProduct() {
  loading.value = true
  try {
    const res = await productApi.getById(route.params.id)
    product.value = res.data
  } finally {
    loading.value = false
  }
}

async function handleAddCart() {
  try {
    await cartApi.add(product.value.id, quantity.value)
    ElMessage.success(`已添加 ${quantity.value} 件商品到购物车`)
  } catch (error) {
    ElMessage.error('加入购物车失败')
  }
}

async function handleBuyNow() {
  try {
    submitting.value = true

    const res = await cartApi.add(product.value.id, quantity.value)
    const cartItemId = res.data

    ElMessageBox.confirm(
      `确定购买 ${quantity.value} 件 "${product.value.name}"？`,
      '确认购买',
      { confirmButtonText: '立即下单', cancelButtonText: '再想想', type: 'info' }
    ).then(async () => {
      try {
        const orderRes = await orderApi.create({ cartItemIds: [cartItemId] })
        ElMessage.success('下单成功！')

        router.push({
          path: `/order/${orderRes.data.id}`,
          query: { newOrder: 'true' }
        })
      }).catch(() => {})
    }).catch(() => {})

  } catch (error) {
    console.error('购买失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadProduct())
</script>

<style scoped>
.detail-image {
  height: 400px;
  background: #f5f7fa;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-name {
  font-size: 22px;
  color: #333;
  margin-bottom: 8px;
}

.detail-desc {
  color: #999;
  font-size: 14px;
  margin-bottom: 16px;
}

.price-area {
  background: #fef0f0;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.label {
  color: #999;
  font-size: 14px;
}

.detail-price {
  color: #f56c6c;
  font-size: 28px;
  font-weight: 700;
}

.detail-original {
  color: #c0c4cc;
  font-size: 14px;
  text-decoration: line-through;
}

.info-area {
  display: flex;
  gap: 24px;
  color: #666;
  font-size: 14px;
}

.action-area {
  display: flex;
  align-items: center;
}
</style>