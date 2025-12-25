import { useState, useEffect } from 'react'
import { Table, Button, Form, Input, Select, Space, Modal, message, Tag, Avatar, Card } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, ReloadOutlined, UserOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import type { Member, MemberQuery, MemberForm } from '@/types/member'
import { memberApi } from '@/api/member'
import styles from './MemberList.module.css'

const MemberList = () => {
  const [form] = Form.useForm()
  const [modalForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [dataSource, setDataSource] = useState<Member[]>([])
  const [total, setTotal] = useState(0)
  const [query, setQuery] = useState<MemberQuery>({
    current: 1,
    size: 10,
  })
  const [modalVisible, setModalVisible] = useState(false)
  const [modalTitle, setModalTitle] = useState('新增会员')
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([])

  // 会员等级选项
  const levelOptions = [
    { label: '普通会员', value: 1 },
    { label: '银卡会员', value: 2 },
    { label: '金卡会员', value: 3 },
    { label: 'VIP会员', value: 4 },
  ]

  // 状态选项
  const statusOptions = [
    { label: '全部', value: undefined },
    { label: '正常', value: 1 },
    { label: '禁用', value: 0 },
  ]

  // 性别映射
  const sexMap: Record<number, string> = {
    0: '未知',
    1: '男',
    2: '女',
  }

  // 加载数据
  const loadData = async () => {
    setLoading(true)
    try {
      // TODO: 接口接入后取消注释
      // const data = await memberApi.getPageList(query)
      // setDataSource(data.records)
      // setTotal(data.total)
      
      // 模拟数据
      const mockData: Member[] = [
        {
          id: 1,
          memberNo: 'M202501001',
          nickname: '张三',
          avatar: '',
          phone: '13800138000',
          email: 'zhangsan@example.com',
          sex: 1,
          level: 3,
          levelName: '金卡会员',
          points: 1500,
          balance: 299.00,
          status: 1,
          registerTime: '2024-01-15 10:30:00',
          lastLoginTime: '2024-12-25 09:15:00',
        },
        {
          id: 2,
          memberNo: 'M202501002',
          nickname: '李四',
          phone: '13900139000',
          sex: 2,
          level: 2,
          levelName: '银卡会员',
          points: 800,
          balance: 150.50,
          status: 1,
          registerTime: '2024-02-20 14:20:00',
          lastLoginTime: '2024-12-24 16:30:00',
        },
      ]
      setDataSource(mockData)
      setTotal(mockData.length)
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
    setQuery({ ...query, ...values, current: 1 })
  }

  // 重置
  const handleReset = () => {
    form.resetFields()
    setQuery({ current: 1, size: 10 })
  }

  // 分页变化
  const handleTableChange = (page: number, pageSize: number) => {
    setQuery({ ...query, current: page, size: pageSize })
  }

  // 新增
  const handleAdd = () => {
    setModalTitle('新增会员')
    setModalVisible(true)
    modalForm.resetFields()
  }

  // 编辑
  const handleEdit = async (record: Member) => {
    setModalTitle('编辑会员')
    setModalVisible(true)
    // TODO: 接口接入后加载详情
    modalForm.setFieldsValue(record)
  }

  // 删除
  const handleDelete = (id: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该会员吗？',
      onOk: async () => {
        try {
          // TODO: 接口接入后取消注释
          // await memberApi.delete(id)
          message.success('删除成功')
          loadData()
        } catch (error) {
          message.error('删除失败')
        }
      },
    })
  }

  // 批量删除
  const handleBatchDelete = () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要删除的会员')
      return
    }
    Modal.confirm({
      title: '确认删除',
      content: `确定要删除选中的 ${selectedRowKeys.length} 个会员吗？`,
      onOk: async () => {
        try {
          // TODO: 接口接入后取消注释
          // await memberApi.batchDelete(selectedRowKeys)
          message.success('删除成功')
          setSelectedRowKeys([])
          loadData()
        } catch (error) {
          message.error('删除失败')
        }
      },
    })
  }

  // 修改状态
  const handleStatusChange = async (id: number, status: number) => {
    try {
      // TODO: 接口接入后取消注释
      // await memberApi.updateStatus(id, status)
      message.success('状态修改成功')
      loadData()
    } catch (error) {
      message.error('状态修改失败')
    }
  }

  // 提交表单
  const handleModalOk = async () => {
    try {
      const values = await modalForm.validateFields()
      // TODO: 接口接入后取消注释
      // if (values.id) {
      //   await memberApi.update(values)
      // } else {
      //   await memberApi.create(values)
      // }
      message.success('保存成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('表单验证失败:', error)
    }
  }

  // 表格列定义
  const columns: ColumnsType<Member> = [
    {
      title: '会员编号',
      dataIndex: 'memberNo',
      width: 120,
    },
    {
      title: '会员信息',
      key: 'memberInfo',
      width: 200,
      render: (_, record) => (
        <Space>
          <Avatar src={record.avatar} icon={<UserOutlined />} />
          <div>
            <div>{record.nickname}</div>
            <div style={{ fontSize: 12, color: '#999' }}>{record.phone}</div>
          </div>
        </Space>
      ),
    },
    {
      title: '性别',
      dataIndex: 'sex',
      width: 80,
      render: (sex) => sexMap[sex] || '-',
    },
    {
      title: '会员等级',
      dataIndex: 'levelName',
      width: 120,
      render: (levelName, record) => {
        const colors = ['default', 'blue', 'gold', 'red']
        return <Tag color={colors[record.level - 1]}>{levelName}</Tag>
      },
    },
    {
      title: '积分',
      dataIndex: 'points',
      width: 100,
      align: 'right',
    },
    {
      title: '余额',
      dataIndex: 'balance',
      width: 100,
      align: 'right',
      render: (balance) => `¥${balance.toFixed(2)}`,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      render: (status, record) => (
        <Tag color={status === 1 ? 'success' : 'error'} style={{ cursor: 'pointer' }}
          onClick={() => handleStatusChange(record.id, status === 1 ? 0 : 1)}>
          {status === 1 ? '正常' : '禁用'}
        </Tag>
      ),
    },
    {
      title: '注册时间',
      dataIndex: 'registerTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            编辑
          </Button>
          <Button type="link" size="small" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
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
          <Form.Item name="memberNo" label="会员编号">
            <Input placeholder="请输入会员编号" allowClear />
          </Form.Item>
          <Form.Item name="nickname" label="昵称">
            <Input placeholder="请输入昵称" allowClear />
          </Form.Item>
          <Form.Item name="phone" label="手机号">
            <Input placeholder="请输入手机号" allowClear />
          </Form.Item>
          <Form.Item name="level" label="会员等级">
            <Select placeholder="请选择会员等级" style={{ width: 120 }} allowClear options={levelOptions} />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select placeholder="请选择状态" style={{ width: 100 }} allowClear options={statusOptions} />
          </Form.Item>
          <Form.Item>
            <Space>
              <Button type="primary" onClick={handleSearch}>搜索</Button>
              <Button onClick={handleReset}>重置</Button>
            </Space>
          </Form.Item>
        </Form>

        {/* 工具栏 */}
        <div className={styles.toolbar}>
          <Space>
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增会员
            </Button>
            <Button danger icon={<DeleteOutlined />} onClick={handleBatchDelete}>
              批量删除
            </Button>
          </Space>
          <Button icon={<ReloadOutlined />} onClick={loadData}>刷新</Button>
        </div>

        {/* 表格 */}
        <Table
          rowKey="id"
          columns={columns}
          dataSource={dataSource}
          loading={loading}
          rowSelection={{
            selectedRowKeys,
            onChange: (keys) => setSelectedRowKeys(keys as number[]),
          }}
          pagination={{
            current: query.current,
            pageSize: query.size,
            total,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total) => `共 ${total} 条`,
            onChange: handleTableChange,
          }}
          scroll={{ x: 1200 }}
        />
      </Card>

      {/* 新增/编辑弹窗 */}
      <Modal
        title={modalTitle}
        open={modalVisible}
        onOk={handleModalOk}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={modalForm} labelCol={{ span: 6 }} wrapperCol={{ span: 16 }}>
          <Form.Item name="id" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="nickname" label="昵称" rules={[{ required: true, message: '请输入昵称' }]}>
            <Input placeholder="请输入昵称" />
          </Form.Item>
          <Form.Item name="phone" label="手机号" rules={[
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
          ]}>
            <Input placeholder="请输入手机号" />
          </Form.Item>
          <Form.Item name="email" label="邮箱" rules={[
            { type: 'email', message: '请输入正确的邮箱' }
          ]}>
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item name="sex" label="性别">
            <Select placeholder="请选择性别">
              <Select.Option value={0}>未知</Select.Option>
              <Select.Option value={1}>男</Select.Option>
              <Select.Option value={2}>女</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="birthday" label="生日">
            <Input type="date" />
          </Form.Item>
          <Form.Item name="level" label="会员等级" rules={[{ required: true, message: '请选择会员等级' }]}>
            <Select placeholder="请选择会员等级" options={levelOptions} />
          </Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true, message: '请选择状态' }]}>
            <Select placeholder="请选择状态">
              <Select.Option value={1}>正常</Select.Option>
              <Select.Option value={0}>禁用</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default MemberList
