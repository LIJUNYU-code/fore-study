<template>
  <div class="product-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>商品列表</span>
          <div class="filter-area">
            <el-input v-model="keyword" placeholder="搜索商品" :prefix-icon="Search" clearable style="width: 200px" @clear="loadData" @keyup.enter="loadData" />
            <el-select v-model="categoryId" placeholder="全部分类" clearable style="width: 150px; margin-left: 12px" @change="loadData">
              <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
          </div>
        </div>
      </template>

      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="p in products" :key="p.id">
          <el-card shadow="hover" class="product-item" @click="$router.push(`/product/${p.id}`)">
            <div class="product-image">
              <el-icon :size="56" color="#c0c4cc"><Picture /></el-icon>
            </div>
            <div class="product-info">
              <div class="product-name" :title="p.name">{{ p.name }}</div>
              <div class="product-meta">
                <span class="price">¥{{ p.price }}</span>
                <span class="original-price" v-if="p.originalPrice">¥{{ p.originalPrice }}</span>
              </div>
              <div class="product-sales">已售 {{ p.sales || 0 }} 件</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && !products.length" description="暂无商品" />

      <div class="pagination-area" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[8, 16, 24, 32]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { productApi, categoryApi } from '../../api/product'
import { Search } from '@element-plus/icons-vue'

const route = useRoute()
const loading = ref(false)
const products = ref([])
const categories = ref([])
const keyword = ref(route.query.keyword || '')
const categoryId = ref(null)
const page = ref(1)
const size = ref(8)
const total = ref(0)

async function loadData() {
  loading.value = true
  try {
    const res = await productApi.list({
      keyword: keyword.value || undefined,
      categoryId: categoryId.value || undefined,
      page: page.value,
      size: size.value
    })
    // 后端返回的是分页对象：{ records: [], total: xxx, page: x, size: x, pages: x }
    const pageData = res.data || {}
    products.value = pageData.records || []
    total.value = pageData.total || 0
  } finally {
    loading.value = false
  }
}

async function loadCategories() {
  try {
    const res = await categoryApi.tree()
    const flat = []
    function flatten(list) {
      list?.forEach(item => {
        flat.push(item)
        if (item.children?.length) flatten(item.children)
      })
    }
    flatten(res.data || [])
    categories.value = flat
  } catch {}
}

onMounted(() => {
  loadData()
  loadCategories()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.filter-area {
  display: flex;
  align-items: center;
}

.product-item {
  cursor: pointer;
  margin-bottom: 20px;
  transition: all 0.3s;
}

.product-item:hover {
  transform: translateY(-4px);
}

.product-image {
  height: 160px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.product-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 6px;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 700;
}

.original-price {
  color: #c0c4cc;
  font-size: 12px;
  text-decoration: line-through;
  margin-left: 6px;
}

.product-sales {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.pagination-area {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>