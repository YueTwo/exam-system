<template>

  <data-table
    ref="pagingTable"
    :options="options"
    :list-query="listQuery"
  >
    <template #filter-content>

      <el-input v-model="listQuery.params.title" placeholder="搜索题库名称" style="width: 200px;" class="filter-item" />

    </template>

    <template #data-columns>

      <el-table-column
        label="题库ID"
        prop="id"
        align="center"
      />

      <el-table-column
        label="题库名称"
      >

        <template slot-scope="data">

          <router-link :to="{ name: 'UpdateRepo', params:{id: data.row.id}}">
            {{ data.row.title }}
          </router-link>

        </template>

      </el-table-column>

      <el-table-column
        label="单选题数量"
        prop="radioCount"
        align="center"
      />

      <el-table-column
        label="多选题数量"
        prop="multiCount"
        align="center"
      />

      <el-table-column
        label="判断题数量"
        prop="judgeCount"
        align="center"
      />

      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
      />

      <el-table-column
        label="操作"
        align="center"
        width="180"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>

    </template>

  </data-table>

</template>

<script>
import DataTable from '@/components/DataTable'
import { deleteRepo } from '@/api/qu/repo'

export default {
  name: 'QuList',
  components: { DataTable },
  data() {
    return {

      listQuery: {
        current: 1,
        size: 10,
        params: {
          title: ''
        }
      },

      options: {

        // 可批量操作
        multi: true,

        // 批量操作列表
        multiActions: [
          {
            value: 'delete',
            label: '批量删除'
          }
        ],
        // 列表请求URL
        listUrl: '/exam/api/repo/paging',
        // 删除请求URL
        deleteUrl: '/exam/api/repo/delete',
        // 启用禁用
        stateUrl: '/qu/repo/state',
        // 添加数据路由
        addRoute: 'AddRepo'
      }
    }
  },
  methods: {
    // 处理编辑操作
    handleEdit(row) {
      this.$router.push({
        name: 'UpdateRepo',
        params: { id: row.id }
      })
    },

    // 处理删除操作
    handleDelete(row) {
      this.$confirm(`确定要删除题库"${row.title}"吗？删除后将无法恢复。`, '删除确认', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除接口
        deleteRepo({
          ids: [row.id]
        }).then(() => {
          // 删除成功（拦截器已经验证 code === 0）
          this.$message.success('删除成功')
          // 刷新列表
          this.$refs.pagingTable.fetchData()
        }).catch(() => {
          // 删除失败（拦截器已经显示错误消息）
          // 这里可以添加额外的错误处理逻辑
        })
      }).catch(() => {
        // 用户取消删除，不做任何操作
      })
    }
  }
}
</script>
