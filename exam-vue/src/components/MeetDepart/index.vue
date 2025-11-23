<template>

  <el-select
    v-model="valueModel"
    :remote-method="fetchList"
    style="width: 100%"
    filterable
    remote
    clearable
    automatic-dropdown
    placeholder="请选择院系"
    @change="handlerChange"
  >
    <el-option
      v-for="item in list"
      :key="item.id"
      :label="item.facultyName || item.label || item.name"
      :value="item.id"
    />
  </el-select>

</template>

<script>
import { fetchTree } from '@/api/sys/depart/depart'

export default {
  name: 'MeetDepart',
  props: {
    value: [String, Number]
  },
  data() {
    return {
      list: [],
      valueModel: this.value
    }
  },
  watch: {
    value(val) {
      this.valueModel = val
    }
  },
  created() {
    this.fetchList()
  },
  methods: {
    fetchList() {
      // Try to fetch faculty category first, fallback to all
      const tryLoad = (params) => fetchTree(params).then(response => {
        // response may be array or wrapped
        if (Array.isArray(response)) return response
        if (response && (response.code === 0 || response.code === '0')) return response.data || []
        if (response && response.data) return response.data
        return []
      }).catch(() => [])

      return tryLoad({ category: 'FACULTY' }).then(list => {
        if (!list || list.length === 0) {
          return tryLoad().then(all => { this.list = all || []; return this.list })
        }
        this.list = list
        return this.list
      })
    },
    handlerChange(e) {
      this.$emit('input', e)
      this.$emit('change', e)
    }
  }
}
</script>

<style scoped>
.el-select { width: 100%; }
</style>
