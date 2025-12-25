import { Routes, Route, Navigate } from 'react-router-dom'
import Login from '@/pages/Login'
import Layout from '@/layouts/MainLayout'
import AuthGuard from './AuthGuard'
import { routes, flattenRoutes } from './routes'

function AppRouter() {
  // 扁平化路由配置
  const flatRoutes = flattenRoutes(routes)

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<AuthGuard><Layout /></AuthGuard>}>
        <Route index element={<Navigate to="/home" replace />} />
        {flatRoutes.map((route) => (
          <Route key={route.path} path={route.path.substring(1)} element={route.element} />
        ))}
      </Route>
    </Routes>
  )
}

export default AppRouter
