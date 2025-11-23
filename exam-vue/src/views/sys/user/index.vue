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
          label="院系"
        >
          <template v-slot="scope">
            {{ displayDeptName(scope.row) }}
          </template>
        </el-table-column>
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
          <meet-depart v-model="formData.departId" />
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
import MeetDepart from '@/components/MeetDepart'
import { saveData } from '@/api/sys/user/user'
import { deleteData } from '@/api/common'
import { fetchTree } from '@/api/sys/depart/depart'

export default {
  name: 'SysUserList',
  components: { MeetDepart, DataTable, MeetRole },
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
    // 加载院系列表（不按 category 过滤，展示所有院系）
    const loadTree = (params) => {
      return fetchTree(params)
        .then(response => {
          console.debug('fetchTree response:', response)
          if (Array.isArray(response)) {
            return response
          } else if (response && (response.code === 0 || response.code === '0')) {
            return response.data || []
          } else if (response && response.data) {
            return response.data
          }
          return []
        })
        .catch(err => {
          console.error('fetchTree 请求失败', err)
          return []
        })
    }

    // 首先尝试带 category 参数加载院系
    loadTree({ category: 'FACULTY' }).then(list => {
      this.treeData = list || []
      // 如果没数据，尝试不带参数加载全部
      if (!this.treeData || this.treeData.length === 0) {
        console.warn('fetchTree with category returned empty, retrying without category')
        loadTree().then(all => {
          this.treeData = all || []
          if (!this.treeData || this.treeData.length === 0) {
            console.warn('depart tree is empty')
          }
        })
      }
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

    getDeptName(id) {
      if (!id) return ''
      const find = (nodes) => {
        if (!nodes || !nodes.length) return ''
        for (let i = 0; i < nodes.length; i++) {
          const node = nodes[i]
          if (node.id === id || String(node.id) === String(id)) {
            return node.facultyName || node.label || node.name || ''
          }
          if (node.children && node.children.length) {
            const r = find(node.children)
            if (r) return r
          }
        }
        return ''
      }
      return find(this.treeData)
    },

    // 根据 facultyName 查找对应的 id（用于后端以 name 存储 depart_id 的情况下回填）
    findDeptIdByName(name) {
      if (!name) return null
      const find = (nodes) => {
        if (!nodes || !nodes.length) return null
        for (let i = 0; i < nodes.length; i++) {
          const node = nodes[i]
          const nodeName = node.facultyName || node.label || node.name || ''
          if (nodeName === name) return node.id || node.value || null
          if (node.children && node.children.length) {
            const r = find(node.children)
            if (r) return r
          }
        }
        return null
      }
      return find(this.treeData)
    },

    // 安全展示院系名称：支持 row 中 departId（id 字符串）或 depart_id（可能是 id 或 faculty_name）
    displayDeptName(row) {
      if (!row) return ''
      const id = row.departId || row.depart_id
      if (!id) return ''

      // 如果是纯数字或看起来像 id，则按 id 查找名称
      if (/^\d+$/.test(String(id))) {
        const name = this.getDeptName(id)
        return name || ''
      }

      // 否则认为后端已把 faculty_name 存入 depart_id，直接返回
      return id
    },

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

      // normalize depart id field for the form (support snake_case from backend)
      let departId = null
      if (row.departId) {
        departId = row.departId
      } else if (row.depart_id) {
        const raw = String(row.depart_id)
        // 如果后端已经保存的是 id（纯数字），直接使用；否则当作 facultyName，反查 id
        if (/^\d+$/.test(raw)) {
          departId = raw
        } else {
          departId = this.findDeptIdByName(row.depart_id) || null
        }
      }
      this.formData.departId = departId
      this.formData.password = null

      console.log(JSON.stringify(this.formData))
    },

    departSelected(data) {
      // `DepartTreeSelect` 组件通过 `selected` 事件只会回传选中的 id（不是对象）
      const id = data
      this.formData.departId = id
      // 保存上次选择的院系到本地，便于下次新建时预选
      this.lastDepartId = id
      try {
        localStorage.setItem('sys_user_last_departId', String(id))
      } catch (e) {
        // ignore
      }
    },
    handleSave() {
      // 确保发送给后端的字段为 DTO 所期望的 `departId`（部门 id 字符串）
      if (this.formData.departId !== undefined && this.formData.departId !== null && this.formData.departId !== '') {
        this.formData.departId = String(this.formData.departId)
      }

      // 简单校验：roles 与 departId 为必填项（后端也会校验），提前提示以避免 500
      if (!this.formData.roles || this.formData.roles.length === 0) {
        this.$message({ type: 'warning', message: '请至少选择一个角色' })
        return
      }
      if (!this.formData.departId) {
        this.$message({ type: 'warning', message: '请选择院系' })
        return
      }

      // 打印将要发送的 payload，便于在控制台查看导致 500 的原因
      console.debug('Saving user payload:', JSON.parse(JSON.stringify(this.formData)))

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
