<template>
  <div>

    <div class="title-box">
      <div>用户注册</div>
    </div>

    <el-form ref="postForm" :model="postForm" :rules="loginRules">

      <el-form-item prop="userName">
        <el-input
          v-model="postForm.userName"
          style="width: 100%"
          placeholder="用户名"
          prefix-icon="el-icon-mobile"
        />
      </el-form-item>

      <el-form-item prop="realName">
        <el-input
          v-model="postForm.realName"
          style="width: 100%"
          placeholder="姓名"
          prefix-icon="el-icon-user"
        />
      </el-form-item>

      <el-form-item label="院系" prop="departId">
        <depart-tree-select v-model="postForm.departId" :options="treeData" :props="defaultProps" />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="postForm.password"
          show-password
          style="width: 100%"
          placeholder="密码"
          type="password"
          prefix-icon="el-icon-lock"
        />
      </el-form-item>

      <el-form-item>
        <el-button :loading="loading" type="primary" style="width: 100%" @click.native.prevent="handleReg">注册</el-button>
      </el-form-item>

    </el-form>

    <div style="display: flex; align-items: center; justify-content: flex-end">
      <el-link type="primary" href="/#/login">已有账号</el-link>
    </div>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import DepartTreeSelect from '@/components/DepartTreeSelect'
import { fetchTree } from '@/api/sys/depart/depart'

export default {
  data() {
    return {
      postForm: {
        mobile: '',
        departId: '',
        password: ''
      },
      loginRules: {
        password: [{ required: true, trigger: 'blur', message: '登录密码不能为空！' }],
        userName: [{ required: true, trigger: 'blur', message: '用户名不能为空！' }],
        realName: [{ required: true, trigger: 'blur', message: '姓名不能为空！' }],
        captchaValue: [{ required: true, trigger: 'blur', message: '验证码不能为空' }]
        ,
        departId: [{ required: true, trigger: 'change', message: '请选择院系' }]
      },
      loading: false
    }
  },
  components: { DepartTreeSelect },

  created() {
    this.treeData = []
    this.defaultProps = { value: 'id', label: 'facultyName', children: 'children' }
    this.loadFaculties()
  },
  computed: {
    ...mapGetters([
      'siteData'
    ])
  },

  methods: {

    // 加载院系列表供注册时选择
    loadFaculties() {
      fetchTree({ category: 'FACULTY' })
        .then(res => {
          if (res && (res.code === 0 || res.code === '0')) {
            this.treeData = res.data || []
          } else {
            this.treeData = res && res.data ? res.data : []
          }
        })
        .catch(() => {
          this.treeData = []
        })
    },

    handleReg() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          // include departId in payload (may be empty if user didn't select)
          const payload = Object.assign({}, this.postForm)
          this.$store.dispatch('user/reg', payload)
            .then(() => {
              const redirect = this.$route.query.redirect || '/my/exam'
              this.$router.push({ path: redirect })
              this.loading = false
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    }

  }
}
</script>

