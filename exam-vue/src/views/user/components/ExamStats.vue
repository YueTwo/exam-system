<template>
  <!-- 普通 div 容器，移除 el-dialog 弹窗 -->
  <div class="exam-stats-wrapper">
    <!-- 加载状态占位 -->
    <el-skeleton v-if="loading" active :rows="6" />

    <!-- 核心统计内容（加载完成后显示） -->
    <div v-else class="exam-stats-container">
      <!-- 题型正确率图表 -->
      <el-card style="margin-bottom: 20px;">
        <div slot="header">题型正确率分布</div>
        <div ref="chartContainer" style="width: 100%; height: 300px;"></div>
        <p
          v-if="!stats.questionTypeStats || stats.questionTypeStats.length === 0"
          style="text-align: center; padding: 20px; color: #999;"
        >
          暂无题型统计数据
        </p>
      </el-card>

      <!-- 答题明细 -->
      <el-card>
        <div slot="header">答题明细</div>
        <el-table :data="stats.answerDetail || []" border size="mini">
          <el-table-column label="题型" prop="type" align="center" />
          <el-table-column label="总题数" prop="total" align="center" />
          <el-table-column label="做对" prop="right" align="center" />
          <el-table-column label="做错" prop="wrong" align="center" />
          <el-table-column label="正确率" align="center">
            <template v-slot="scope">
              {{
                scope.row.total
                  ? ((scope.row.right / scope.row.total) * 100).toFixed(2) + "%"
                  : "0.00%"
              }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { fetchExamStats } from '@/api/user/book'

export default {
  name: "ExamStats", // 普通组件名，移除弹窗相关命名
  props: {
    // 父组件传入：考试ID（必传，用于请求数据）
    examId: {
      type: String,
      required: true
    },
    // 父组件传入：用户ID（必传，用于请求数据）
    userId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      loading: false, // 数据加载状态
      stats: {
        questionTypeStats: [], // 题型统计
        answerDetail: []       // 答题明细
      },
      chart: null // ECharts实例
    };
  },
  // 组件挂载时自动加载数据（替代弹窗的open事件）
  mounted() {
    this.fetchExamStats();
  },
  methods: {
    // 加载后端考试统计数据
    async fetchExamStats() {
      this.loading = true;
      try {
        // ========== 真实后端请求（替换为你的接口） ==========
        // const res = await this.$http.get(`/api/exam/stats/${this.examId}`, {
        //   params: { userId: this.userId }
        // });
        // const data = res.data;

        // 模拟后端请求（测试用，和真实接口格式一致）
        const res = await fetchExamStats(this.examId, this.userId);

        /**
         * 假设后端返回结构为：
         * {
         *   code: 0,
         *   data: {
         *     questionTypeStats: [],
         *     answerDetail: []
         *   },
         *   msg: ''
         * }
         */

        const statsList = res.data || res;

        // 1. 将后端字段映射并计算正确率
        this.stats = {
          questionTypeStats: (statsList || []).map(item => ({
            type: `题型${item.quType}`,
            total: item.total,
            rightCount: item.rightCount,
            rightRate: item.total > 0 ? ((item.rightCount / item.total) * 100).toFixed(2) : 0
          })),
          answerDetail: (statsList || []).map(item => ({
            type: `题型${item.quType}`,
            total: item.total,
            right: item.rightCount,
            wrong: item.total - item.rightCount
          }))
        };

        // 数据加载完成后初始化图表（确保DOM已渲染）
        this.$nextTick(() => {
          setTimeout(() => {
            this.initChart();
          }, 0);
        });
      } catch (err) {
        this.$message.error("获取考试统计数据失败！");
        console.error("数据请求失败：", err);
      } finally {
        this.loading = false;
      }
    },

    // 模拟后端接口返回（测试数据）
    mockFetchExamStats() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({
            questionTypeStats: [
              { type: "单选题", rightRate: 90 },
              { type: "多选题", rightRate: 80 },
              { type: "判断题", rightRate: 95 },
              { type: "简答题", rightRate: 75 }
            ],
            answerDetail: [
              { type: "单选题", total: 20, right: 18, wrong: 2 },
              { type: "多选题", total: 10, right: 8, wrong: 2 },
              { type: "判断题", total: 15, right: 14, wrong: 1 },
              { type: "简答题", total: 5, right: 4, wrong: 1 }
            ]
          });
        }, 500);
      });
    },

    // 初始化ECharts图表（改用ref获取DOM，更安全）
    initChart() {
      // 数据校验：无数据则不初始化
      if (!this.stats.questionTypeStats || this.stats.questionTypeStats.length === 0) return;

      // 用ref获取DOM容器（替代getElementById，避免ID冲突）
      const container = this.$refs.chartContainer;
      if (!container) return;

      // 销毁旧实例，避免重复创建
      if (this.chart) {
        this.chart.dispose();
      }

      // 初始化图表
      this.chart = echarts.init(container);
      const option = {
        tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
        grid: { left: "3%", right: "4%", bottom: "3%", containLabel: true },
        xAxis: {
          type: "category",
          data: this.stats.questionTypeStats.map(item => item.type)
        },
        yAxis: {
          type: "value",
          max: 100,
          name: "正确率(%)"
        },
        series: [
          {
            name: "正确率",
            type: "bar",
            barMaxWidth: 36,
            barCategoryGap: "50%",
            data: this.stats.questionTypeStats.map(item => item.rightRate),
            itemStyle: {
              borderRadius: [4, 4, 0, 0],
              color: params => {
                const colorList = ["#1890ff", "#52c41a", "#faad14", "#ff4d4f"];
                return colorList[params.dataIndex] || "#1890ff";
              }
            }
          }
        ]
      };
      this.chart.setOption(option);

      // 绑定窗口resize事件，图表自适应
      window.addEventListener("resize", this.handleChartResize);
    },

    // 图表自适应resize
    handleChartResize() {
      if (this.chart) {
        this.chart.resize();
      }
    }
  },
  // 组件销毁时清理资源
  beforeDestroy() {
    // 销毁图表实例
    if (this.chart) {
      this.chart.dispose();
      this.chart = null;
    }
    // 移除resize事件监听
    window.removeEventListener("resize", this.handleChartResize);
  }
};
</script>

<style scoped>
/* 普通容器样式 */
.exam-stats-wrapper {
  width: 100%;
  min-height: 400px;
  padding: 10px;
  box-sizing: border-box;
}

.exam-stats-container {
  width: 100%;
}

.el-card {
  margin-bottom: 15px;
  border-radius: 4px;
}
</style>