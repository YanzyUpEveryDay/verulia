import { httpClient } from '@/utils/request'
import type { Role, RoleQuery, RoleCreate, RoleUpdate } from '@/types/role'

interface PageResult<T> {
  list: T[]
  total: number
}

export const roleApi = {
  // 获取角色列表
  getRoleList(params: RoleQuery) {
    return httpClient.get<PageResult<Role>>('/system/role/page', { params })
  },

  // 获取角色详情
  getRoleById(id: number) {
    return httpClient.get<Role>(`/system/role/${id}`)
  },

  // 新增角色
  addRole(data: RoleCreate) {
    return httpClient.post<void>('/system/role', data)
  },

  // 更新角色
  updateRole(data: RoleUpdate) {
    return httpClient.put<void>('/system/role', data)
  },

  // 删除角色
  deleteRole(id: number) {
    return httpClient.delete<void>(`/system/role/${id}`)
  },
}
