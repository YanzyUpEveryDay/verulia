import { create } from 'zustand'
import { persist } from 'zustand/middleware'

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
  setUserInfo: (userInfo: UserInfo) => void
  clearUserInfo: () => void
}

export const useAuthStore = create<AuthStore>()(
  persist(
    (set) => ({
      userInfo: null,
      setUserInfo: (userInfo) => set({ userInfo }),
      clearUserInfo: () => set({ userInfo: null }),
    }),
    {
      name: 'auth-storage',
    }
  )
)
