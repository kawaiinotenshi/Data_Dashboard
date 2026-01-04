import { createRouter, createWebHistory } from 'vue-router'
import LogisticsDashboard from '../views/LogisticsDashboard.vue'
import SupplyChainDashboard from '../views/SupplyChainDashboard.vue'
import AdminDashboard from '../views/AdminDashboard.vue'
import UserManagement from '../components/UserManagement.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: LogisticsDashboard
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: LogisticsDashboard
  },
  {
    path: '/supply-chain',
    name: 'SupplyChain',
    component: SupplyChainDashboard
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminDashboard,
    redirect: '/admin/users',
    children: [
      {
        path: 'users',
        name: 'UserManagement',
        component: UserManagement
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
