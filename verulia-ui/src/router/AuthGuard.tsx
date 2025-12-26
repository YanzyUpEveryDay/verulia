import { useEffect } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { Spin } from 'antd'
import { tokenStorage } from '@/utils/storage'
import { useAuthStore } from '@/store/auth'
import { authApi } from '@/api/auth'

interface AuthGuardProps {
  children: React.ReactNode
}

/**
 * 路由守卫组件
 * 验证用户是否已登录，未登录则跳转到登录页
 * 已登录则从后端获取用户信息进行鉴权
 */
function AuthGuard({ children }: AuthGuardProps) {
  const location = useLocation()
  const { userInfo, loading, setUserInfo, setLoading, clearUserInfo } = useAuthStore()
  const hasToken = tokenStorage.hasToken()

  useEffect(() => {
    // 如果有 token 但没有用户信息，则从后端获取
    if (hasToken && !userInfo && !loading) {
      setLoading(true)
      authApi
        .getUserInfo()
        .then((data) => {
          setUserInfo(data)
        })
        .catch((error) => {
          console.error('获取用户信息失败:', error)
          // 获取失败，清除 token 和用户信息
          tokenStorage.removeToken()
          clearUserInfo()
        })
        .finally(() => {
          setLoading(false)
        })
    }
  }, [hasToken, userInfo, loading, setUserInfo, setLoading, clearUserInfo])

  // 没有 token，跳转到登录页
  if (!hasToken) {
    // 保存当前路径作为登录后的重定向地址
    // 如果是首页或根路径，不需要重定向参数
    const needRedirect = location.pathname !== '/' && location.pathname !== '/home'
    const redirectUrl = needRedirect ? `/login?redirect=${encodeURIComponent(location.pathname)}` : '/login'
    
    return <Navigate to={redirectUrl} replace />
  }

  // 正在加载用户信息
  if (loading || !userInfo) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Spin size="large" tip="加载中...">
          <div style={{ padding: '50px' }} />
        </Spin>
      </div>
    )
  }

  return <>{children}</>
}

export default AuthGuard
