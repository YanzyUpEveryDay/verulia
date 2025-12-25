import { Navigate, useLocation } from 'react-router-dom'
import { tokenStorage } from '@/utils/storage'

interface AuthGuardProps {
  children: React.ReactNode
}

/**
 * 路由守卫组件
 * 验证用户是否已登录，未登录则跳转到登录页
 */
function AuthGuard({ children }: AuthGuardProps) {
  const location = useLocation()
  const hasToken = tokenStorage.hasToken()
  if (!hasToken) {
    // 未登录，跳转到登录页，并保存当前路径用于登录后跳转回来
    return <Navigate to="/login" state={{ from: location.pathname }} replace />
  }

  return <>{children}</>
}

export default AuthGuard
