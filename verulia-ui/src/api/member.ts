import { httpClient } from '@/utils/request'
import type { Member, MemberQuery, MemberForm, MemberPageResult } from '@/types/member'

/**
 * 会员管理 API
 */
export const memberApi = {
  /**
   * 分页查询会员列表
   */
  getPageList(params: MemberQuery) {
    return httpClient.get<MemberPageResult>('/member/page', { params })
  },

  /**
   * 获取会员详情
   */
  getById(id: number) {
    return httpClient.get<Member>(`/member/${id}`)
  },

  /**
   * 新增会员
   */
  create(data: MemberForm) {
    return httpClient.post<void>('/member', data)
  },

  /**
   * 更新会员
   */
  update(data: MemberForm) {
    return httpClient.put<void>('/member', data)
  },

  /**
   * 删除会员
   */
  delete(id: number) {
    return httpClient.delete<void>(`/member/${id}`)
  },

  /**
   * 批量删除会员
   */
  batchDelete(ids: number[]) {
    return httpClient.delete<void>('/member/batch', { data: ids })
  },

  /**
   * 修改会员状态
   */
  updateStatus(id: number, status: string) {
    return httpClient.put<void>(`/member/${id}/status`, { status })
  },

  /**
   * 充值积分
   */
  rechargePoints(id: number, points: number) {
    return httpClient.post<void>(`/member/${id}/points`, { points })
  },

  /**
   * 充值余额
   */
  rechargeBalance(id: number, amount: number) {
    return httpClient.post<void>(`/member/${id}/balance`, { amount })
  },
}
