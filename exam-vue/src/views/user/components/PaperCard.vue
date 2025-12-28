<template>

  <div>

    <el-card v-for="item in paperList" :key=item.id style="margin-bottom: 10px; line-height: 30px;">

      <el-row :gutter="20">
        <el-col :span="12">
          考试时间：{{ item.createTime }}
        </el-col>

        <el-col :span="12">
          考试用时：{{ item.userTime }}分钟
        </el-col>

        <el-col :span="12">
          考试得分：{{ item.userScore }}
        </el-col>

        <el-col :span="12">
          是否合格：{{ item.userScore > item.qualifyScore ?'是':'否' }}
        </el-col>

        <el-col :span="12">
          考试状态：{{ item.state | paperStateFilter }}
        </el-col>

        <el-col :span="12">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-list"
            @click="handleAnswerOverview(item.id)"
          >
            查看试卷
          </el-button>
        </el-col>

      </el-row>

    </el-card>

    <el-drawer
      :visible.sync="overviewVisible"
      title="试卷详情"
      direction="rtl"
      size="80%"
      append-to-body
    >
      <!-- 答题总览内容（和之前一致） -->
      <div style="max-height: 90vh; overflow-y: auto; padding: 10px;">
        <el-collapse v-model="activeAnalysisIds">
          <el-collapse-item
            v-for="(question, index) in answerList"
            :key="question.id"
            :name="question.id"
            style="margin-bottom: 8px; border: 1px solid #f0f0f0; border-radius: 4px;"
          >
            <template #title>
              <div style="display: flex; justify-content: space-between; align-items: center; width: 100%;">
                <span>
                  第{{ index + 1 }}题（{{ getQuestionTypeText(question.quType) }}）
                  <el-tag :type="question.isRight === 1 ? 'success' : 'danger'" size="mini" style="margin-left: 8px;">
                    {{ question.isRight === 1 ? '正确' : '错误' }}
                  </el-tag>
                  <el-tag :type="question.answered ? 'primary' : 'warning'" size="mini" style="margin-left: 8px;">
                    {{ question.answered ? '已作答' : '未作答' }}
                  </el-tag>
                </span>
                <span style="color: #666;">分值：{{ question.score }}分</span>
              </div>
            </template>

            <div style="padding: 10px 0;">
              <!-- 题目内容 -->
              <div style="font-weight: 600; margin-bottom: 12px; line-height: 1.6;">
                题目：{{ question.quContent || '无题目内容' }}
              </div>

              <!-- 选择类题目（单选/多选/判断）：渲染选项列表 -->
              <div v-if="question.optionList && question.optionList.length" style="margin: 12px 0;">
                <div
                  v-for="(opt, optIndex) in question.optionList"
                  :key="optIndex"
                  style="margin: 6px 0; padding: 6px 10px; border-radius: 4px;"
                  :style="{
                    backgroundColor: opt.isCorrect ? '#f0f9ff' : (opt.isUserAnswer ? '#fef0f0' : '#ffffff'),
                    border: opt.isCorrect || opt.isUserAnswer ? '1px solid #e6e6e6' : '1px solid #f5f5f5',
                  }"
                >
                  <span
                    style="font-weight: 600; margin-right: 8px;"
                    :style="{
                      color: opt.isCorrect ? '#67c23a' : (opt.isUserAnswer ? '#f56c6c' : '#909399'),
                    }"
                  >
                    {{ opt.label }}.
                  </span>
                  <span style="line-height: 1.6;">
                    {{ opt.content || '无选项内容' }}
                    <span v-if="opt.isCorrect" style="color: #67c23a; font-size: 12px; margin-left: 8px;">【正确答案】</span>
                    <span v-if="opt.isUserAnswer && !opt.isCorrect" style="color: #f56c6c; font-size: 12px; margin-left: 8px;">【你的答案（错误）】</span>
                  </span>
                </div>
              </div>

              <!-- 非选择类题目：显示用户答案和正确答案 -->
              <div v-else style="margin: 12px 0; line-height: 1.8;">
                <div>
                  <span style="color: #666; font-weight: 500;">你的答案：</span>
                  <span>{{ formatUserAnswer(question) }}</span>
                </div>
                <div style="margin-top: 4px;">
                  <span style="color: #666; font-weight: 500;">正确答案：</span>
                  <span style="color: #67c23a; font-weight: 600;">{{ question.correctAnswer || '无' }}</span>
                </div>
              </div>

              <!-- 解析部分 -->
              <div style="margin-top: 12px; padding-top: 12px; border-top: 1px dashed #e6e6e6;">
                <div style="font-weight: 600; color: #666; margin-bottom: 4px;">解析：</div>
                <div style="color: #909399; line-height: 1.6; white-space: pre-line;">
                  {{ question.quAnalysis && question.quAnalysis.trim() ? question.quAnalysis : '暂无解析' }}
                </div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-drawer>

  </div>

</template>

<script>

import { listPaper } from '@/api/paper/paper'
import { getAnswerOverview } from '../../../api/user/paper'

export default {

  name: 'UserPaperList',
  props: {
    examId: {
      type: String,
      default: ''
    },
    userId: {
      type: String,
      default: ''
    }
  },

  data() {
    return {
      value1: null,
      overviewVisible: false,
      paperList: [],
      answerList: [],
      activeAnalysisIds: []
    }
  },

  watch: {

    // 检测查询变化
    examId: {
      handler() {
        this.fetchPaperList()
      },
      deep: true
    },

    // 检测查询变化
    userId: {
      handler() {
        this.fetchPaperList()
      },
      deep: true
    }
  },

  created() {
    this.fetchPaperList()
  },
  methods: {

    fetchPaperList() {
      listPaper(this.userId, this.examId).then(response => {
        this.paperList = response.data.records
      })
    },
    handleAnswerOverview(paperId) {
      if (!paperId) {
        this.$message.warning('考试记录ID不能为空')
        return
      }
      // 调用答题总览接口
      getAnswerOverview(paperId, this.userId).then(res => {
        if (res.code === 0) {
          // 对返回的数据进行预处理：解析correctAnswer为选项列表
          this.answerList = res.data.map(item => ({
            ...item,
            // 补充id（后端返回的是quId，折叠面板需要唯一key）
            id: item.quId,
            // 解析correctAnswer：选择类=选项列表，非选择类=原字符串
            optionList: this.parseCorrectAnswer(item)
          }));
          this.overviewVisible = true // 打开弹窗
        }
      }).catch(err => {
        this.$message.error('获取答题总览失败：' + (err.msg || '网络异常'))
      })
    },

    // 新增：获取题型文本（如1=单选，2=多选，3=判断，4=简答）
    getQuestionTypeText(type) {
      const typeMap = {
        1: '单选题',
        2: '多选题',
        3: '判断题',
        4: '填空题',
        5: '简答题'
      }
      return typeMap[type] || '未知题型'
    },
    parseCorrectAnswer(question) {
      // 选择类题目（1=单选，2=多选，3=判断）
      if ([1, 2, 3].includes(question.quType)) {
        try {
          // 解析JSON字符串为选项列表
          const optionList = JSON.parse(question.correctAnswer || '[]');
          console.log(optionList)
          // 标记用户答案（后端userAnswer是字符串，如"A,B"）
          const userAnswers = question.answer ? question.answer.split(',').map(item => item.trim()) : []
          return optionList.map(opt => ({
            ...opt,
            // 标记用户是否选择了该选项
            isUserAnswer: userAnswers.includes(opt.label),
          }))
        } catch (e) {
          console.error(`解析题目${question.quId}选项失败：`, e)
          return []
        }
      }
      // 非选择类题目：返回空列表
      return question.correctAnswer
    },
    formatUserAnswer(question) {
      if (question.answered !== 1) return '未作答'
      if (!question.answer) return '未作答'
      // 选择类题目：格式化答案（如"A,B" → "A、B"）
      if ([1, 2, 3].includes(question.quType)) {
        return question.answer.split(',').join('、')
      }
      // 非选择类：直接返回
      return question.answer || '未作答'
    }
  }
}
</script>

<style scoped>
  /* 优化折叠面板样式 */
  ::v-deep .el-collapse-item__header {
    background-color: #f8f9fa;
    padding: 10px 15px;
  }
  ::v-deep .el-collapse-item__content {
    padding: 0 15px;
  }
</style>
