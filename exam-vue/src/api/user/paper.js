import { post } from '@/utils/request'

export function getAnswerOverview(paperId, userId) {
    return post('/exam/api/user/exam/result', { paperId: paperId, userId: userId })
}