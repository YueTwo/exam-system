<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="配置售价" name="price">
          <el-form label-width="110px">
            <el-form-item label="选择考试">
              <el-select v-model="priceForm.examId" filterable placeholder="请选择考试" style="width: 360px" @change="loadExamPrice">
                <el-option v-for="ex in examList" :key="ex.id" :label="ex.title" :value="ex.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="售价(元)">
              <el-input-number v-model="priceForm.priceYuan" :min="0" :step="0.5" :precision="2" />
              <span style="margin-left: 10px; color: #999">0 表示免费</span>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :disabled="!priceForm.examId" :loading="priceLoading" @click="handleSavePrice">保存</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="考试订单" name="order">
          <data-table
            ref="orderTable"
            :options="orderOptions"
            :list-query="orderListQuery"
          >
            <template #filter-content>
              <el-select v-model="orderListQuery.params.examId" filterable clearable placeholder="选择考试" class="filter-item" style="width: 240px">
                <el-option v-for="ex in examList" :key="ex.id" :label="ex.title" :value="ex.id" />
              </el-select>

              <el-select v-model="orderListQuery.params.status" clearable placeholder="订单状态" class="filter-item" style="width: 140px">
                <el-option :value="0" label="待支付" />
                <el-option :value="1" label="已支付" />
                <el-option :value="2" label="已取消" />
              </el-select>
            </template>

            <template #data-columns>
              <el-table-column prop="id" label="订单ID" width="220" />
              <el-table-column prop="examTitle" label="考试" />
              <el-table-column prop="userName" label="用户" width="140" />
              <el-table-column label="金额(元)" width="100" align="center">
                <template v-slot="scope">
                  {{ (scope.row.amountCent || 0) / 100 }}
                </template>
              </el-table-column>
              <el-table-column label="状态" width="100" align="center">
                <template v-slot="scope">
                  <span v-if="scope.row.status===0">待支付</span>
                  <span v-else-if="scope.row.status===1" style="color:#24da70">已支付</span>
                  <span v-else>已取消</span>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="170" />
              <el-table-column prop="payTime" label="支付时间" width="170" />
              <el-table-column label="操作" width="120" align="center">
                <template v-slot="scope">
                  <el-button v-if="scope.row.status===0" size="mini" type="primary" @click="handleMarkPaid(scope.row.id)">标记已支付</el-button>
                </template>
              </el-table-column>
            </template>
          </data-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import { fetchList, fetchDetail } from '@/api/exam/exam'
import { savePrice, markPaid } from '@/api/exam/pay'

export default {
  name: 'ExamPay',
  components: { DataTable },
  data() {
    return {
      activeTab: 'price',
      examList: [],
      priceLoading: false,
      priceForm: {
        examId: '',
        priceYuan: 0
      },
      orderOptions: {
        listUrl: '/exam/api/exam/pay/order/paging',
        multi: false
      },
      orderListQuery: {
        current: 1,
        size: 10,
        params: {
          examId: '',
          status: null
        }
      }
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
    loadExamPrice() {
      if (!this.priceForm.examId) return
      fetchDetail(this.priceForm.examId).then(res => {
        const priceCent = res.data ? res.data.priceCent : 0
        this.priceForm.priceYuan = (priceCent || 0) / 100
      })
    },
    handleSavePrice() {
      if (!this.priceForm.examId) return
      this.priceLoading = true
      const priceCent = Math.round((this.priceForm.priceYuan || 0) * 100)
      savePrice({ examId: this.priceForm.examId, priceCent: priceCent }).then(() => {
        this.$message.success('保存成功')
      }).finally(() => {
        this.priceLoading = false
      })
    },
    handleMarkPaid(orderId) {
      this.$confirm('确认标记为已支付？', '提示', { type: 'warning' }).then(() => {
        markPaid({ orderId: orderId }).then(() => {
          this.$message.success('操作成功')
          if (this.$refs.orderTable) this.$refs.orderTable.getList()
        })
      })
    }
  }
}
</script>

