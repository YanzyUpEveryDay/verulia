import { httpClient } from '@/utils/request'
import type { User, UserQuery, UserCreate, UserUpdate } from '@/types/user'

interface PageResult<T> {
  rows: T[]
  total: number
}

export const userApi = {
  // 获取用户列表
  getUserList(params: UserQuery) {
    return httpClient.get<PageResult<User>>('/system/user/page', { params })
  },

  // 获取用户详情
  getUserById(id: number) {
    return httpClient.get<User>(`/system/user/${id}`)
  },

  // 新增用户
  addUser(data: UserCreate) {
    return httpClient.post<void>('/system/user', data)
  },

  // 更新用户
  updateUser(data: UserUpdate) {
    return httpClient.put<void>('/system/user', data)
  },

  // 删除用户
  deleteUser(id: number) {
    return httpClient.delete<void>(`/system/user/${id}`)
  },
}
