<template>

  <div>

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @multi-actions="handleMultiAction"
    >

      <template #filter-content>

        <el-input v-model="listQuery.params.userName" style="width: 200px" placeholder="搜索登录名" class="filter-item" />
        <el-input v-model="listQuery.params.realName" style="width: 200px" placeholder="搜索姓名" class="filter-item" />

        <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleAdd">
          添加
        </el-button>

      </template>

      <template #data-columns>

        <el-table-column
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          label="用户名"
        >
          <template v-slot="scope">
            <a @click="handleUpdate(scope.row)">{{ scope.row.userName }}</a>
          </template>

        </el-table-column>

        <el-table-column
          align="center"
          label="姓名"
          prop="realName"
        />

        <el-table-column
          align="center"
          label="角色"
          prop="roleIds"
        />

        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
        />

        <el-table-column
          align="center"
          label="状态"
        >

          <template v-slot="scope">
            {{ scope.row.state | stateFilter }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="操作"
          width="140"
        >
          <template v-slot="scope">
            <el-button type="text" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" style="color:#f56c6c" @click="handleDeleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </template>
    </data-table>

    <el-dialog :visible.sync="dialogVisible" title="添加用户" width="500px">

      <el-form :model="formData" label-position="left" label-width="100px">

        <el-form-item label="用户名">
          <el-input v-model="formData.userName" />
        </el-form-item>

        <el-form-item label="姓名">
          <el-input v-model="formData.realName" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="formData.password" placeholder="不修改请留空" type="password" />
        </el-form-item>

        <el-form-item label="院系">
          <depart-tree-select v-model="formData.departId" :options="treeData" :props="defaultProps" />
        </el-form-item>

        <el-form-item label="角色">
          <meet-role v-model="formData.roles" />
        </el-form-item>

        <!--        <el-form-item label="头像" prop="avatar">-->

        <!--          <single-upload-->
        <!--            v-model="formData.avatar"-->
        <!--          />-->
        <!--        </el-form-item>-->

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>

    </el-dialog>

  </div>

</template>

<script>
import DataTable from '@/components/DataTable'
import MeetRole from '@/components/MeetRole'
import { saveData } from '@/api/sys/user/user'
import { deleteData } from '@/api/common'
import DepartTreeSelect from '@/components/DepartTreeSelect'
import { fetchTree } from '@/api/sys/depart/depart'

export default {
  name: 'SysUserList',
  components: { DepartTreeSelect, DataTable, MeetRole },
  filters: {

    // 订单状态
    userState(value) {
      const map = {
        '0': '正常',
        '1': '禁用'
      }
      return map[value]
    }
  },
  data() {
    return {

      treeData: [],
      // 保存上次选择的院系ID，用于新建时预选
      lastDepartId: null,
      defaultProps: {
        value: 'id',
        label: 'facultyName',
        children: 'children'
      },
      dialogVisible: false,

      listQuery: {
        current: 1,
        size: 10,
        params: {
        }
      },

      formData: {
        avatar: ''
      },

      options: {
        // 列表请求URL
        listUrl: '/exam/api/sys/user/paging',
        // 启用禁用
        stateUrl: '/sys/user/state',
        deleteUrl: '/exam/api/sys/user/delete',
        // 批量操作列表
        multiActions: [
          {
            value: 'enable',
            label: '启用'
          }, {
            value: 'disable',
            label: '禁用'
          },
          {
            value: 'delete',
            label: '删除'
          }
        ]
      }
    }
  },

  created() {
    // 加载院系列表
    fetchTree({ category: 'FACULTY' })
      .then(response => {
        // 后端返回统一结构 { code: 0, data: [...] }
        if (response && (response.code === 0 || response.code === '0')) {
          this.treeData = response.data || []
        } else {
          // 可能没有权限或其他错误，打印以便排查
          console.error('fetchTree 返回异常', response)
          this.treeData = response && response.data ? response.data : []
        }
      })
      .catch(err => {
        console.error('fetchTree 请求失败', err)
        this.treeData = []
      })

    // 从 localStorage 读取上次保存的院系（如果有），在新增时会作为默认选中
    try {
      const saved = localStorage.getItem('sys_user_last_departId')
      if (saved) {
        this.lastDepartId = saved
      }
    } catch (e) {
      // ignore
    }
  },

  methods: {

    handleUploadSuccess(response) {
      // 上传图片赋值
      this.formData.avatar = response.data.url
    },

    handleAdd() {
      this.formData = {}
      // 如果有上次选择的院系，预填到新建表单
      if (this.lastDepartId) {
        this.formData.departId = this.lastDepartId
      }
      this.dialogVisible = true
    },

    handleUpdate(row) {
      this.dialogVisible = true
      this.formData = row
      this.formData.roles = row.roleIds.split(',')
      this.formData.password = null

      console.log(JSON.stringify(this.formData))
    },

    departSelected(data) {
      this.formData.departId = data.id
      // 保存上次选择的院系到本地，便于下次新建时预选
      this.lastDepartId = data.id
      try {
        localStorage.setItem('sys_user_last_departId', String(data.id))
      } catch (e) {
        // ignore
      }
    },
    handleSave() {
      saveData(this.formData).then(() => {
        this.$message({
          type: 'success',
          message: '用户修改成功!'
        })
        this.dialogVisible = false
        this.$refs.pagingTable.getList()
      })
    },

    // 删除单个用户
    handleDeleteRow(row) {
      this.$confirm('确实要删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用通用删除接口
        deleteData(this.options.deleteUrl, [row.id]).then(() => {
          this.$message({ type: 'success', message: '删除成功!' })
          this.$refs.pagingTable.getList()
        })
      }).catch(() => {})
    },

    // 批量操作监听
    handleMultiAction(obj) {
      if (obj.opt === 'cancel') {
        this.handleCancelOrder(obj.ids)
      }
    }
  }
}
</script>
