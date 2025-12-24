<template>
  <div class="app-container">
    <el-card>
      <div v-loading="loading" style="min-height: 60px">
        <div v-if="error" style="color: #ff0000">{{ error }}</div>
        <div v-else>正在跳转到考试...</div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { resolveShare } from '@/api/exam/share'

export default {
  name: 'ShareExamRedirect',
  data() {
    return {
      loading: true,
      error: ''
    }
  },
  created() {
    const token = this.$route.params.shareToken
    if (!token) {
      this.loading = false
      this.error = '分享链接无效'
      return
    }
    resolveShare({ shareToken: token }).then(res => {
      const examId = res.data ? res.data.examId : ''
      if (!examId) {
        this.error = '分享链接无效或已失效'
        return
      }
      this.$router.replace({ name: 'PreExam', params: { examId: examId } })
    }).catch((e) => {
      this.error = (e && e.message) ? e.message : '分享链接无效或已失效'
    }).finally(() => {
      this.loading = false
    })
  }
}
</script>

