<template>
  <div class="cart-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>购物车 ({{ cartList.length }}件商品)</span>
          <el-button type="danger" text :icon="Delete" @click="handleClear" v-if="cartList.length">清空购物车</el-button>
        </div>
      </template>

      <el-table :data="cartList" v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="商品信息" min-width="300">
          <template #default="{ row }">
            <div class="cart-product">
              <div class="cart-img">
                <el-icon :size="32" color="#c0c4cc"><Picture /></el-icon>
              </div>
              <span>商品ID: {{ row.productId }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120" align="center">
          <template #default>¥--</template>
        </el-table-column>
        <el-table-column label="数量" width="180" align="center">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" size="small" @change="handleUpdateQuantity(row)" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120" align="center">
          <template #default>¥--</template>
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
          <span class="total-price">¥--</span>
          <el-button type="danger" size="large" :disabled="!selectedItems.length" @click="handleCheckout">
            结算 ({{ selectedItems.length }})
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi } from '../../api/cart'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const cartList = ref([])
const selectedItems = ref([])
const allSelected = ref(false)

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
}

function handleCheckout() {
  ElMessage.info('结算功能待后端实现')
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