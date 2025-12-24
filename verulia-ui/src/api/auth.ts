import { httpClient } from '@/utils/request'

interface LoginParams {
  grantType: string
  username: string
  password: string
}

interface UserInfo {
  userId: number
  username: string
  nickname: string
  avatar?: string
  phone?: string
  sex?: string
}

interface LoginResponse {
  user: UserInfo
  roles: string[]
  permissions: string[]
}

export const authApi = {
  // 用户登录 - 返回token字符串
  login(data: LoginParams) {
    return httpClient.post<string>('/login', data)
  },

  // 获取用户信息
  getUserInfo() {
    return httpClient.get<LoginResponse>('/getRemoteInfo')
  },

  // 退出登录
  logout() {
    return httpClient.post<void>('/logout')
  },
}
