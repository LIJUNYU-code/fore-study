<template>
  <div class="order-detail-page">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div class="page-header">
          <span>订单详情</span>
          <el-button text @click="$router.back()">返回</el-button>
        </div>
      </template>

      <div v-if="order">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单编号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="statusMap[order.status]?.type || 'info'" size="small">
              {{ statusMap[order.status]?.label || '未知' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ order.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="实付金额">¥{{ order.payAmount }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ order.createTime }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ order.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <h4 style="margin: 20px 0 12px">商品清单</h4>
        <el-table :data="order.orderItems || []" border>
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="price" label="单价" width="120" align="center">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" align="center" />
          <el-table-column label="小计" width="120" align="center">
            <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
          </el-table-column>
        </el-table>

        <div class="action-area" v-if="order.status === 0">
          <el-button type="primary" size="large" @click="handlePay">立即支付</el-button>
          <el-button size="large" @click="handleCancel">取消订单</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '../../api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref(null)

const statusMap = {
  0: { label: '待支付', type: 'warning' },
  1: { label: '已支付', type: 'primary' },
  2: { label: '已发货', type: '' },
  3: { label: '已完成', type: 'success' },
  4: { label: '已取消', type: 'info' },
  5: { label: '已退款', type: 'danger' }
}

async function loadOrder() {
  loading.value = true
  try {
    const res = await orderApi.getById(route.params.id)
    order.value = res.data
  } finally {
    loading.value = false
  }
}

async function handlePay() {
  try {
    await ElMessageBox.confirm('确认支付？', '支付确认')
    await orderApi.pay(order.value.id)
    ElMessage.success('支付成功')
    loadOrder()
  } catch {}
}

async function handleCancel() {
  try {
    await ElMessageBox.confirm('确认取消订单？', '取消确认', { type: 'warning' })
    await orderApi.cancel(order.value.id)
    ElMessage.success('已取消')
    loadOrder()
  } catch {}
}

onMounted(() => loadOrder())
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.action-area {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
  gap: 12px;
}
</style>