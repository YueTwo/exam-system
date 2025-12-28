<template>

  <div>
    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
    >
      <template #filter-content>

        <el-input v-model="listQuery.params.title" placeholder="搜索考试名称" style="width: 200px;" class="filter-item" />

      </template>

      <template #data-columns>

        <el-table-column
          label="考试名称"
          prop="title"
          show-overflow-tooltip
        />

        <el-table-column
          label="考试次数"
          prop="tryCount"
          align="center"
        />

        <el-table-column
          label="最高分"
          prop="maxScore"
          align="center"
        />

        <el-table-column
          label="是否通过"
          align="center"
        >

          <template v-slot="scope">
            <span v-if="scope.row.passed" style="color: #00ff00;">通过</span>
            <span v-else style="color: #ff0000;">未通过</span>
          </template>

        </el-table-column>

        <el-table-column
          label="最后考试时间"
          prop="updateTime"
          align="center"
        />

        <el-table-column
          label="操作"
          align="center"
        >
          <template v-slot="scope">
            <el-button type="primary" size="mini" icon="el-icon-view" @click="handleExamDetail(scope.row.examId)">详情</el-button>
            <el-button type="warning" size="mini" icon="el-icon-close" @click="handlerExamBook(scope.row.examId)">错题</el-button>
            <el-button type="primary" size="mini" icon="el-icon-view" @click="handlerExamStats(scope.row.examId)">总览</el-button>
          </template>

        </el-table-column>

      </template>

    </data-table>

    <el-dialog :visible.sync="dialogVisible" title="考试明细" width="60%">

      <div class="el-dialog-div">
        <paper-card :exam-id="examId" :user-id="userId" />
      </div>

    </el-dialog>

    <el-dialog :visible.sync="graphVisible" title="总览" width="60%">
      <div class="el-dialog-div">
        <exam-stats :key="examId" :exam-id="examId" :user-id="userId"/>
      </div>
    </el-dialog>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import DataTable from '@/components/DataTable'
import ExamStats from '@/views/user/components/ExamStats'
import PaperCard from '@/views/user/components/PaperCard'

export default {
  name: 'MyExamList',
  components: { DataTable, ExamStats, PaperCard},
  data() {
    return {

      dialogVisible: false,
      graphVisible: false,
      examId: '',

      listQuery: {
        current: 1,
        size: 10,
        params: {
          title: ''
        }
      },

      options: {
        // 可批量操作
        multi: false,
        // 列表请求URL
        listUrl: '/exam/api/user/exam/my-paging'
      }
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  methods: {

    // 开始考试
    handleExamDetail(examId) {
      this.examId = examId
      this.dialogVisible = true
    },

    handlerExamBook(examId) {
      this.$router.push({ name: 'BookList', params: { examId: examId }})
    },

    handlerExamStats(examId) {
      this.examId = examId
      this.graphVisible = true
    }
  }
}
</script>

<style scoped>

  .el-dialog-div{
    height: 60vh;
    overflow: auto;
    padding: 10px;
  }

</style>
