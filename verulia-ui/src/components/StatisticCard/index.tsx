import { Card, Statistic } from 'antd'
import { ReactNode } from 'react'
import styles from './StatisticCard.module.css'

/**
 * 统计卡片组件属性接口
 */
interface StatisticCardProps {
  /** 卡片标题 */
  title: string
  /** 数值 */
  value: number | string
  /** 前缀图标 */
  icon?: ReactNode
  /** 卡片颜色主题 */
  color?: string
  /** 数值后缀 */
  suffix?: string
  /** 增长趋势百分比 */
  trend?: number
}

/**
 * 统计卡片组件
 * 用于展示关键数据指标，支持自定义图标、颜色和趋势
 */
function StatisticCard({ 
  title, 
  value, 
  icon, 
  color = '#1890ff',
  suffix,
  trend
}: StatisticCardProps) {
  return (
    <Card className={styles.card} variant="borderless">
      <div className={styles.cardContent}>
        <div className={styles.iconWrapper} style={{ background: `${color}15` }}>
          <div style={{ color, fontSize: 24 }}>
            {icon}
          </div>
        </div>
        <div className={styles.statistic}>
          <Statistic 
            title={<span className={styles.title}>{title}</span>}
            value={value}
            suffix={suffix}
            valueStyle={{ fontSize: 28, fontWeight: 600, color: '#333' }}
          />
          {trend !== undefined && (
            <div className={trend >= 0 ? styles.trendUp : styles.trendDown}>
              {trend >= 0 ? '↑' : '↓'} {Math.abs(trend)}%
              <span className={styles.trendLabel}>较昨日</span>
            </div>
          )}
        </div>
      </div>
    </Card>
  )
}

export default StatisticCard
