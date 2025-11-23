import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/register'] // no redirect whitelist

// 防止路由生成/重定向重入导致的无限循环或栈溢出
let addingRoutes = false

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // 获取网站基本信息
  let siteData = store.getters.siteData
  if (!siteData.siteName) {
    siteData = await store.dispatch('settings/getSite')
  }

  // 页面标题
  document.title = getPageTitle(siteData.siteName, to.meta.title)

  // 本地token
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // If roles exist and we already attempted to generate routes, skip fetching again
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      const generated = store.getters.permission_generated
      if (hasRoles && generated) {
        next()
      } else if (hasRoles && !generated) {
        // roles present but no routes generated yet: generate once
        // 防止并发/重入导致重复 addRoutes 或 next 调用
        if (addingRoutes) {
          // 已经在生成路由，等待一会儿再继续（避免重复触发）
          next()
          return
        }

        try {
          addingRoutes = true
          const accessRoutes = await store.dispatch('permission/generateRoutes', store.getters.roles)
          router.addRoutes(accessRoutes)
          addingRoutes = false
          next({ ...to, replace: true })
        } catch (error) {
          addingRoutes = false
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      } else {
        try {
          // 读取用户权限
          const { roles } = await store.dispatch('user/getInfo')
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          // 出错注销
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    // 排除白名单
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // 跳转到登录页面
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
