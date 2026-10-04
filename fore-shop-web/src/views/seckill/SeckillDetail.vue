<template>
  <div class="seckill-detail-page">
    <el-card shadow="never" v-loading="loading">
      <div v-if="activity" class="seckill-detail">
        <el-row :gutter="40">
          <el-col :span="10">
            <div class="detail-image">
              <el-icon :size="100" color="#f56c6c"><Timer /></el-icon>
            </div>
          </el-col>
          <el-col :span="14">
            <h2 class="detail-name">{{ activity.productName || '秒杀商品' }}</h2>

            <div class="countdown-area" v-if="activity.status === 0">
              <span>距离开始：</span>
              <span class="countdown">{{ countdown }}</span>
            </div>
            <div class="countdown-area ongoing" v-else-if="activity.status === 1">
              <el-tag type="danger" effect="dark" size="large">抢购进行中</el-tag>
            </div>
            <div class="countdown-area ended" v-else>
              <el-tag type="info" size="large">活动已结束</el-tag>
            </div>

            <div class="price-area">
              <div class="price-item">
                <span class="label">秒杀价</span>
                <span class="seckill-price">¥{{ activity.seckillPrice }}</span>
              </div>
              <div class="price-item">
                <span class="label">原价</span>
                <span class="original-price">¥{{ activity.originalPrice }}</span>
              </div>
            </div>

            <div class="stock-area">
              <el-progress
                :percentage="((activity.totalStock - activity.availableStock) / activity.totalStock * 100)"
                :stroke-width="16"
                :show-text="false"
                color="#f56c6c"
              />
              <span>剩余 {{ activity.availableStock }} 件 / 共 {{ activity.totalStock }} 件</span>
            </div>

            <div class="limit-info">
              每人限购 {{ activity.limitPerUser }} 件
            </div>

            <el-button
              type="danger"
              size="large"
              class="buy-btn"
              :disabled="activity.status !== 1 || activity.availableStock <= 0"
              :loading="buying"
              @click="handleSeckill"
            >
              {{ activity.status === 0 ? '未开始' : activity.status === 1 ? '立即抢购' : '已结束' }}
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { seckillApi } from '../../api/seckill'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const buying = ref(false)
const activity = ref(null)
const countdown = ref('')
let timer = null

async function loadActivity() {
  loading.value = true
  try {
    const res = await seckillApi.getById(route.params.id)
    activity.value = res.data
    startCountdown()
  } finally {
    loading.value = false
  }
}

function startCountdown() {
  if (timer) clearInterval(timer)
  timer = setInterval(() => {
    if (!activity.value?.startTime) return
    const start = new Date(activity.value.startTime).getTime()
    const now = Date.now()
    const diff = start - now
    if (diff <= 0) {
      countdown.value = '00:00:00'
      clearInterval(timer)
      return
    }
    const h = Math.floor(diff / 3600000)
    const m = Math.floor((diff % 3600000) / 60000)
    const s = Math.floor((diff % 60000) / 1000)
    countdown.value = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }, 1000)
}

async function handleSeckill() {
  buying.value = true
  try {
    const res = await seckillApi.execute(activity.value.id)
    const result = res.data
    if (result === -1) {
      ElMessage.info('排队中，请稍候查询结果...')
      pollResult()
    } else if (result > 0) {
      ElMessage.success('秒杀成功！')
      router.push(`/order/${result}`)
    } else {
      ElMessage.error('秒杀失败，商品已售罄')
    }
  } catch {
  } finally {
    buying.value = false
  }
}

function pollResult() {
  let count = 0
  const pollTimer = setInterval(async () => {
    count++
    if (count > 30) {
      clearInterval(pollTimer)
      ElMessage.warning('查询超时，请到订单列表查看')
      return
    }
    try {
      const res = await seckillApi.getResult(activity.value.id)
      const result = res.data
      if (result > 0) {
        clearInterval(pollTimer)
        ElMessage.success('秒杀成功！')
        router.push(`/order/${result}`)
      } else if (result === -1) {
        clearInterval(pollTimer)
        ElMessage.error('秒杀失败，商品已售罄')
      }
    } catch {
      clearInterval(pollTimer)
    }
  }, 2000)
}

onMounted(() => loadActivity())
onUnmounted(() => { if (timer) clearInterval(timer) })
</script>

<style scoped>
.detail-image {
  height: 360px;
  background: linear-gradient(135deg, #fef0f0, #fde2e2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-name {
  font-size: 22px;
  color: #333;
  margin-bottom: 16px;
}

.countdown-area {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.countdown {
  font-size: 24px;
  font-weight: 700;
  color: #f56c6c;
  font-family: monospace;
}

.price-area {
  display: flex;
  gap: 32px;
  margin-bottom: 20px;
}

.price-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.label {
  font-size: 12px;
  color: #999;
}

.seckill-price {
  font-size: 32px;
  font-weight: 700;
  color: #f56c6c;
}

.original-price {
  font-size: 18px;
  color: #c0c4cc;
  text-decoration: line-through;
}

.stock-area {
  margin-bottom: 12px;
  font-size: 13px;
  color: #666;
}

.limit-info {
  font-size: 13px;
  color: #e6a23c;
  margin-bottom: 20px;
}

.buy-btn {
  width: 200px;
  height: 48px;
  font-size: 18px;
  font-weight: 700;
}
</style>