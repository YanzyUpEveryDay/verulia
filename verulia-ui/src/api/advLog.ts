import { httpClient } from '@/utils/request'

export interface AdvAdventureLog {
  logId: number
  userId: number
  taskId: number
  taskTitle?: string
  adventureDate: string
  status: string
  swapCount: number
  checkinImg?: string
  checkinComment?: string
  aiReply?: string
  auditStatus: string
  finishTime?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
}

export interface AdvLogQuery {
  pageNum: number
  pageSize: number
  userId?: number
  taskId?: number
  startDate?: string
  endDate?: string
  status?: string
  auditStatus?: string
}

export interface AdvLogUpdate {
  logId: number
  checkinImg?: string
  checkinComment?: string
  aiReply?: string
  status?: string
  auditStatus?: string
}

export interface AdvLogAudit {
  logId: number
  auditStatus: string
  aiReply?: string
}

export const advLogApi = {
  // 分页查询
  getPageList: (params: AdvLogQuery) => {
    return httpClient.get('/adventure/log/page', { params })
  },

  // 根据ID查询
  getById: (logId: number) => {
    return httpClient.get(`/adventure/log/${logId}`)
  },

  // 修改
  update: (data: AdvLogUpdate) => {
    return httpClient.put('/adventure/log', data)
  },

  // 删除
  delete: (logId: number) => {
    return httpClient.delete(`/adventure/log/${logId}`)
  },

  // 审核
  audit: (data: AdvLogAudit) => {
    return httpClient.post('/adventure/log/audit', data)
  },
}
