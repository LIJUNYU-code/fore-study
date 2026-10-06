<template>
  <div class="cart-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>购物车 ({{ cartList.length }}件商品)</span>
          <el-button type="danger" text :icon="Delete" @click="handleClear" v-if="cartList.length">清空购物车</el-button>
        </div>
      </template>

      <el-table :data="cartList" v-loading="loading" @selection-change="handleSelectionChange" row-key="id">
        <el-table-column type="selection" width="55" :reserve-selection="true" />
        <el-table-column label="商品信息" min-width="300">
          <template #default="{ row }">
            <div class="cart-product">
              <div class="cart-img">
                <img v-if="row.productImage" :src="row.productImage" alt="" />
                <el-icon v-else :size="32" color="#c0c4cc"><Picture /></el-icon>
              </div>
              <div class="product-info">
                <span class="product-name">{{ row.productName || '未知商品' }}</span>
                <span class="product-id">商品ID: {{ row.productId }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120" align="center">
          <template #default="{ row }">
            <span class="price">¥{{ row.price ? row.price.toFixed(2) : '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="180" align="center">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" size="small" @change="handleUpdateQuantity(row)" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120" align="center">
          <template #default="{ row }">
            <span class="subtotal">¥{{ (row.price && row.quantity) ? (row.price * row.quantity).toFixed(2) : '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button type="danger" text size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && !cartList.length" description="购物车空空如也" />

      <div class="cart-footer" v-if="cartList.length">
        <div class="footer-left">
          <el-checkbox v-model="allSelected" @change="handleSelectAll">全选</el-checkbox>
        </div>
        <div class="footer-right">
          <span class="total-label">合计：</span>
          <span class="total-price">¥{{ totalPrice }}</span>
          <el-button type="danger" size="large" :disabled="!selectedItems.length || submitting" :loading="submitting" @click="handleCheckout">
            结算 ({{ selectedItems.length }})
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi } from '../../api/cart'
import { orderApi } from '../../api/order'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const cartList = ref([])
const selectedItems = ref([])
const allSelected = ref(false)

const totalPrice = computed(() => {
  return selectedItems.value.reduce((sum, item) => {
    return sum + (item.price || 0) * item.quantity
  }, 0).toFixed(2)
})

async function loadData() {
  loading.value = true
  try {
    const res = await cartApi.list()
    cartList.value = res.data || []
  } finally {
    loading.value = false
  }
}

function handleSelectionChange(val) {
  selectedItems.value = val
  allSelected.value = val.length === cartList.value.length && cartList.value.length > 0
}

async function handleUpdateQuantity(row) {
  try {
    await cartApi.updateQuantity(row.id, row.quantity)
  } catch {
    loadData()
  }
}

async function handleDelete(id) {
  try {
    await cartApi.delete(id)
    ElMessage.success('已删除')
    loadData()
  } catch {}
}

async function handleClear() {
  try {
    await ElMessageBox.confirm('确定清空购物车？', '提示', { type: 'warning' })
    await cartApi.clear()
    ElMessage.success('已清空')
    loadData()
  } catch {}
}

function handleSelectAll(val) {
  allSelected.value = val
  const table = document.querySelector('.el-table__body-wrapper .el-table__body')
  if (table) {
    const rows = table.querySelectorAll('.el-table__row')
    rows.forEach((row, index) => {
      const checkbox = row.querySelector('.el-checkbox__input')
      if (checkbox) {
        checkbox.click()
      }
    })
  }
}

async function handleCheckout() {
  if (!selectedItems.value.length) {
    ElMessage.warning('请选择要结算的商品')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要结算 ${selectedItems.value.length} 件商品吗？`,
      '确认下单',
      { type: 'info' }
    )

    submitting.value = true
    const cartItemIds = selectedItems.value.map(item => item.id)
    const res = await orderApi.create({ cartItemIds })

    ElMessage.success('下单成功！')

    router.push({
      path: `/order/${res.data.id}`,
      query: { newOrder: 'true' }
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '下单失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.cart-product {
  display: flex;
  align-items: center;
  gap: 12px;
}

.cart-img {
  width: 60px;
  height: 60px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
}

.cart-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-name {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.product-id {
  color: #999;
  font-size: 12px;
}

.price {
  color: #333;
  font-weight: 500;
}

.subtotal {
  color: #f56c6c;
  font-weight: 600;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.total-price {
  color: #f56c6c;
  font-size: 24px;
  font-weight: 700;
  margin-right: 20px;
}

.total-label {
  color: #666;
  font-size: 14px;
}
</style>