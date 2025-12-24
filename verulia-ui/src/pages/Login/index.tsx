import { useState, useEffect } from 'react'
import { Form, Input, Button, message, Checkbox } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { authApi } from '@/api/auth'
import { useAuthStore } from '@/store/auth'
import styles from './Login.module.css'

interface LoginForm {
  username: string
  password: string
  remember?: boolean
}

function Login() {
  const [loading, setLoading] = useState(false)
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 })
  const navigate = useNavigate()
  const setUserInfo = useAuthStore((state) => state.setUserInfo)

  // 鼠标跟随效果
  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      setMousePosition({ x: e.clientX, y: e.clientY })
    }
    window.addEventListener('mousemove', handleMouseMove)
    return () => window.removeEventListener('mousemove', handleMouseMove)
  }, [])

  const onFinish = async (values: LoginForm) => {
    setLoading(true)
    try {
      // 登录请求，返回token
      const token = await authApi.login({
        grantType: 'password',
        username: values.username,
        password: values.password,
      })
      
      // 存储token
      localStorage.setItem('token', token)
      
      // 获取用户信息
      const userInfo = await authApi.getUserInfo()
      setUserInfo(userInfo)
      
      message.success('登录成功！')
      navigate('/home')
    } catch (error: any) {
      message.error(error.message || '登录失败，请检查用户名和密码')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.loginContainer}>
      {/* 鼠标跟随光晕 */}
      <div 
        className={styles.mouseGlow}
        style={{
          left: mousePosition.x,
          top: mousePosition.y,
        }}
      />
      
      {/* 动态背景粒子 */}
      <div className={styles.particles}>
        <div className={styles.particle}></div>
        <div className={styles.particle}></div>
        <div className={styles.particle}></div>
        <div className={styles.particle}></div>
        <div className={styles.particle}></div>
      </div>
      
      <div className={styles.loginBox}>
        <div className={styles.loginCard}>
          <div className={styles.loginHeader}>
            <div className={styles.logo}>
              <img src="/logo.png" alt="Verulia" className={styles.logoImage} />
            </div>
            <h1 className={styles.title}>欢迎回来</h1>
            <p className={styles.subtitle}>登录 Verulia 管理系统</p>
          </div>
          
          <Form
            name="login"
            onFinish={onFinish}
            autoComplete="off"
            size="large"
          >
            <Form.Item
              name="username"
              rules={[{ required: true, message: '请输入用户名' }]}
              className={styles.formItem}
            >
              <Input 
                prefix={<UserOutlined style={{ color: '#bfbfbf' }} />} 
                placeholder="请输入用户名"
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[{ required: true, message: '请输入密码' }]}
              className={styles.formItem}
            >
              <Input.Password
                prefix={<LockOutlined style={{ color: '#bfbfbf' }} />}
                placeholder="请输入密码"
              />
            </Form.Item>

            <Form.Item>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Form.Item name="remember" valuePropName="checked" noStyle>
                  <Checkbox>记住我</Checkbox>
                </Form.Item>
                {/* <a href="#" style={{ color: '#1890ff' }}>忘记密码？</a> */}
              </div>
            </Form.Item>

            <Form.Item>
              <Button 
                type="primary" 
                htmlType="submit" 
                loading={loading} 
                className={styles.loginButton}
              >
                登录
              </Button>
            </Form.Item>
          </Form>
          
          {/* <div className={styles.footer}>
            还没有账号？
            <a href="#" className={styles.footerLink}>立即注册</a>
          </div> */}
        </div>
      </div>
      
      {/* 版权信息 */}
      <div className={styles.copyright}>
        <p>© {new Date().getFullYear()} Verulia. All rights reserved.</p>
      </div>
    </div>
  )
}

export default Login
