<template>
  <div class="category-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="page-header">
          <span>分类管理</span>
          <el-button type="primary" @click="handleAdd(null)">
            <el-icon><Plus /></el-icon>
            新增顶级分类
          </el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        row-key="id"
        :tree-props="{ children: 'children' }"
        default-expand-all
        border
      >
        <el-table-column prop="name" label="分类名称" min-width="200" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleAdd(row)">添加子分类</el-button>
            <el-button type="warning" link @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定要删除该分类吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && !tableData.length" description="暂无分类数据" />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级分类">
          <el-input v-model="parentName" disabled />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" :max="9999" />
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
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { categoryApi } from '../../api/product'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentParent = ref(null)
const parentName = computed(() => {
  if (!currentParent.value) return '无（顶级分类）'
  return currentParent.value.name
})

const formData = ref({
  name: '',
  parentId: 0,
  sort: 0
})

const formRules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ]
}

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑分类'
  if (currentParent.value) return `添加子分类：${currentParent.value.name}`
  return '新增顶级分类'
})

async function loadData() {
  loading.value = true
  try {
    const res = await categoryApi.tree()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  } finally {
    loading.value = false
  }
}

function handleAdd(parent) {
  isEdit.value = false
  currentParent.value = parent
  formData.value = {
    name: '',
    parentId: parent ? parent.id : 0,
    sort: 0
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  currentParent.value = null
  formData.value = {
    id: row.id,
    name: row.name,
    parentId: row.parentId || 0,
    sort: row.sort || 0
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await categoryApi.update(formData.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await categoryApi.create(formData.value)
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

async function handleDelete(id) {
  try {
    await categoryApi.delete(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}
</style>