<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="分享链接" name="share">
          <el-form label-width="110px">
            <el-form-item label="选择考试">
              <el-select v-model="shareForm.examId" filterable placeholder="请选择考试" style="width: 360px" @change="clearShareResult">
                <el-option v-for="ex in examList" :key="ex.id" :label="ex.title" :value="ex.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="过期时间(可选)">
              <el-date-picker v-model="shareForm.expireTime" type="datetime" placeholder="不设置表示不过期" style="width: 360px" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="shareLoading" @click="handleGenerate">生成链接</el-button>
              <el-button type="danger" :disabled="!shareForm.examId" :loading="shareLoading" @click="handleDisable">禁用分享</el-button>
            </el-form-item>

            <el-form-item v-if="shareResult.shareToken" label="分享链接">
              <el-input v-model="shareUrl" readonly />
              <el-button style="margin-top: 10px" @click="copyToClipboard(shareUrl)">复制链接</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="发送通知" name="notice">
          <el-form label-width="110px">
            <el-form-item label="选择考试">
              <el-select v-model="noticeForm.examId" filterable placeholder="请选择考试" style="width: 360px" @change="syncNoticeQuery">
                <el-option v-for="ex in examList" :key="ex.id" :label="ex.title" :value="ex.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="标题">
              <el-input v-model="noticeForm.title" />
            </el-form-item>
            <el-form-item label="内容">
              <el-input v-model="noticeForm.content" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="noticeLoading" @click="handleSendNotice">发送</el-button>
            </el-form-item>
          </el-form>

          <data-table
            ref="noticeTable"
            :options="noticeOptions"
            :list-query="noticeListQuery"
          >
            <template #data-columns>
              <el-table-column prop="title" label="标题" />
              <el-table-column prop="examId" label="考试ID" width="220" />
              <el-table-column prop="createTime" label="时间" width="170" />
              <el-table-column prop="content" label="内容" />
            </template>
          </data-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import { fetchList } from '@/api/exam/exam'
import { generateShare, disableShare } from '@/api/exam/share'
import { sendNotice } from '@/api/exam/notice'

export default {
  name: 'ExamShare',
  components: { DataTable },
  data() {
    return {
      activeTab: 'share',
      examList: [],
      shareLoading: false,
      shareForm: {
        examId: '',
        expireTime: null
      },
      shareResult: {},
      noticeLoading: false,
      noticeForm: {
        examId: '',
        title: '',
        content: ''
      },
      noticeOptions: {
        listUrl: '/exam/api/exam/notice/paging',
        multi: false
      },
      noticeListQuery: {
        current: 1,
        size: 10,
        params: {
          examId: ''
        }
      }
    }
  },
  computed: {
    shareUrl() {
      if (!this.shareResult.shareToken) return ''
      return `${window.location.origin}/#/my/exam/share/${this.shareResult.shareToken}`
    }
  },
  created() {
    this.loadExamList()
  },
  methods: {
    loadExamList() {
      fetchList().then(res => {
        this.examList = (res.data && res.data.records) ? res.data.records : []
      })
    },
    clearShareResult() {
      this.shareResult = {}
    },
    handleGenerate() {
      if (!this.shareForm.examId) {
        this.$message.warning('请选择考试！')
        return
      }
      this.shareLoading = true
      const payload = {
        examId: this.shareForm.examId
      }
      if (this.shareForm.expireTime) payload.expireTime = this.shareForm.expireTime
      generateShare(payload).then(res => {
        this.shareResult = res.data || {}
        this.$message.success('已生成分享链接')
      }).finally(() => {
        this.shareLoading = false
      })
    },
    handleDisable() {
      if (!this.shareForm.examId) return
      this.shareLoading = true
      disableShare({ id: this.shareForm.examId }).then(() => {
        this.shareResult = {}
        this.$message.success('已禁用分享')
      }).finally(() => {
        this.shareLoading = false
      })
    },
    copyToClipboard(text) {
      if (!text) return
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => {
          this.$message.success('已复制')
        }).catch(() => {
          this.$message.warning('复制失败，请手动复制')
        })
        return
      }
      this.$message.warning('当前浏览器不支持一键复制，请手动复制')
    },
    syncNoticeQuery() {
      this.noticeListQuery.params.examId = this.noticeForm.examId
    },
    handleSendNotice() {
      if (!this.noticeForm.examId) {
        this.$message.warning('请选择考试！')
        return
      }
      if (!this.noticeForm.title) {
        this.$message.warning('请填写标题！')
        return
      }
      if (!this.noticeForm.content) {
        this.$message.warning('请填写内容！')
        return
      }
      this.noticeLoading = true
      sendNotice({ ...this.noticeForm }).then(() => {
        this.$message.success('发送成功')
        this.noticeForm.title = ''
        this.noticeForm.content = ''
        if (this.$refs.noticeTable) this.$refs.noticeTable.getList()
      }).finally(() => {
        this.noticeLoading = false
      })
    }
  }
}
</script>

