// 会员信息
export interface Member {
  id: number
  memberNo: string // 会员编号
  nickname: string // 昵称
  avatar?: string // 头像
  phone?: string // 手机号
  email?: string // 邮箱
  sex?: number // 性别 0=未知 1=男 2=女
  birthday?: string // 生日
  level: number // 会员等级
  levelName?: string // 会员等级名称
  points: number // 积分
  balance: number // 余额
  status: number // 状态 0=禁用 1=正常
  registerTime?: string // 注册时间
  lastLoginTime?: string // 最后登录时间
}

// 会员列表查询参数
export interface MemberQuery {
  current?: number
  size?: number
  memberNo?: string
  nickname?: string
  phone?: string
  level?: number
  status?: number
}

// 会员表单
export interface MemberForm {
  id?: number
  nickname: string
  phone?: string
  email?: string
  sex?: number
  birthday?: string
  level: number
  status: number
}

// 分页结果
export interface MemberPageResult {
  records: Member[]
  total: number
  size: number
  current: number
  pages: number
}
