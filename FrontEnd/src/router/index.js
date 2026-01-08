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
    component: () => import('../views/LogisticsDashboard.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/LogisticsDashboard.vue')
  },
  {
    path: '/supply-chain',
    name: 'SupplyChain',
    component: () => import('../views/SupplyChainDashboard.vue')
  },
  { path: '/admin', name: 'Admin', component: () => import('../views/AdminDashboard.vue'), redirect: '/admin/users', children: [ { path: 'users', name: 'UserManagement', component: () => import('../components/UserManagement.vue') }, { path: 'employees', name: 'EmployeeManagement', component: () => import('../components/EmployeeManagement.vue') }, { path: 'departments', name: 'DepartmentManagement', component: () => import('../components/EmployeeManagement.vue') } ] }
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

export default router
