// 会员信息
export interface Member {
  id: number
  nickname: string // 昵称
  avatar?: string // 头像
  mobile?: string // 手机号
  openid?: string // 微信OpenID
  unionid?: string // 微信UnionID
  balance: number // 余额
  score: number // 积分
  level?: number // 会员等级
  status: string // 状态 0=正常 1=封禁
  registerSource?: string // 注册来源
  createTime?: string // 创建时间
}

// 会员列表查询参数
export interface MemberQuery {
  pageNum?: number
  pageSize?: number
  nickname?: string
  mobile?: string
  level?: number
  status?: string
}

// 会员表单
export interface MemberForm {
  id?: number
  nickname: string
  mobile?: string
  password?: string
  avatar?: string
  level?: number
  status: string
  registerSource?: string
}

// 分页结果
export interface MemberPageResult {
  records: Member[]
  total: number
  size: number
  current: number
  pages: number
}
