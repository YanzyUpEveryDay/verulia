import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { message } from 'antd'

// 后端统一响应结构
interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

class HttpClient {
  private instance: AxiosInstance

  constructor() {
    this.instance = axios.create({
      baseURL: '/api',
      timeout: 30000,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    this.setupInterceptors()
  }

  private setupInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        // 从 localStorage 获取 token 并添加到请求头
        const token = localStorage.getItem('token')
        if (token && config.headers) {
          // Sa-Token配置: token-name=satoken
          config.headers['satoken'] = `Bearer ${token}`
        }
        return config
      },
      (error) => {
        return Promise.reject(error)
      }
    )

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response: AxiosResponse<ApiResponse>) => {
        const { data } = response
        
        // 如果返回的状态码不是200，统一处理错误
        if (data.code !== 200) {
          message.error(data.message || '请求失败')
          return Promise.reject(new Error(data.message || '请求失败'))
        }

        // 直接返回data字段，API层不需要再处理response结构
        return data.data as any
      },
      (error) => {
        // 处理 HTTP 错误
        if (error.response) {
          switch (error.response.status) {
            case 401:
              message.error('未授权，请重新登录')
              // 清除本地存储的认证信息
              localStorage.removeItem('token')
              localStorage.removeItem('auth-storage')
              // 跳转到登录页
              window.location.href = '/login'
              break
            case 403:
              message.error('拒绝访问')
              break
            case 404:
              message.error('请求的资源不存在')
              break
            case 500:
              message.error('服务器错误')
              break
            default:
              message.error(error.response.data?.message || '请求失败')
          }
        } else if (error.request) {
          message.error('网络错误，请检查网络连接')
        } else {
          message.error('请求配置错误')
        }

        return Promise.reject(error)
      }
    )
  }

  // 封装后的方法直接返回data，不需要再包装ApiResponse
  public get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.get(url, config)
  }

  public post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.post(url, data, config)
  }

  public put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.put(url, data, config)
  }

  public delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.delete(url, config)
  }
}

export const httpClient = new HttpClient()
