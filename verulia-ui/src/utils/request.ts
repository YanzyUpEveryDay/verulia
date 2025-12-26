import axios from 'axios'
import type {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios'
import { message, Modal } from 'antd'
import { tokenStorage } from './storage'

// 防止重复弹出登录过期提示
let isShowingLoginExpiredModal = false

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
            baseURL: import.meta.env.VITE_API_BASE_URL,
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
                const token = tokenStorage.getToken()
                if (token) {
                    // Sa-Token配置: token-name=satoken
                    config.headers['Authorization'] = `Bearer ${token}`
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
                const { data, status } = response
                console.log(response)
                // HTTP状态码异常处理(非2xx)
                if (status < 200 || status >= 300) {
                    const errorMsg = data?.message || `请求失败(${status})`
                    message.error(errorMsg)
                    return Promise.reject(new Error(errorMsg))
                }

                // 统一通过业务状态码 data.code 判断
                switch (data.code) {
                    case 200:
                        // 请求成功,返回数据
                        return data.data as any
                    case 401:
                        // 未授权,清除认证信息并跳转登录
                        tokenStorage.removeToken()
                        // 防止多个401请求同时触发多个弹窗
                        if (!isShowingLoginExpiredModal) {
                            isShowingLoginExpiredModal = true
                            Modal.warning({
                                title: '登录已过期',
                                content: '您的登录信息已过期,请重新登录',
                                okText: '确认',
                                centered: true,
                                onOk: () => {
                                    isShowingLoginExpiredModal = false
                                    // 使用 React Router 导航，避免整页刷新
                                    // 保存当前路径，登录后可以返回
                                    const currentPath = window.location.pathname
                                    window.location.href = `/login?redirect=${encodeURIComponent(currentPath)}`
                                },
                                onCancel: () => {
                                    isShowingLoginExpiredModal = false
                                },
                            })
                        }
                        return Promise.reject(new Error(data.message || '未授权'))

                    case 403:
                        message.error(data.message || '拒绝访问')
                        return Promise.reject(new Error(data.message || '拒绝访问'))

                    case 404:
                        message.error(data.message || '请求的资源不存在')
                        return Promise.reject(new Error(data.message || '请求的资源不存在'))

                    case 500:
                        message.error(data.message || '服务器错误')
                        return Promise.reject(new Error(data.message || '服务器错误'))

                    default:
                        // 其他业务错误
                        message.error(data.message || '请求失败')
                        return Promise.reject(new Error(data.message || '请求失败'))
                }
            },
            (error) => {
                // 只处理网络错误和请求配置错误
                if (error.request && !error.response) {
                    message.error('网络错误,请检查网络连接')
                } else if (!error.request) {
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
