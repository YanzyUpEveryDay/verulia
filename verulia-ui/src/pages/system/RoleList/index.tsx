import { useState, useEffect } from 'react'
import { Table, Button, Space, message, Modal, Form, Input } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { roleApi } from '@/api/role'
import type { Role } from '@/types/role'

function RoleList() {
  const [loading, setLoading] = useState(false)
  const [dataSource, setDataSource] = useState<Role[]>([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRole, setEditingRole] = useState<Role | null>(null)
  const [form] = Form.useForm()

  const columns = [
    {
      title: '角色ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: '角色名称',
      dataIndex: 'roleName',
      key: 'roleName',
    },
    {
      title: '权限字符',
      dataIndex: 'roleKey',
      key: 'roleKey',
    },
    {
      title: '显示顺序',
      dataIndex: 'roleSort',
      key: 'roleSort',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: number) => (status === 1 ? '正常' : '停用'),
    },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: Role) => (
        <Space>
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          <Button
            type="link"
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

  const fetchData = async () => {
    setLoading(true)
    try {
      const result = await roleApi.getRoleList({ pageNum, pageSize })
      setDataSource(result.list)
      setTotal(result.total)
    } catch (error: any) {
      message.error(error.message || '获取角色列表失败')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [pageNum])

  const handleAdd = () => {
    setEditingRole(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (record: Role) => {
    setEditingRole(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleDelete = (roleId: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个角色吗？',
      onOk: async () => {
        try {
          await roleApi.deleteRole(roleId)
          message.success('删除成功')
          fetchData()
        } catch (error: any) {
          message.error(error.message || '删除失败')
        }
      },
    })
  }

  const handleModalOk = async () => {
    try {
      const values = await form.validateFields()
      if (editingRole) {
        await roleApi.updateRole({ ...values, id: editingRole.id })
        message.success('更新成功')
      } else {
        await roleApi.addRole(values)
        message.success('添加成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error: any) {
      message.error(error.message || '操作失败')
    }
  }

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          新增角色
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={dataSource}
        loading={loading}
        rowKey="id"
        pagination={{
          current: pageNum,
          pageSize,
          total,
          onChange: (page) => setPageNum(page),
        }}
      />
      <Modal
        title={editingRole ? '编辑角色' : '新增角色'}
        open={modalVisible}
        onOk={handleModalOk}
        onCancel={() => setModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="角色名称"
            name="roleName"
            rules={[{ required: true, message: '请输入角色名称' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="权限字符"
            name="roleKey"
            rules={[{ required: true, message: '请输入权限字符' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="显示顺序"
            name="roleSort"
          >
            <Input type="number" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default RoleList
