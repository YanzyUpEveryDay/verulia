export interface Role {
  id: number
  roleName: string
  roleKey: string
  roleSort?: number
  status?: number
  createTime?: string
}

export interface RoleQuery {
  pageNum: number
  pageSize: number
  roleName?: string
  roleKey?: string
  status?: number
}

export interface RoleCreate {
  roleName: string
  roleKey: string
  roleSort?: number
  status?: number
}

export interface RoleUpdate {
  id: number
  roleName?: string
  roleKey?: string
  roleSort?: number
  status?: number
}
