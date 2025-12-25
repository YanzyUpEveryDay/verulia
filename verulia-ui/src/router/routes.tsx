import {
  HomeOutlined,
  UserOutlined,
  TeamOutlined,
  CrownOutlined,
} from '@ant-design/icons'
import Home from '@/pages/Home'
import UserList from '@/pages/system/UserList'
import RoleList from '@/pages/system/RoleList'
import MemberList from '@/pages/member/MemberList'

import type { MenuProps } from 'antd'

export interface MenuItemType {
  key: string
  icon?: React.ReactNode
  label?: string
  children?: MenuItemType[]
}

type MenuItem = Required<MenuProps>['items'][number]

export interface RouteConfig {
  path: string
  label?: string
  icon?: React.ReactNode
  element?: React.ReactNode
  children?: RouteConfig[]
  hideInMenu?: boolean // 是否在菜单中隐藏
}

/**
 * 路由配置
 * 统一管理路由和菜单，避免重复定义
 */
export const routes: RouteConfig[] = [
  {
    path: '/home',
    label: '首页',
    icon: <HomeOutlined />,
    element: <Home />,
  },
  {
    path: '/member',
    label: '会员管理',
    icon: <CrownOutlined />,
    children: [
      {
        path: '/member/list',
        label: '会员列表',
        icon: <UserOutlined />,
        element: <MemberList />,
      },
    ],
  },
  {
    path: '/system',
    label: '系统管理',
    icon: <TeamOutlined />,
    children: [
      {
        path: '/system/user',
        label: '用户管理',
        icon: <UserOutlined />,
        element: <UserList />,
      },
      {
        path: '/system/role',
        label: '角色管理',
        icon: <TeamOutlined />,
        element: <RoleList />,
      },
    ],
  },
]

/**
 * 将路由配置转换为菜单配置
 */
export const getMenuItems = (routes: RouteConfig[]): MenuItem[] => {
  return routes
    .filter((route) => !route.hideInMenu)
    .map((route) => ({
      key: route.path,
      icon: route.icon,
      label: route.label,
      children: route.children ? getMenuItems(route.children) : undefined,
    })) as MenuItem[]
}

/**
 * 扁平化路由，用于生成 Route 组件
 */
export const flattenRoutes = (routes: RouteConfig[]): RouteConfig[] => {
  const result: RouteConfig[] = []
  
  routes.forEach((route) => {
    if (route.element) {
      result.push(route)
    }
    if (route.children) {
      result.push(...flattenRoutes(route.children))
    }
  })
  
  return result
}
