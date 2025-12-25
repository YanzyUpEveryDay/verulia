import { Card } from 'antd'
import { XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Area, AreaChart } from 'recharts'
import styles from './TrendChart.module.css'

/**
 * 趋势图表组件属性
 */
interface TrendChartProps {
  /** 图表标题 */
  title: string
  /** 数据标签列表 */
  labels: string[]
  /** 数据值列表 */
  data: number[]
  /** 线条颜色 */
  color?: string
}

/**
 * 趋势图表组件
 * 使用 Recharts 绘制折线图，便于后期对接数据
 */
function TrendChart({ 
  title, 
  labels, 
  data, 
  color = '#1890ff' 
}: TrendChartProps) {
  // 转换数据格式为 Recharts 所需的格式
  const chartData = labels.map((label, index) => ({
    date: label,
    value: data[index]
  }))

  return (
    <Card 
      title={<span className={styles.cardTitle}>{title}</span>}
      variant="borderless"
      className={styles.card}
    >
      <ResponsiveContainer width="100%" height={300}>
        <AreaChart data={chartData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
          <defs>
            <linearGradient id={`gradient-${title}`} x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor={color} stopOpacity={0.2}/>
              <stop offset="95%" stopColor={color} stopOpacity={0}/>
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
          <XAxis 
            dataKey="date" 
            tick={{ fontSize: 12, fill: '#999' }}
            tickLine={false}
          />
          <YAxis 
            tick={{ fontSize: 12, fill: '#999' }}
            tickLine={false}
            axisLine={false}
          />
          <Tooltip 
            contentStyle={{ 
              backgroundColor: '#fff', 
              border: '1px solid #e8e8e8',
              borderRadius: '4px',
              fontSize: '12px'
            }}
          />
          <Area 
            type="monotone" 
            dataKey="value" 
            stroke={color} 
            strokeWidth={3}
            fill={`url(#gradient-${title})`}
            dot={{ fill: color, strokeWidth: 2, r: 4, stroke: '#fff' }}
            activeDot={{ r: 6 }}
          />
        </AreaChart>
      </ResponsiveContainer>
    </Card>
  )
}

export default TrendChart
