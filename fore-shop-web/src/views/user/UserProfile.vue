<template>
  <div class="profile-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" class="profile-card">
          <div class="avatar-area">
            <el-avatar :size="80" :icon="UserFilled" />
            <h3>{{ userStore.nickname }}</h3>
            <p class="user-id">ID: {{ userStore.userInfo?.id || '-' }}</p>
          </div>
          <el-divider />
          <div class="profile-menu">
            <div class="menu-item" :class="{ active: activeTab === 'info' }" @click="activeTab = 'info'">
              <el-icon><User /></el-icon> 基本信息
            </div>
            <div class="menu-item" :class="{ active: activeTab === 'password' }" @click="activeTab = 'password'">
              <el-icon><Lock /></el-icon> 修改密码
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="never" v-if="activeTab === 'info'">
          <template #header><span>基本信息</span></template>
          <el-form :model="profileForm" label-width="80px" style="max-width: 500px">
            <el-form-item label="用户名">
              <el-input :value="userStore.userInfo?.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" v-if="activeTab === 'password'">
          <template #header><span>修改密码</span></template>
          <el-form :model="passwordForm" label-width="80px" style="max-width: 500px">
            <el-form-item label="旧密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword">确认修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { userApi } from '../../api/user'
import { UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('info')

const profileForm = reactive({ nickname: '', phone: '', email: '' })
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

async function handleUpdateProfile() {
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('用户信息加载失败，请重新登录')
      return
    }
    await userApi.updateUser(userId, profileForm)
    ElMessage.success('修改成功')
    await userStore.fetchUserInfo()
  } catch (error) {
    console.error('更新用户信息失败:', error)
  }
}

async function handleChangePassword() {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次密码不一致')
    return
  }
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('用户信息加载失败，请重新登录')
      return
    }
    await userApi.changePassword(userId, {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    console.error('修改密码失败:', error)
  }
}

onMounted(async () => {
  await userStore.fetchUserInfo()
  if (userStore.userInfo) {
    profileForm.nickname = userStore.userInfo.nickname || ''
    profileForm.phone = userStore.userInfo.phone || ''
    profileForm.email = userStore.userInfo.email || ''
  }
})
</script>

<style scoped>
.avatar-area {
  text-align: center;
  padding: 20px 0;
}

.avatar-area h3 {
  margin-top: 12px;
  color: #333;
}

.user-id {
  color: #999;
  font-size: 13px;
  margin-top: 4px;
}

.menu-item {
  padding: 10px 16px;
  cursor: pointer;
  border-radius: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  color: #666;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item.active {
  background: #ecf5ff;
  color: #409eff;
}
</style>