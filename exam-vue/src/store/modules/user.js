import { login, reg, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

// 防止并发重复请求 getInfo（会导致 /info 被同时请求多次）
let infoPromise = null

const state = {
  token: getToken(),
  userId: '',
  name: '',
  realName: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_ID: (state, userId) => {
    state.userId = userId
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_REAL_NAME: (state, realName) => {
    state.realName = realName
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  reg({ commit }, userInfo) {
    const { userName, realName, password } = userInfo
    return new Promise((resolve, reject) => {
      reg({ userName: userName.trim(), realName: realName.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    // 去重：如果已有未完成的请求，直接返回同一个 promise
    if (infoPromise) return infoPromise

    infoPromise = new Promise((resolve, reject) => {
      // Do not pass token in query; axios interceptor will add token header
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          reject('校验失败，请重新登录！.')
          infoPromise = null
          return
        }
        const { id, roles, userName, realName, avatar, introduction } = data

        // Make roles robust: backend may return string or other types for roles
        let safeRoles = roles
        if (!safeRoles) safeRoles = []
        else if (!Array.isArray(safeRoles)) {
          if (typeof safeRoles === 'string') {
            safeRoles = safeRoles.split(',').map(r => r.trim()).filter(r => r)
          } else if (typeof safeRoles === 'object') {
            try {
              // If it's an object, try to extract keys or values as fallback
              safeRoles = Object.keys(safeRoles)
            } catch (e) {
              safeRoles = [String(safeRoles)]
            }
          } else {
            safeRoles = [String(safeRoles)]
          }
        }

        // roles must be a non-empty array
        if (!safeRoles || safeRoles.length <= 0) {
          infoPromise = null
          reject('用户角色不能为空！')
          return
        }

        commit('SET_ID', id)
        commit('SET_ROLES', safeRoles)
        commit('SET_REAL_NAME', realName)
        commit('SET_NAME', userName)
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', introduction)
        infoPromise = null
        resolve(data)
      }).catch(error => {
        infoPromise = null
        reject(error)
      })
    })

    return infoPromise
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      // reset visited views and cached views
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
