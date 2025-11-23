import { post } from '@/utils/request'

export function login(data) {
  return post('/exam/api/sys/user/login', data)
}

export function getInfo(token) {
  // Use header token (axios interceptor will attach token from cookie), do not rely on query param
  return post('/exam/api/sys/user/info')
}

export function logout() {
  return post('/exam/api/sys/user/logout', {})
}

export function reg(data) {
  return post('/exam/api/sys/user/reg', data)
}
