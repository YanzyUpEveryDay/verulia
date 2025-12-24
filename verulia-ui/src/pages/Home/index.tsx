import { Row, Col } from 'antd'
import { 
  UserOutlined, 
  TeamOutlined, 
  EyeOutlined,
  FileTextOutlined,
  PlusOutlined,
  SettingOutlined,
  SafetyOutlined,
  BarChartOutlined
} from '@ant-design/icons'
import StatisticCard from '@/components/StatisticCard'
import QuickAction from '@/components/QuickAction'
import TrendChart from '@/components/TrendChart'
import ActivityList from '@/components/ActivityList'
import styles from './Home.module.css'

/**
 * 管理端首页组件
 * 展示系统概览、数据统计、快捷操作和最近动态
 */
function Home() {
  // 统计数据（模拟数据）
  const statisticData = [
    {
      title: '用户总数',
      value: 1289,
      icon: <UserOutlined />,
      color: '#1890ff',
      trend: 12.5
    },
    {
      title: '角色总数',
      value: 8,
      icon: <TeamOutlined />,
      color: '#52c41a',
      trend: 0
    },
    {
      title: '今日访问',
      value: 2341,
      icon: <EyeOutlined />,
      color: '#722ed1',
      trend: 8.3
    },
    {
      title: '系统消息',
      value: 15,
      icon: <FileTextOutlined />,
      color: '#fa8c16',
      trend: -3.2
    }
  ]

  // 快捷操作数据
  const quickActions = [
    {
      title: '新增用户',
      icon: <PlusOutlined />,
      color: '#1890ff',
      onClick: () => console.log('新增用户')
    },
    {
      title: '角色管理',
      icon: <TeamOutlined />,
      color: '#52c41a',
      onClick: () => console.log('角色管理')
    },
    {
      title: '系统设置',
      icon: <SettingOutlined />,
      color: '#722ed1',
      onClick: () => console.log('系统设置')
    },
    {
      title: '安全中心',
      icon: <SafetyOutlined />,
      color: '#fa8c16',
      onClick: () => console.log('安全中心')
    },
    {
      title: '数据分析',
      icon: <BarChartOutlined />,
      color: '#eb2f96',
      onClick: () => console.log('数据分析')
    },
    {
      title: '日志查看',
      icon: <FileTextOutlined />,
      color: '#13c2c2',
      onClick: () => console.log('日志查看')
    },
    {
      title: '权限配置',
      icon: <SafetyOutlined />,
      color: '#faad14',
      onClick: () => console.log('权限配置')
    },
    {
      title: '系统监控',
      icon: <EyeOutlined />,
      color: '#2f54eb',
      onClick: () => console.log('系统监控')
    }
  ]

  // 用户增长趋势数据（模拟最近7天）
  const userTrendData = {
    labels: ['12-18', '12-19', '12-20', '12-21', '12-22', '12-23', '12-24'],
    data: [1156, 1178, 1203, 1225, 1241, 1268, 1289]
  }

  // 访问量趋势数据（模拟最近7天）
  const visitTrendData = {
    labels: ['12-18', '12-19', '12-20', '12-21', '12-22', '12-23', '12-24'],
    data: [2156, 2389, 2245, 2567, 2423, 2198, 2341]
  }

  // 最近动态数据
  const activities = [
    {
      title: '管理员登录系统',
      time: '5分钟前',
      type: 'success' as const,
      description: 'IP: 192.168.1.100'
    },
    {
      title: '新用户注册',
      time: '12分钟前',
      type: 'processing' as const,
      description: '用户名: zhangsan，等待审核'
    },
    {
      title: '角色权限更新',
      time: '30分钟前',
      type: 'warning' as const,
      description: '更新了"编辑"角色的权限配置'
    },
    {
      title: '系统备份完成',
      time: '1小时前',
      type: 'success' as const,
      description: '数据库备份已成功保存'
    },
    {
      title: '登录失败告警',
      time: '2小时前',
      type: 'error' as const,
      description: 'IP: 10.0.0.88 连续登录失败5次'
    },
    {
      title: '用户信息修改',
      time: '3小时前',
      type: 'processing' as const,
      description: '用户"lisi"更新了个人资料'
    }
  ]

  return (
    <div className={styles.container}>
      {/* 欢迎区域 */}
      <div className={styles.welcome}>
        <h1 className={styles.welcomeTitle}>欢迎回来，管理员</h1>
        <p className={styles.welcomeDesc}>今天是 2025年12月24日，祝您工作愉快！</p>
      </div>

      {/* 数据统计卡片 */}
      <Row gutter={[24, 24]} className={styles.statisticRow}>
        {statisticData.map((item, index) => (
          <Col xs={24} sm={12} lg={6} key={index}>
            <StatisticCard
              title={item.title}
              value={item.value}
              icon={item.icon}
              color={item.color}
              trend={item.trend}
            />
          </Col>
        ))}
      </Row>

      {/* 快捷操作 */}
      <Row gutter={[24, 24]} className={styles.actionCol}>
        <Col span={24}>
          <QuickAction actions={quickActions} />
        </Col>
      </Row>

      {/* 趋势图表 */}
      <Row gutter={[24, 24]} className={styles.chartRow}>
        <Col xs={24} lg={12}>
          <TrendChart
            title="用户增长趋势"
            labels={userTrendData.labels}
            data={userTrendData.data}
            color="#1890ff"
          />
        </Col>
        <Col xs={24} lg={12}>
          <TrendChart
            title="访问量趋势"
            labels={visitTrendData.labels}
            data={visitTrendData.data}
            color="#52c41a"
          />
        </Col>
      </Row>

      {/* 最近动态 */}
      <Row gutter={[24, 24]}>
        <Col span={24}>
          <ActivityList activities={activities} />
        </Col>
      </Row>
    </div>
  )
}

export default Home
