<template>
  <div class="seckill-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>⚡ 秒杀专区</span>
          <el-tag type="danger" effect="dark" size="large">限时抢购</el-tag>
        </div>
      </template>

      <el-row :gutter="20" v-loading="loading">
        <el-col :span="8" v-for="item in seckillList" :key="item.id">
          <el-card shadow="hover" class="seckill-card" @click="$router.push(`/seckill/${item.id}`)">
            <div class="seckill-image">
              <el-icon :size="48" color="#c0c4cc"><Picture /></el-icon>
            </div>
            <div class="seckill-info">
              <div class="seckill-name">{{ item.productName || '商品' }}</div>
              <div class="seckill-prices">
                <span class="seckill-price">¥{{ item.seckillPrice }}</span>
                <span class="seckill-original">¥{{ item.originalPrice }}</span>
              </div>
              <div class="seckill-stock">
                <el-progress
                  :percentage="((item.totalStock - item.availableStock) / item.totalStock * 100)"
                  :stroke-width="12"
                  :show-text="false"
                  color="#f56c6c"
                />
                <span class="stock-text">已抢{{ item.totalStock - item.availableStock }}件 / 共{{ item.totalStock }}件</span>
              </div>
              <div class="seckill-time">
                <span>{{ formatTime(item.startTime) }} ~ {{ formatTime(item.endTime) }}</span>
              </div>
              <el-button
                :type="item.status === 1 ? 'danger' : item.status === 0 ? 'warning' : 'info'"
                size="small"
                :disabled="item.status !== 1"
                class="seckill-btn"
              >
                {{ item.status === 1 ? '立即抢购' : item.status === 0 ? '即将开始' : '已结束' }}
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && !seckillList.length" description="暂无秒杀活动" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { seckillApi } from '../../api/seckill'

const loading = ref(false)
const seckillList = ref([])

function formatTime(time) {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 16)
}

async function loadData() {
  loading.value = true
  try {
    const res = await seckillApi.list()
    seckillList.value = res.data || []
  } finally {
    loading.value = false
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
  font-size: 18px;
}

.seckill-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

.seckill-card:hover {
  transform: translateY(-4px);
  border-color: #f56c6c;
}

.seckill-image {
  height: 140px;
  background: linear-gradient(135deg, #fef0f0, #fde2e2);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.seckill-name {
  font-size: 15px;
  color: #333;
  font-weight: 500;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.seckill-prices {
  margin-bottom: 10px;
}

.seckill-price {
  color: #f56c6c;
  font-size: 22px;
  font-weight: 700;
}

.seckill-original {
  color: #c0c4cc;
  font-size: 13px;
  text-decoration: line-through;
  margin-left: 8px;
}

.seckill-stock {
  margin-bottom: 8px;
}

.stock-text {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  display: block;
}

.seckill-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.seckill-btn {
  width: 100%;
}
</style>