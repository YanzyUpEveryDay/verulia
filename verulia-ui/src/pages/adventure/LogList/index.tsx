import { useState, useEffect } from 'react'
import {
  Table,
  Button,
  Form,
  Input,
  Select,
  Space,
  Modal,
  message,
  Tag,
  Card,
  DatePicker,
  Image,
} from 'antd'
import {
  EditOutlined,
  DeleteOutlined,
  ReloadOutlined,
  CheckOutlined,
  EyeOutlined,
} from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import type { AdvAdventureLog, AdvLogQuery } from '@/api/advLog'
import { advLogApi } from '@/api/advLog'
import styles from './LogList.module.css'
import dayjs from 'dayjs'

const { TextArea } = Input
const { RangePicker } = DatePicker

const LogList = () => {
  const [form] = Form.useForm()
  const [modalForm] = Form.useForm()
  const [auditForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [dataSource, setDataSource] = useState<AdvAdventureLog[]>([])
  const [total, setTotal] = useState(0)
  const [query, setQuery] = useState<AdvLogQuery>({
    pageNum: 1,
    pageSize: 10,
  })
  const [editModalVisible, setEditModalVisible] = useState(false)
  const [auditModalVisible, setAuditModalVisible] = useState(false)
  const [detailModalVisible, setDetailModalVisible] = useState(false)
  const [currentRecord, setCurrentRecord] = useState<AdvAdventureLog | null>(null)

  // 状态选项
  const statusOptions = [
    { label: '全部', value: undefined },
    { label: '待接受', value: '0' },
    { label: '进行中', value: '1' },
    { label: '已完成', value: '2' },
  ]

  // 审核状态选项
  const auditStatusOptions = [
    { label: '全部', value: undefined },
    { label: '未审核', value: '0' },
    { label: '已审核', value: '1' },
  ]

  // 加载数据
  const loadData = async () => {
    setLoading(true)
    try {
      const data = await advLogApi.getPageList(query)
      setDataSource(data.rows)
      setTotal(data.total)
    } catch (error) {
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [query])

  // 搜索
  const handleSearch = () => {
    const values = form.getFieldsValue()
    const searchParams: any = { ...values }
    
    // 处理日期范围
    if (values.dateRange && values.dateRange.length === 2) {
      searchParams.startDate = values.dateRange[0].format('YYYY-MM-DD')
      searchParams.endDate = values.dateRange[1].format('YYYY-MM-DD')
      delete searchParams.dateRange
    }
    
    setQuery({ ...query, ...searchParams, pageNum: 1 })
  }

  // 重置
  const handleReset = () => {
    form.resetFields()
    setQuery({ pageNum: 1, pageSize: 10 })
  }

  // 分页变化
  const handleTableChange = (page: number, pageSize: number) => {
    setQuery({ ...query, pageNum: page, pageSize: pageSize })
  }

  // 查看详情
  const handleView = async (record: AdvAdventureLog) => {
    setCurrentRecord(record)
    setDetailModalVisible(true)
  }

  // 编辑
  const handleEdit = async (record: AdvAdventureLog) => {
    setCurrentRecord(record)
    setEditModalVisible(true)
    modalForm.setFieldsValue(record)
  }

  // 删除
  const handleDelete = (logId: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该手账记录吗？',
      onOk: async () => {
        try {
          await advLogApi.delete(logId)
          message.success('删除成功')
          loadData()
        } catch (error) {
          message.error('删除失败')
        }
      },
    })
  }

  // 审核
  const handleAudit = (record: AdvAdventureLog) => {
    setCurrentRecord(record)
    setAuditModalVisible(true)
    auditForm.setFieldsValue({
      logId: record.logId,
      auditStatus: '1',
      aiReply: record.aiReply,
    })
  }

  // 提交编辑
  const handleEditModalOk = async () => {
    try {
      const values = await modalForm.validateFields()
      await advLogApi.update(values)
      message.success('保存成功')
      setEditModalVisible(false)
      loadData()
    } catch (error) {
      console.error('表单验证失败:', error)
    }
  }

  // 提交审核
  const handleAuditModalOk = async () => {
    try {
      const values = await auditForm.validateFields()
      await advLogApi.audit(values)
      message.success('审核成功')
      setAuditModalVisible(false)
      loadData()
    } catch (error) {
      console.error('审核失败:', error)
    }
  }

  // 表格列定义
  const columns: ColumnsType<AdvAdventureLog> = [
    {
      title: '手账ID',
      dataIndex: 'logId',
      width: 80,
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      width: 100,
    },
    {
      title: '奇遇任务',
      dataIndex: 'taskTitle',
      width: 200,
    },
    {
      title: '奇遇日期',
      dataIndex: 'adventureDate',
      width: 120,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      render: status => {
        const statusMap: Record<string, { label: string; color: string }> = {
          '0': { label: '待接受', color: 'default' },
          '1': { label: '进行中', color: 'processing' },
          '2': { label: '已完成', color: 'success' },
        }
        const statusInfo = statusMap[status] || { label: '-', color: 'default' }
        return <Tag color={statusInfo.color}>{statusInfo.label}</Tag>
      },
    },
    {
      title: '换一换次数',
      dataIndex: 'swapCount',
      width: 100,
    },
    {
      title: '审核状态',
      dataIndex: 'auditStatus',
      width: 100,
      render: auditStatus => (
        <Tag color={auditStatus === '1' ? 'success' : 'warning'}>
          {auditStatus === '1' ? '已审核' : '未审核'}
        </Tag>
      ),
    },
    {
      title: '完成时间',
      dataIndex: 'finishTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      width: 250,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            size="small"
            icon={<EyeOutlined />}
            onClick={() => handleView(record)}
          >
            详情
          </Button>
          <Button
            type="link"
            size="small"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          {record.auditStatus === '0' && (
            <Button
              type="link"
              size="small"
              icon={<CheckOutlined />}
              onClick={() => handleAudit(record)}
            >
              审核
            </Button>
          )}
          <Button
            type="link"
            size="small"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.logId)}
          >
            删除
          </Button>
        </Space>
      ),
    },
  ]

  return (
    <div className={styles.container}>
      <Card>
        {/* 搜索表单 */}
        <Form form={form} layout="inline" className={styles.searchForm}>
          <Form.Item name="userId" label="用户ID">
            <Input placeholder="请输入用户ID" allowClear style={{ width: 150 }} />
          </Form.Item>
          <Form.Item name="taskId" label="任务ID">
            <Input placeholder="请输入任务ID" allowClear style={{ width: 150 }} />
          </Form.Item>
          <Form.Item name="dateRange" label="日期范围">
            <RangePicker />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select
              placeholder="请选择状态"
              style={{ width: 120 }}
              allowClear
              options={statusOptions}
            />
          </Form.Item>
          <Form.Item name="auditStatus" label="审核状态">
            <Select
              placeholder="请选择"
              style={{ width: 120 }}
              allowClear
              options={auditStatusOptions}
            />
          </Form.Item>
          <Form.Item>
            <Space>
              <Button type="primary" onClick={handleSearch}>
                搜索
              </Button>
              <Button onClick={handleReset}>重置</Button>
            </Space>
          </Form.Item>
        </Form>

        {/* 工具栏 */}
        <div className={styles.toolbar}>
          <div></div>
          <Button icon={<ReloadOutlined />} onClick={loadData}>
            刷新
          </Button>
        </div>

        {/* 表格 */}
        <Table
          rowKey="logId"
          columns={columns}
          dataSource={dataSource}
          loading={loading}
          pagination={{
            current: query.pageNum,
            pageSize: query.pageSize,
            total,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: total => `共 ${total} 条`,
            onChange: handleTableChange,
          }}
          scroll={{ x: 1600 }}
        />
      </Card>

      {/* 详情弹窗 */}
      <Modal
        title="手账详情"
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setDetailModalVisible(false)}>
            关闭
          </Button>,
        ]}
        width={700}
      >
        {currentRecord && (
          <div>
            <p><strong>手账ID：</strong>{currentRecord.logId}</p>
            <p><strong>用户ID：</strong>{currentRecord.userId}</p>
            <p><strong>奇遇任务：</strong>{currentRecord.taskTitle}</p>
            <p><strong>奇遇日期：</strong>{currentRecord.adventureDate}</p>
            <p><strong>状态：</strong>{currentRecord.status === '0' ? '待接受' : currentRecord.status === '1' ? '进行中' : '已完成'}</p>
            <p><strong>换一换次数：</strong>{currentRecord.swapCount}</p>
            {currentRecord.checkinImg && (
              <div>
                <p><strong>打卡照片：</strong></p>
                <Image src={currentRecord.checkinImg} className={styles.imagePreview} />
              </div>
            )}
            {currentRecord.checkinComment && (
              <p><strong>打卡心得：</strong>{currentRecord.checkinComment}</p>
            )}
            {currentRecord.aiReply && (
              <p><strong>AI回信：</strong>{currentRecord.aiReply}</p>
            )}
            <p><strong>审核状态：</strong>{currentRecord.auditStatus === '1' ? '已审核' : '未审核'}</p>
            <p><strong>完成时间：</strong>{currentRecord.finishTime || '-'}</p>
          </div>
        )}
      </Modal>

      {/* 编辑弹窗 */}
      <Modal
        title="编辑手账"
        open={editModalVisible}
        onOk={handleEditModalOk}
        onCancel={() => setEditModalVisible(false)}
        width={700}
      >
        <Form form={modalForm} labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
          <Form.Item name="logId" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="checkinImg" label="打卡照片">
            <Input placeholder="请输入图片URL" />
          </Form.Item>
          <Form.Item name="checkinComment" label="打卡心得">
            <TextArea rows={4} placeholder="请输入打卡心得" />
          </Form.Item>
          <Form.Item name="aiReply" label="AI回信">
            <TextArea rows={4} placeholder="请输入AI回信" />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select placeholder="请选择状态">
              <Select.Option value="0">待接受</Select.Option>
              <Select.Option value="1">进行中</Select.Option>
              <Select.Option value="2">已完成</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="auditStatus" label="审核状态">
            <Select placeholder="请选择审核状态">
              <Select.Option value="0">未审核</Select.Option>
              <Select.Option value="1">已审核</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      {/* 审核弹窗 */}
      <Modal
        title="审核手账"
        open={auditModalVisible}
        onOk={handleAuditModalOk}
        onCancel={() => setAuditModalVisible(false)}
        width={700}
      >
        <Form form={auditForm} labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
          <Form.Item name="logId" hidden>
            <Input />
          </Form.Item>
          <Form.Item
            name="auditStatus"
            label="审核状态"
            rules={[{ required: true, message: '请选择审核状态' }]}
          >
            <Select placeholder="请选择审核状态">
              <Select.Option value="0">未审核</Select.Option>
              <Select.Option value="1">已审核</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="aiReply" label="AI回信">
            <TextArea rows={6} placeholder="请输入AI回信内容" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default LogList
