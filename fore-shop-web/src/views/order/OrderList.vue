<template>
  <div class="order-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>我的订单</span>
          <div class="filter-tabs">
            <el-radio-group v-model="statusFilter" size="small" @change="loadData">
              <el-radio-button :value="null">全部</el-radio-button>
              <el-radio-button :value="0">待支付</el-radio-button>
              <el-radio-button :value="1">已支付</el-radio-button>
              <el-radio-button :value="2">已发货</el-radio-button>
              <el-radio-button :value="3">已完成</el-radio-button>
              <el-radio-button :value="4">已取消</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>

      <div v-loading="loading">
        <div v-for="order in orderList" :key="order.id" class="order-card" @click="$router.push(`/order/${order.id}`)">
          <div class="order-header">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <el-tag :type="statusMap[order.status]?.type || 'info'" size="small">
              {{ statusMap[order.status]?.label || '未知' }}
            </el-tag>
          </div>
          <div class="order-body">
            <div class="order-items" v-if="order.orderItems?.length">
              <span v-for="item in order.orderItems" :key="item.id" class="order-item-name">
                {{ item.productName }} × {{ item.quantity }}
              </span>
            </div>
            <div class="order-amount">¥{{ order.totalAmount }}</div>
          </div>
          <div class="order-footer">
            <span class="order-time">{{ order.createTime }}</span>
            <div class="order-actions">
              <el-button v-if="order.status === 0" type="primary" size="small" @click.stop="handlePay(order.id)">支付</el-button>
              <el-button v-if="order.status === 0" type="danger" size="small" @click.stop="handleCancel(order.id)">取消</el-button>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && !orderList.length" description="暂无订单" />
      </div>

      <div class="pagination-area" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :total="total"
          :page-size="10"
          layout="total, prev, pager, next"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderApi } from '../../api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const orderList = ref([])
const statusFilter = ref(null)
const page = ref(1)
const total = ref(0)

const statusMap = {
  0: { label: '待支付', type: 'warning' },
  1: { label: '已支付', type: 'primary' },
  2: { label: '已发货', type: '' },
  3: { label: '已完成', type: 'success' },
  4: { label: '已取消', type: 'info' },
  5: { label: '已退款', type: 'danger' }
}

async function loadData() {
  loading.value = true
  try {
    const res = await orderApi.list({
      status: statusFilter.value ?? undefined,
      page: page.value,
      size: 10
    })
    orderList.value = res.data || []
    total.value = res.data?.length || 0
  } finally {
    loading.value = false
  }
}

async function handlePay(id) {
  try {
    await ElMessageBox.confirm('确认支付该订单？', '支付确认')
    await orderApi.pay(id)
    ElMessage.success('支付成功')
    loadData()
  } catch {}
}

async function handleCancel(id) {
  try {
    await ElMessageBox.confirm('确认取消该订单？', '取消确认', { type: 'warning' })
    await orderApi.cancel(id)
    ElMessage.success('已取消')
    loadData()
  } catch {}
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

.order-card {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.order-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.order-no {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-item-name {
  font-size: 13px;
  color: #666;
  margin-right: 12px;
}

.order-amount {
  font-size: 20px;
  color: #f56c6c;
  font-weight: 700;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.order-time {
  font-size: 12px;
  color: #999;
}

.pagination-area {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>