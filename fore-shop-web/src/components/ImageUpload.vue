<template>
  <div class="image-upload">
    <el-upload
      v-model:file-list="fileList"
      :action="uploadUrl"
      :headers="uploadHeaders"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      :limit="1"
      accept="image/*"
      list-type="picture-card"
      class="uploader"
    >
      <img v-if="imageUrl" :src="imageUrl" class="uploaded-image" />
      <el-icon v-else class="uploader-icon"><Plus /></el-icon>
    </el-upload>

    <div v-if="imageUrl" class="image-actions">
      <el-button type="danger" link size="small" @click="removeImage">
        <el-icon><Delete /></el-icon>
        删除
      </el-button>
    </div>

    <div class="upload-tip">
      <slot name="tip">
        支持 JPG、PNG、GIF 格式，建议尺寸 800x800px，大小不超过 2MB
      </slot>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { fileApi } from '../api/file'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const fileList = ref([])
const imageUrl = ref(props.modelValue)

const uploadUrl = '/api/file/upload'

const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`,
  'X-User-Id': localStorage.getItem('userId') || ''
}))

watch(() => props.modelValue, (newVal) => {
  imageUrl.value = newVal
})

watch(imageUrl, (newVal) => {
  emit('update:modelValue', newVal)
})

function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }

  return true
}

function handleSuccess(response, file) {
  if (response.code === 200 && response.data?.url) {
    imageUrl.value = response.data.url
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

function handleError(error) {
  console.error('上传失败:', error)
  ElMessage.error('上传失败，请重试')
}

function removeImage() {
  imageUrl.value = ''
  fileList.value = []
}
</script>

<style scoped>
.image-upload {
  display: inline-block;
}

.uploader {
  width: 148px;
  height: 148px;
  border-radius: 6px;
  overflow: hidden;
}

.uploader :deep(.el-upload) {
  width: 100%;
  height: 100%;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  margin-top: 8px;
  text-align: center;
}

.upload-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
  line-height: 1.4;
}
</style>