import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import './style.css'
import App from './App.vue'
import { initializeAuth } from './utils/auth.js'
import { setupRouterGuards } from './utils/router.js'

// Import components
import Home from './components/Home.vue'
import Login from './components/Login.vue'
import Signup from './components/Signup.vue'
import Dashboard from './components/Dashboard.vue'
import AdminHome from './components/AdminHome.vue'
import Journals from './components/user/Journals.vue'
import Team from './components/user/Team.vue'
import MyRewards from './components/user/MyRewards.vue'
import MyWallets from './components/user/MyWallets.vue'
import MyWinnings from './components/user/MyWinnings.vue'
import WinningNumbers from './components/user/WinningNumbers.vue'
import MyAccount from './components/user/MyAccount.vue'
import Settings from './components/user/Settings.vue'
import AdminJournals from './components/admin/AdminJournals.vue'
import AdminRewards from './components/admin/AdminRewards.vue'
import AdminDrawings from './components/admin/AdminDrawings.vue'
import AdminPayments from './components/admin/AdminPayments.vue'
import AdminCertificates from './components/admin/AdminCertificates.vue'
import Certificates from './components/user/Certificates.vue'
import AdminMembers from './components/admin/AdminMembers.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/signup', component: Signup },
  { path: '/dashboard', component: Dashboard },
  { path: '/admin', component: AdminHome },
  { path: '/journals', component: Journals },
  { path: '/team', component: Team },
  { path: '/my-rewards', component: MyRewards },
  { path: '/my-wallets', component: MyWallets },
  { path: '/my-winnings', component: MyWinnings },
  { path: '/winning-numbers', component: WinningNumbers },
  { path: '/certificates', component: Certificates },
  { path: '/my-account', component: MyAccount },
  { path: '/settings', component: Settings },
  { path: '/admin/journals', component: () => import('./components/admin/AdminJournals.vue') },
  { path: '/admin/rewards', component: AdminRewards },
  { path: '/admin/drawings', component: AdminDrawings },
  { path: '/admin/payments', component: AdminPayments },
  { path: '/admin/certificates', component: AdminCertificates },
  { path: '/admin/admin-manager', component: () => import('./components/admin/AdminManager.vue') },
  { path: '/admin/members', component: AdminMembers }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Initialize authentication
initializeAuth()

// Setup router guards
setupRouterGuards(router)

const app = createApp(App)
app.use(router)
app.mount('#app')