import { Card, Timeline, Badge } from 'antd'
import styles from './ActivityList.module.css'

/**
 * 活动项接口
 */
interface ActivityItem {
  /** 活动标题 */
  title: string
  /** 活动时间 */
  time: string
  /** 活动类型: success | processing | error | warning */
  type?: 'success' | 'processing' | 'error' | 'warning'
  /** 活动描述 */
  description?: string
}

/**
 * 活动列表组件属性
 */
interface ActivityListProps {
  /** 活动项列表 */
  activities: ActivityItem[]
}

/**
 * 活动列表组件
 * 展示最近系统活动和操作记录
 */
function ActivityList({ activities }: ActivityListProps) {
  // 类型对应的颜色映射
  const colorMap = {
    success: '#52c41a',
    processing: '#1890ff',
    error: '#ff4d4f',
    warning: '#faad14'
  }

  return (
    <Card 
      title={<span className={styles.cardTitle}>最近动态</span>}
      variant="borderless"
      className={styles.card}
    >
      <Timeline
        items={activities.map((activity, index) => ({
          key: index,
          color: activity.type ? colorMap[activity.type] : colorMap.processing,
          children: (
            <div className={styles.activityItem}>
              <div className={styles.activityHeader}>
                <span className={styles.activityTitle}>{activity.title}</span>
                <span className={styles.activityTime}>{activity.time}</span>
              </div>
              {activity.description && (
                <div className={styles.activityDesc}>{activity.description}</div>
              )}
            </div>
          )
        }))}
      />
    </Card>
  )
}

export default ActivityList
