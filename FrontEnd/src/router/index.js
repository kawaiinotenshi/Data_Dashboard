import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/LogisticsDashboard.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/LogisticsDashboard.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/supply-chain',
    name: 'SupplyChain',
    component: () => import('../views/SupplyChainDashboard.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/AdminDashboard.vue'),
    redirect: '/admin/users',
    meta: { requiresAuth: true, roles: ['admin'] },
    children: [
      { 
        path: 'users', 
        name: 'UserManagement', 
        component: () => import('../components/UserManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin'] } // 仅管理员可访问
      },
      { 
        path: 'employees', 
        name: 'EmployeeManagement', 
        component: () => import('../components/EmployeeManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin'] } // 仅管理员可访问
      },
      { 
        path: 'departments', 
        name: 'DepartmentManagement', 
        component: () => import('../components/EmployeeManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin'] } // 仅管理员可访问
      },
      // 新增数据管理路由
      { 
        path: 'orders', 
        name: 'OrderManagement', 
        component: () => import('../components/OrderManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin', 'manager', 'staff'] } // 支持管理员、经理和员工角色
      },
      { 
        path: 'warehouses', 
        name: 'WarehouseManagement', 
        component: () => import('../components/WarehouseManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin', 'manager'] } // 支持管理员和经理角色
      },
      { 
        path: 'logistics-tasks', 
        name: 'TransportTaskManagement', 
        component: () => import('../components/TransportTaskManagement.vue'),
        meta: { requiresAuth: true, roles: ['admin', 'manager', 'staff'] } // 支持管理员、经理和员工角色
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userInfo = localStorage.getItem('userInfo')
  const isAuthenticated = userInfo !== null
  
  if (to.meta.requiresAuth === true) {
    if (!isAuthenticated) {
      next('/login')
      return
    }
    
    const user = JSON.parse(userInfo)
    if (to.meta.roles && !to.meta.roles.includes(user.role)) {
      // 没有权限，重定向到首页
      next('/')
      return
    }
  }
  
  // 已登录用户访问登录页，重定向到首页
  if (isAuthenticated && to.path === '/login') {
    next('/')
    return
  }
  
  next()
})

export default router
