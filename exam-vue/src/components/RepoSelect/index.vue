<template>

  <el-select
    v-model="currentValue"
    :multiple="multi"
    :remote-method="fetchData"
    filterable
    remote
    reserve-keyword
    clearable
    automatic-dropdown
    placeholder="选择或搜索题库"
    class="filter-item"
    @change="handlerChange"
  >
    <el-option
      v-for="item in dataList"
      :key="item.id"
      :label="item.title"
      :value="item.id"
    />
  </el-select>

</template>

<script>

import { fetchPaging } from '@/api/qu/repo'

export default {
  name: 'RepoSelect',
  props: {
    multi: {
      type: Boolean,
      default: false
    },
    value: String | Array,
    excludes: Array
  },
  data() {
    return {
      // 下拉选项值
      dataList: [],
      currentValue: []
    }
  },

  watch: {
    // 检测查询变化
    value: {
      handler() {
        this.currentValue = this.value
      }
    }
  },
  created() {
    this.currentValue = this.value
    this.fetchData()
  },
  methods: {

    fetchData(q) {
      fetchPaging({ current: 1, size: 500, params: { title: q, excludes: this.excludes }}).then(res => {
          const records = (res && res.data && res.data.records) || [];
          const MAX = 500;
          if (records.length > MAX) {
            try { this.$message && this.$message.warning('选项过多，仅显示前 ' + MAX + ' 条，请输入关键字以过滤'); } catch (e) {}
          }
          this.dataList = records.slice(0, MAX);
        })
    },
    handlerChange(e) {
      const obj = this.dataList.find((item) => {
        return item.id === e
      })

      this.$emit('change', obj)
      this.$emit('input', e)
    }
  }
}
</script>
