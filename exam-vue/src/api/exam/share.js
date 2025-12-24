import { post } from '@/utils/request'

export function generateShare(data) {
  return post('/exam/api/exam/share/generate', data)
}

export function disableShare(data) {
  return post('/exam/api/exam/share/disable', data)
}

export function resolveShare(data) {
  return post('/exam/api/exam/share/resolve', data)
}

