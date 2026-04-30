import { defineStore } from 'pinia'
import { login, getUserInfo, logout } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: {},
    roles: [],
    permissions: []
  }),

  actions: {
    async login(loginForm) {
      const res = await login(loginForm)
      if (res.code === 200) {
        this.token = res.data.token
        setToken(res.data.token)
      }
      return res
    },

    async getUserInfo() {
      const res = await getUserInfo()
      if (res.code === 200) {
        this.userInfo = res.data.user
        this.roles = res.data.roles || []
        this.permissions = res.data.permissions || []
      }
      return res
    },

    async logout() {
      await logout()
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
      removeToken()
    },

    resetToken() {
      this.token = ''
      removeToken()
    }
  }
})