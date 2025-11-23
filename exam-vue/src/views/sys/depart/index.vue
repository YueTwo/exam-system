<template>
  <div class="app-container">
    <div class="filter-container">

      <el-input v-model="listQuery.params.facultyName" style="width: 200px" placeholder="搜索院系名称" class="filter-item" />

      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="formDialog(0)">
        添加院系
      </el-button>

    </div>

    <el-table
      ref="table"
      :data="tableData.records"
      :default-expand-all.sync="defaultExpand"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :header-cell-style="{'background':'#f2f3f4', 'color':'#555', 'font-weight':'bold', 'line-height':'32px'}"
      row-key="id"
      border
    >
      <el-table-column label="院系名称" prop="facultyName" />
      <el-table-column label="院系编码" prop="deptCode" />

      <el-table-column align="center" label="排序">
        <template v-slot="scope">
          <el-button title="向下排序" size="small" icon="el-icon-sort-down" circle @click="handleSort(scope.row.id, 1)" />
          <el-button title="向上排序" size="small" icon="el-icon-sort-up" circle @click="handleSort(scope.row.id, 0)" />
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作项">
        <template v-slot="scope">
          <el-button size="small" icon="el-icon-plus" circle @click="formDialog(scope.row.id)" />
          <el-button size="small" icon="el-icon-edit" circle @click="formDialog(scope.row.parentId, scope.row.id)" />
          <el-button size="small" icon="el-icon-delete" circle @click="handleDelete(scope.row.id)" />
        </template>
      </el-table-column>

    </el-table>

    <pagination v-show="tableData.total>0" :total="tableData.total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="dialogFormVisible"
      title="维护院系"
      width="30%"
    >
      <el-form ref="postForm" :model="postForm" :rules="rules" label-width="100px" label-position="left">
        <el-form-item label="院系名称" prop="facultyName">
          <el-input v-model="postForm.facultyName" />
        </el-form-item>

        <el-form-item label="院系编码" prop="deptCode">
          <el-input v-model="postForm.deptCode" />
        </el-form-item>

        <el-form-item label="类别" prop="category" style="display:none">
          <el-input v-model="postForm.category" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { pagingTree, fetchDetail, deleteData, saveData, sortData } from '@/api/sys/depart/depart'
import Pagination from '@/components/Pagination'

export default {
  name: 'Depart',
  components: { Pagination },
  data() {
    return {
      defaultExpand: true,
      postForm: {},
      rules: {
        facultyName: [
          { required: true, message: '院系名称不能为空！' }
        ]
      },
      dialogFormVisible: false,
      tableData: {
        total: 0,
        records: []
      },
      listLoading: true,
      listQuery: {
        current: 1,
        size: 10,
        params: {
          // 只加载院系类别的节点
          category: 'FACULTY'
        }
      }
    }
  },
  watch: {

    // 检测查询变化
    listQuery: {
      handler() {
        this.getList()
      },
      deep: true
    }
  },
  created() {
    this.getList()
  },
  methods: {

    /**
       * 编辑窗口
       * @param parentId
       * @param currentId
       */
    formDialog(parentId, currentId) {
      // 清空表单
      this.postForm = {}

      // 覆盖上级分类
      this.postForm.refType = this.listQuery.refType
      this.postForm.parentId = parentId

      // 如果是修改
      if (currentId != null) {
        fetchDetail(currentId).then(response => {
          this.postForm = response.data
        })
      }

      this.dialogFormVisible = true
    },

    getList() {
      this.listLoading = true
      pagingTree(this.listQuery).then(response => {
        this.tableData = response.data
        this.listLoading = false
      })
    },

    handleSort(id, sort) {
      sortData(id, sort).then(() => {
        this.$notify({
          title: '成功',
          message: '排序成功！',
          type: 'success',
          duration: 2000
        })

        this.getList()
      })
    },

    handleSave() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '分类保存成功！',
            type: 'success',
            duration: 2000
          })

          this.dialogFormVisible = false
          this.getList()
        })
      })
    },

    handleDelete(id) {
      // 删除
      this.$confirm('确实要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = []
        ids.push(id)

        deleteData(ids).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })

          this.getList()
        }).catch(err => {
          console.log(err)
        })
      })
    }
  }
}
</script>
