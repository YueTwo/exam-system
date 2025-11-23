import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  // Normalize roles to array
  if (!Array.isArray(roles)) {
    if (!roles) roles = []
    else if (typeof roles === 'string') roles = roles.split(',').map(r => r.trim()).filter(r => r)
    else roles = [roles]
  }

  if (route.meta && route.meta.roles) {
    const allowed = Array.isArray(route.meta.roles) ? route.meta.roles : String(route.meta.roles).split(',').map(r => r.trim())
    return roles.some(role => allowed.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      commit('SET_ROUTES', accessedRoutes)
      // mark that we attempted to generate routes (even if empty) to avoid repeated attempts
      if (typeof commit === 'function') {
        commit('SET_GENERATED', true)
      }
      resolve(accessedRoutes)
    })
  }
}

// add generated flag mutation
mutations.SET_GENERATED = (state, v) => {
  state.generated = v
}

// extend initial state with generated flag
if (typeof state.generated === 'undefined') state.generated = false

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
