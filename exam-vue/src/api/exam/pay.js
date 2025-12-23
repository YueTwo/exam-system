import { post } from '@/utils/request'

export function savePrice(data) {
  return post('/exam/api/exam/pay/price/save', data)
}

export function createOrder(data) {
  return post('/exam/api/exam/pay/order/create', data)
}

export function orderPaging(data) {
  return post('/exam/api/exam/pay/order/paging', data)
}

export function markPaid(data) {
  return post('/exam/api/exam/pay/order/mark-paid', data)
}

