import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)
  const nickname = computed(() => userInfo.value?.nickname || userInfo.value?.username || '用户')

  async function login(loginForm) {
    const res = await userApi.login(loginForm)
    token.value = res.data
    localStorage.setItem('token', res.data)
    await fetchUserInfo()
  }

  async function fetchUserInfo() {
    if (!token.value) return
    try {
      const userId = localStorage.getItem('userId')
      const res = await userApi.getUserById(userId)
      userInfo.value = res.data
    } catch {
      userInfo.value = null
    }
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
  }

  return { token, userInfo, isLoggedIn, nickname, login, fetchUserInfo, logout }
})