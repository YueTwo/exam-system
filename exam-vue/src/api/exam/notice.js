import { post } from '@/utils/request'

export function sendNotice(data) {
  return post('/exam/api/exam/notice/send', data)
}

export function noticePaging(data) {
  return post('/exam/api/exam/notice/paging', data)
}

