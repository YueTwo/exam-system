<template>
  <div class="app-container">
    <el-card>
      <el-form inline label-width="90px">
        <el-form-item label="选择考试">
          <el-select v-model="examId" filterable clearable placeholder="全部考试" style="width: 280px" @change="loadAll">
            <el-option v-for="ex in examList" :key="ex.id" :label="ex.title" :value="ex.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="Top">
          <el-input-number v-model="top" :min="1" :max="200" @change="loadAll" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-refresh" @click="loadAll">刷新</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 15px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="人员概览" name="person">
          <el-table :data="personList" border style="width: 100%">
            <el-table-column prop="realName" label="姓名" />
            <el-table-column prop="userName" label="账号" />
            <el-table-column prop="departName" label="部门" />
            <el-table-column prop="examCount" label="参与考试数" width="110" align="center" />
            <el-table-column prop="passCount" label="通过次数" width="110" align="center" />
            <el-table-column prop="avgScore" label="平均最高分" width="120" align="center" />
            <el-table-column prop="maxScore" label="最高分" width="90" align="center" />
            <el-table-column prop="totalTryCount" label="累计考试次数" width="130" align="center" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="部门概览" name="depart">
          <el-table :data="departList" border style="width: 100%">
            <el-table-column prop="departName" label="部门" />
            <el-table-column prop="userCount" label="部门用户数" width="120" align="center" />
            <el-table-column prop="examUserCount" label="参与考试用户数" width="140" align="center" />
            <el-table-column prop="passCount" label="通过记录数" width="120" align="center" />
            <el-table-column prop="avgScore" label="平均最高分" width="120" align="center" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="错题分析" name="wrong">
          <el-table :data="wrongList" border style="width: 100%">
            <el-table-column prop="title" label="题目" />
            <el-table-column prop="wrongCount" label="错误次数" width="120" align="center" />
            <el-table-column prop="userCount" label="涉及用户数" width="120" align="center" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { fetchList } from '@/api/exam/exam'
import { personOverview, departOverview, wrongOverview } from '@/api/exam/analysis'

export default {
  name: 'ExamAnalysis',
  data() {
    return {
      activeTab: 'person',
      examList: [],
      examId: '',
      top: 20,
      personList: [],
      departList: [],
      wrongList: []
    }
  },
  created() {
    this.loadExamList()
    this.loadAll()
  },
  methods: {
    loadExamList() {
      fetchList().then(res => {
        this.examList = (res.data && res.data.records) ? res.data.records : []
      })
    },
    buildParams() {
      const params = { top: this.top }
      if (this.examId) params.examId = this.examId
      return params
    },
    loadAll() {
      const params = this.buildParams()
      personOverview(params).then(res => {
        this.personList = res.data || []
      })
      departOverview(params).then(res => {
        this.departList = res.data || []
      })
      wrongOverview(params).then(res => {
        this.wrongList = res.data || []
      })
    }
  }
}
</script>

