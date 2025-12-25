import { Routes, Route, Navigate } from 'react-router-dom'
import Login from '@/pages/Login'
import Layout from '@/layouts/MainLayout'
import Home from '@/pages/Home'
import UserList from '@/pages/system/UserList'
import RoleList from '@/pages/system/RoleList'
import MemberList from '@/pages/member/MemberList'
import AuthGuard from './AuthGuard'

function AppRouter() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<AuthGuard><Layout /></AuthGuard>}>
        <Route index element={<Navigate to="/home" replace />} />
        <Route path="home" element={<Home />} />
        <Route path="system/user" element={<UserList />} />
        <Route path="system/role" element={<RoleList />} />
        <Route path="member/list" element={<MemberList />} />
      </Route>
    </Routes>
  )
}

export default AppRouter
