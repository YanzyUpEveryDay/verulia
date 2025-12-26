import { useState, useEffect } from 'react'
import { Table, Button, Form, Input, Select, Space, Modal, message, Tag, Card } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import type { AdvTask, AdvTaskQuery } from '@/api/advTask'
import { advTaskApi } from '@/api/advTask'
import styles from './TaskList.module.css'

const { TextArea } = Input

const TaskList = () => {
  const [form] = Form.useForm()
  const [modalForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [dataSource, setDataSource] = useState<AdvTask[]>([])
  const [total, setTotal] = useState(0)
  const [query, setQuery] = useState<AdvTaskQuery>({
    pageNum: 1,
    pageSize: 10,
  })
  const [modalVisible, setModalVisible] = useState(false)
  const [modalTitle, setModalTitle] = useState('新增奇遇任务')
  // const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([])

  // 奇遇分类选项
  const categoryOptions = [
    { label: '探索', value: 'EXPLORE' },
    { label: '战斗', value: 'BATTLE' },
    { label: '收集', value: 'COLLECT' },
    { label: '社交', value: 'SOCIAL' },
    { label: '美食', value: 'FOOD' },
    { label: '健康', value: 'HEALTH' },
    { label: '整理', value: 'CLEAN' },
    { label: '学习', value: 'LEARN' },
    { label: '其他', value: 'OTHER' },
  ]

  // 难度等级选项
  const difficultyOptions = [
    { label: '简单', value: 1 },
    { label: '普通', value: 2 },
    { label: '困难', value: 3 },
    { label: '极难', value: 4 },
    { label: '噩梦', value: 5 },
  ]

  // 状态选项
  const statusOptions = [
    { label: '全部', value: undefined },
    { label: '正常', value: '1' },
    { label: '停用', value: '0' },
  ]

  // 加载数据
  const loadData = async () => {
    setLoading(true)
    try {
      const data = await advTaskApi.getPageList(query)
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
    setQuery({ ...query, ...values, pageNum: 1 })
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

  // 新增
  const handleAdd = () => {
    setModalTitle('新增奇遇任务')
    setModalVisible(true)
    modalForm.resetFields()
  }

  // 编辑
  const handleEdit = async (record: AdvTask) => {
    setModalTitle('编辑奇遇任务')
    setModalVisible(true)
    modalForm.setFieldsValue(record)
  }

  // 删除
  const handleDelete = (id: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该奇遇任务吗？',
      onOk: async () => {
        try {
          await advTaskApi.delete(id)
          message.success('删除成功')
          loadData()
        } catch (error) {
          message.error('删除失败')
        }
      },
    })
  }

  // 提交表单
  const handleModalOk = async () => {
    try {
      const values = await modalForm.validateFields()
      if (values.id) {
        await advTaskApi.update(values)
      } else {
        await advTaskApi.create(values)
      }
      message.success('保存成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('表单验证失败:', error)
    }
  }

  // 表格列定义
  const columns: ColumnsType<AdvTask> = [
    {
      title: '任务ID',
      dataIndex: 'id',
      width: 80,
    },
    {
      title: '奇遇标题',
      dataIndex: 'title',
      width: 200,
    },
    {
      title: '奇遇内容',
      dataIndex: 'content',
      width: 300,
      ellipsis: true,
    },
    {
      title: '分类',
      dataIndex: 'category',
      width: 100,
      render: category => {
        const categoryMap: Record<string, { label: string; color: string }> = {
          EXPLORE: { label: '探索', color: 'blue' },
          BATTLE: { label: '战斗', color: 'red' },
          COLLECT: { label: '收集', color: 'green' },
          SOCIAL: { label: '社交', color: 'purple' },
          FOOD: { label: '美食', color: 'green' },
          HEALTH: { label: '健康', color: 'green' },
          CLEAN: { label: '整理', color: 'purple' },
          LEARN: { label: '学习', color: 'purple' },
          OTHER: { label: '其他', color: 'default' },
        }
        const categoryInfo = categoryMap[category] || { label: category, color: 'default' }
        return <Tag color={categoryInfo.color}>{categoryInfo.label}</Tag>
      },
    },
    {
      title: '难度',
      dataIndex: 'difficulty',
      width: 100,
      render: difficulty => {
        const difficultyMap: Record<number, { label: string; color: string }> = {
          1: { label: '简单', color: 'green' },
          2: { label: '普通', color: 'blue' },
          3: { label: '困难', color: 'orange' },
          4: { label: '极难', color: 'red' },
          5: { label: '噩梦', color: 'purple' },
        }
        const difficultyInfo = difficultyMap[difficulty] || { label: '-', color: 'default' }
        return <Tag color={difficultyInfo.color}>{difficultyInfo.label}</Tag>
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      render: status => (
        <Tag color={status === '1' ? 'success' : 'error'}>{status === '1' ? '正常' : '停用'}</Tag>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            size="small"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          <Button
            type="link"
            size="small"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.id)}
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
          <Form.Item name="title" label="标题">
            <Input placeholder="请输入标题" allowClear />
          </Form.Item>
          <Form.Item name="category" label="分类">
            <Select
              placeholder="请选择分类"
              style={{ width: 180 }}
              allowClear
              options={categoryOptions}
            />
          </Form.Item>
          <Form.Item name="difficulty" label="难度">
            <Select
              placeholder="请选择难度"
              style={{ width: 180 }}
              allowClear
              options={difficultyOptions}
            />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select
              placeholder="请选择状态"
              style={{ width: 180 }}
              allowClear
              options={statusOptions}
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
          <Space>
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增任务
            </Button>
          </Space>
          <Button icon={<ReloadOutlined />} onClick={loadData}>
            刷新
          </Button>
        </div>

        {/* 表格 */}
        <Table
          rowKey="id"
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
          scroll={{ x: 1400 }}
        />
      </Card>

      {/* 新增/编辑弹窗 */}
      <Modal
        title={modalTitle}
        open={modalVisible}
        onOk={handleModalOk}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={modalForm} labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
          <Form.Item name="id" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="title" label="标题" rules={[{ required: true, message: '请输入标题' }]}>
            <Input placeholder="请输入标题" />
          </Form.Item>
          <Form.Item
            name="content"
            label="内容"
            rules={[{ required: true, message: '请输入内容' }]}
          >
            <TextArea rows={4} placeholder="请输入内容" />
          </Form.Item>
          <Form.Item
            name="category"
            label="分类"
            rules={[{ required: true, message: '请选择分类' }]}
          >
            <Select placeholder="请选择分类" options={categoryOptions} />
          </Form.Item>
          <Form.Item
            name="difficulty"
            label="难度"
            rules={[{ required: true, message: '请选择难度' }]}
          >
            <Select placeholder="请选择难度" options={difficultyOptions} />
          </Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true, message: '请选择状态' }]}>
            <Select placeholder="请选择状态">
              <Select.Option value="1">正常</Select.Option>
              <Select.Option value="0">停用</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <TextArea rows={2} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default TaskList
