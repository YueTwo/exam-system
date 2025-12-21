<template>
  <div class="app-container">

    <el-form ref="postForm" :model="postForm" :rules="rules" label-position="top" label-width="100%">

      <!-- 题库基本信息 - 只读展示 -->
      <el-card v-if="postForm.id" class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span style="font-weight: bold;">题库基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">题库ID：</span>
              <span class="info-value">{{ postForm.id }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">题库编号：</span>
              <span class="info-value">{{ postForm.code || '未设置' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 15px;">
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">单选题数量：</span>
              <span class="info-value" style="color: #409EFF; font-weight: bold;">{{ postForm.radioCount || 0 }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">多选题数量：</span>
              <span class="info-value" style="color: #67C23A; font-weight: bold;">{{ postForm.multiCount || 0 }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">判断题数量：</span>
              <span class="info-value" style="color: #E6A23C; font-weight: bold;">{{ postForm.judgeCount || 0 }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 15px;">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">创建时间：</span>
              <span class="info-value">{{ postForm.createTime || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">更新时间：</span>
              <span class="info-value">{{ postForm.updateTime || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 可编辑信息 -->
      <el-card>
        <div slot="header" class="clearfix">
          <span style="font-weight: bold;">编辑题库信息</span>
        </div>

        <el-form-item label="题库名称" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入题库名称" />
        </el-form-item>

        <el-form-item label="题库备注" prop="remark">
          <el-input v-model="postForm.remark" type="textarea" :rows="4" placeholder="请输入题库备注信息" />
        </el-form-item>

      </el-card>

      <div style="margin-top: 20px">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="info" @click="onCancel">返回</el-button>
      </div>

    </el-form>

  </div>
</template>

<script>

import { fetchDetail, saveData } from '@/api/qu/repo'

export default {
  name: 'QuRepoDetail',
  data() {
    return {
      postForm: {

      },
      loading: false,
      rules: {

        title: [
          { required: true, message: '题库名称不能为空！' }
        ]
      }
    }
  },
  created() {
    const id = this.$route.params.id
    if (typeof id !== 'undefined') {
      this.fetchData(id)
    }
  },
  methods: {

    // 添加子项
    handleAdd() {
      this.postForm.answerList.push({ isRight: false, content: '', analysis: '' })
    },

    fetchData(id) {
      const params = { id: id }
      fetchDetail(params).then(response => {
        this.postForm = response.data
      })
    },
    submitForm() {
      console.log(JSON.stringify(this.postForm))

      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '题库保存成功！',
            type: 'success',
            duration: 2000
          })

          this.$router.push({ name: 'ListRepo' })
        })
      })
    },

    onCancel() {
      this.$router.push({ name: 'ListRepo' })
    }

  }
}
</script>

<style scoped>
.info-item {
  padding: 8px 0;
  line-height: 1.8;
}

.info-label {
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.info-value {
  color: #303133;
  font-size: 14px;
  margin-left: 8px;
}

.box-card {
  border-radius: 4px;
}

.box-card .clearfix {
  font-size: 16px;
  color: #303133;
}
</style>
