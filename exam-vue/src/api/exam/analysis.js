import { post } from '@/utils/request'

export function personOverview(data) {
  return post('/exam/api/exam/analysis/person-overview', data || {})
}

export function departOverview(data) {
  return post('/exam/api/exam/analysis/depart-overview', data || {})
}

export function wrongOverview(data) {
  return post('/exam/api/exam/analysis/wrong-overview', data || {})
}

