<template>
  <div class="product-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>商品列表</span>
          <div class="header-actions">
            <div class="filter-area">
              <el-input v-model="keyword" placeholder="搜索商品" :prefix-icon="Search" clearable style="width: 200px" @clear="loadData" @keyup.enter="loadData" />
              <el-select v-model="categoryId" placeholder="全部分类" clearable style="width: 150px; margin-left: 12px" @change="loadData">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </div>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增商品
            </el-button>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="products" border stripe>
        <el-table-column label="商品信息" min-width="250">
          <template #default="{ row }">
            <div class="product-cell">
              <div class="product-thumb">
                <img v-if="row.image" :src="row.image" alt="" />
                <el-icon v-else :size="32" color="#c0c4cc"><Picture /></el-icon>
              </div>
              <div class="product-text">
                <div class="name">{{ row.name }}</div>
                <div class="desc">{{ row.description || '暂无描述' }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column label="价格" width="150">
          <template #default="{ row }">
            <span class="price">¥{{ row.price }}</span>
            <span class="original" v-if="row.originalPrice">¥{{ row.originalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" align="center" />
        <el-table-column prop="sales" label="已售" width="80" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="success" link @click="handleAddToCart(row)" v-if="row.status === 1">
              加入购物车
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-popconfirm title="确定要删除该商品吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑商品' : '新增商品'"
      width="700px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入商品名称" />
        </el-form-item>

        <el-form-item label="商品分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="c in flatCategories"
              :key="c.id"
              :label="c.name"
              :value="c.id"
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="售价" prop="price">
              <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原价">
              <el-input-number v-model="formData.originalPrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存" prop="stock">
              <el-input-number v-model="formData.stock" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="formData.status" style="width: 100%">
                <el-option :value="1" label="上架" />
                <el-option :value="0" label="下架" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="商品图片">
          <el-input v-model="formData.image" placeholder="请输入图片URL（可选）" />
        </el-form-item>

        <el-form-item label="商品描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商品描述（可选）"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi, categoryApi } from '../../api/product'
import { cartApi } from '../../api/cart'
import { Search, Plus, Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import ImageUpload from '../../components/ImageUpload.vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitLoading = ref(false)
const products = ref([])
const categories = ref([])
const keyword = ref(route.query.keyword || '')
const categoryId = ref(null)
const page = ref(1)
const size = ref(8)
const total = ref(0)

const dialogVisible = ref(false)
const formRef = ref()
const isEdit = ref(false)

const formData = ref({
  id: null,
  name: '',
  categoryId: null,
  price: 0,
  originalPrice: null,
  stock: 0,
  status: 1,
  image: '',
  description: ''
})

const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入售价', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' }
  ]
}

const flatCategories = computed(() => {
  return categories.value
})

async function loadData() {
  loading.value = true
  try {
    const res = await productApi.list({
      keyword: keyword.value || undefined,
      categoryId: categoryId.value || undefined,
      page: page.value,
      size: size.value
    })
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

function handleAdd() {
  isEdit.value = false
  formData.value = {
    id: null,
    name: '',
    categoryId: null,
    price: 0,
    originalPrice: null,
    stock: 0,
    status: 1,
    image: '',
    description: ''
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  formData.value = {
    id: row.id,
    name: row.name,
    categoryId: row.categoryId,
    price: row.price,
    originalPrice: row.originalPrice,
    stock: row.stock,
    status: row.status,
    image: row.image || '',
    description: row.description || ''
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await productApi.update(formData.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await productApi.create(formData.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

async function handleToggleStatus(row) {
  try {
    await productApi.toggleStatus(row.id)
    const newStatus = row.status === 1 ? '下架' : '上架'
    ElMessage.success(`${newStatus}成功`)
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  }
}

async function handleDelete(id) {
  try {
    await productApi.deleteProduct(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error(error.response?.data?.message || '删除失败')
  }
}

async function handleAddToCart(product) {
  try {
    await cartApi.add(product.id, 1)
    ElMessage.success(`"${product.name}" 已加入购物车`)
  } catch (error) {
    console.error('加入购物车失败:', error)
    ElMessage.error(error.response?.data?.message || '加入购物车失败')
  }
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

.header-actions {
  display: flex;
  gap: 16px;
  align-items: center;
}

.filter-area {
  display: flex;
  align-items: center;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-thumb {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.product-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-text {
  flex: 1;
  min-width: 0;
}

.name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.desc {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  color: #f56c6c;
  font-size: 15px;
  font-weight: 700;
}

.original {
  color: #c0c4cc;
  font-size: 12px;
  text-decoration: line-through;
  margin-left: 8px;
}

.pagination-area {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>