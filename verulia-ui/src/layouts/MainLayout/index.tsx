import { useState, useEffect } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu, Avatar, Dropdown, Drawer } from 'antd'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  HomeOutlined,
  UserOutlined,
  TeamOutlined,
  LogoutOutlined,
} from '@ant-design/icons'
import { useAuthStore } from '@/store/auth'
import styles from './MainLayout.module.css'

const { Header, Sider, Content } = Layout

function MainLayout() {
  const [collapsed, setCollapsed] = useState(false)
  const [isMobile, setIsMobile] = useState(false)
  const [drawerVisible, setDrawerVisible] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const { userInfo, clearUserInfo } = useAuthStore()

  // 监听屏幕尺寸变化
  useEffect(() => {
    const handleResize = () => {
      const mobile = window.innerWidth < 768
      setIsMobile(mobile)
      // 移动端默认收起侧边栏
      if (!mobile && drawerVisible) {
        setDrawerVisible(false)
      }
    }

    // 初始化检查
    handleResize()

    // 监听窗口大小变化
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [drawerVisible])

  const menuItems = [
    {
      key: '/home',
      icon: <HomeOutlined />,
      label: '首页',
    },
    {
      key: '/system',
      icon: <TeamOutlined />,
      label: '系统管理',
      children: [
        {
          key: '/system/user',
          icon: <UserOutlined />,
          label: '用户管理',
        },
        {
          key: '/system/role',
          icon: <TeamOutlined />,
          label: '角色管理',
        },
      ],
    },
  ]

  // 根据当前路径获取选中的菜单项
  const selectedKeys = [location.pathname]
  
  // 根据当前路径自动计算需要展开的父菜单
  const getOpenKeys = () => {
    const path = location.pathname
    const keys: string[] = []
    // 遍历菜单项，找到包含当前路径的父菜单
    menuItems.forEach(item => {
      if (item.children) {
        const hasActiveChild = item.children.some((child: any) => child.key === path)
        if (hasActiveChild) {
          keys.push(item.key as string)
        }
      }
    })
    return keys
  }
  const openKeys = getOpenKeys()

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
    // 移动端点击菜单后关闭抽屉
    if (isMobile) {
      setDrawerVisible(false)
    }
  }

  // 切换侧边栏显示
  const toggleSidebar = () => {
    if (isMobile) {
      setDrawerVisible(!drawerVisible)
    } else {
      setCollapsed(!collapsed)
    }
  }

  const handleLogout = () => {
    clearUserInfo()
    navigate('/login')
  }

  const userMenuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ]

  // 渲染菜单内容
  const menuContent = (
    <>
      <div className={styles.logo}>
        {collapsed && !isMobile ? 'V' : 'Verulia Admin'}
      </div>
      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={selectedKeys}
        defaultOpenKeys={openKeys}
        items={menuItems}
        onClick={handleMenuClick}
      />
    </>
  )

  return (
    <Layout className={styles.layout}>
      {/* 桌面端：固定侧边栏 */}
      {!isMobile && (
        <Sider 
          trigger={null} 
          collapsible 
          collapsed={collapsed} 
          className={styles.sider}
          width={200}
        >
          {menuContent}
        </Sider>
      )}

      {/* 移动端：抽屉式侧边栏 */}
      {isMobile && (
        <Drawer
          placement="left"
          onClose={() => setDrawerVisible(false)}
          open={drawerVisible}
          className={styles.drawer}
          bodyStyle={{ padding: 0, background: '#001529' }}
          headerStyle={{ display: 'none' }}
          width={200}
        >
          {menuContent}
        </Drawer>
      )}

      <Layout style={{ marginLeft: isMobile ? 0 : collapsed ? 80 : 200, transition: 'margin-left 0.2s' }}>
        <Header className={styles.header}>
          <div className={styles.headerLeft}>
            {isMobile ? (
              <MenuUnfoldOutlined onClick={toggleSidebar} />
            ) : collapsed ? (
              <MenuUnfoldOutlined onClick={toggleSidebar} />
            ) : (
              <MenuFoldOutlined onClick={toggleSidebar} />
            )}
          </div>
          <div className={styles.headerRight}>
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <div className={styles.userInfo}>
                <Avatar icon={<UserOutlined />} />
                <span className={styles.username}>{userInfo?.user?.nickname || '管理员'}</span>
              </div>
            </Dropdown>
          </div>
        </Header>
        <Content className={styles.content}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}

export default MainLayout
