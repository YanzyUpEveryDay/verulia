import { Card } from 'antd'
import { ReactNode } from 'react'
import styles from './QuickAction.module.css'

/**
 * 快捷操作项接口
 */
interface QuickActionItem {
  /** 操作标题 */
  title: string
  /** 操作图标 */
  icon: ReactNode
  /** 图标颜色 */
  color: string
  /** 点击事件 */
  onClick?: () => void
}

/**
 * 快捷操作组件属性
 */
interface QuickActionProps {
  /** 操作项列表 */
  actions: QuickActionItem[]
}

/**
 * 快捷操作组件
 * 提供常用功能的快速入口
 */
function QuickAction({ actions }: QuickActionProps) {
  return (
    <Card 
      title={<span className={styles.cardTitle}>快捷操作</span>}
      variant="borderless"
      className={styles.card}
    >
      <div className={styles.actionGrid}>
        {actions.map((action, index) => (
          <div 
            key={index} 
            className={styles.actionItem}
            onClick={action.onClick}
          >
            <div 
              className={styles.iconWrapper}
              style={{ background: `${action.color}15` }}
            >
              <div style={{ color: action.color, fontSize: 28 }}>
                {action.icon}
              </div>
            </div>
            <div className={styles.actionTitle}>{action.title}</div>
          </div>
        ))}
      </div>
    </Card>
  )
}

export default QuickAction
