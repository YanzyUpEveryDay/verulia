/**
 * 本地存储工具类
 * 统一管理 localStorage 的 key，避免在不同地方使用时出现 key 写错的问题
 */

// 存储 key 常量
const STORAGE_KEYS = {
  TOKEN: 'token'
} as const

/**
 * Token 存储管理
 */
export const tokenStorage = {
  /**
   * 获取 token
   */
  getToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.TOKEN)
  },

  /**
   * 设置 token
   */
  setToken(token: string): void {
    localStorage.setItem(STORAGE_KEYS.TOKEN, token)
  },

  /**
   * 移除 token
   */
  removeToken(): void {
    localStorage.removeItem(STORAGE_KEYS.TOKEN)
  },

  /**
   * 检查 token 是否存在
   */
  hasToken(): boolean {
    return !!this.getToken()
  },
}

/**
 * 导出 storage keys 供需要时使用
 */
export { STORAGE_KEYS }
