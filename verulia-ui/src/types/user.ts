export interface User {
  id: number
  username: string
  nickname: string
  phone?: string
  email?: string
  avatar?: string
  sex?: number
  status?: number
  createTime?: string
  updateTime?: string
  roleIds?: number[]
}

export interface UserQuery {
  pageNum: number
  pageSize: number
  username?: string
  phone?: string
  status?: number
}

export interface UserCreate {
  username: string
  password: string
  nickname: string
  phone?: string
  email?: string
  sex?: number
  status?: number
  roleIds?: number[]
}

export interface UserUpdate {
  id: number
  nickname?: string
  phone?: string
  email?: string
  sex?: number
  status?: number
  roleIds?: number[]
}
