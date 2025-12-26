import { httpClient } from '@/utils/request'

export interface AdvTask {
  id: number
  title: string
  content: string
  category: string
  difficulty: number
  status: string
  remark?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
}

export interface AdvTaskQuery {
  pageNum: number
  pageSize: number
  title?: string
  category?: string
  difficulty?: number
  status?: string
}

export interface AdvTaskForm {
  id?: number
  title: string
  content: string
  category: string
  difficulty: number
  status: string
  remark?: string
}

export const advTaskApi = {
  // 分页查询
  getPageList: (params: AdvTaskQuery) => {
    return httpClient.get('/adventure/task/page', { params })
  },

  // 根据ID查询
  getById: (id: number) => {
    return httpClient.get(`/adventure/task/${id}`)
  },

  // 新增
  create: (data: AdvTaskForm) => {
    return httpClient.post('/adventure/task', data)
  },

  // 修改
  update: (data: AdvTaskForm) => {
    return httpClient.put('/adventure/task', data)
  },

  // 删除
  delete: (id: number) => {
    return httpClient.delete(`/adventure/task/${id}`)
  },
}
