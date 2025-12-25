import { create } from 'zustand'

interface UserInfo {
  user: {
    userId: number
    username: string
    nickname: string
    avatar?: string
    phone?: string
    sex?: string
  }
  roles: string[]
  permissions: string[]
}

interface AuthStore {
  userInfo: UserInfo | null
  loading: boolean
  setUserInfo: (userInfo: UserInfo | null) => void
  setLoading: (loading: boolean) => void
  clearUserInfo: () => void
}

// 不使用 persist 持久化用户信息，每次从后端获取最新数据
export const useAuthStore = create<AuthStore>()((set) => ({
  userInfo: null,
  loading: false,
  setUserInfo: (userInfo) => set({ userInfo }),
  setLoading: (loading) => set({ loading }),
  clearUserInfo: () => set({ userInfo: null }),
}))
