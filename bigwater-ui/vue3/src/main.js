import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import './style.css'
import App from './App.vue'
import { initializeAuth, isAuthenticated } from './utils/auth.js'
import { setupRouterGuards } from './utils/router.js'
import AppLayout from './layouts/AppLayout.vue'

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
import AdminTransactions from './components/admin/AdminTransactions.vue'
import MyTransactions from './components/user/MyTransactions.vue'
import AdminAiAgents from './components/admin/AdminAiAgents.vue'
import AdminAiChats from './components/admin/AdminAiChats.vue'
import AdminWalletManager from './components/admin/AdminWalletManager.vue'
import Training from './components/Training.vue'
import TrainingCommissions from './components/TrainingCommissions.vue'

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
  { path: '/my-transactions', component: MyTransactions },
  { path: '/my-account', component: MyAccount },
  { path: '/profile', component: MyAccount }, // Add profile route pointing to MyAccount
  { path: '/settings', component: Settings },
  { path: '/admin/journals', component: () => import('./components/admin/AdminJournals.vue') },
  { path: '/admin/rewards', component: AdminRewards },
  { path: '/admin/drawings', component: AdminDrawings },
  { path: '/admin/payments', component: AdminPayments },
  { path: '/admin/certificates', component: AdminCertificates },
  { path: '/admin/admin-manager', component: () => import('./components/admin/AdminManager.vue') },
  { path: '/admin/members', component: AdminMembers },
  { path: '/admin/transactions', component: AdminTransactions },
  { path: '/admin/wallet-manager', component: AdminWalletManager },
  { path: '/admin/ai/agents', component: AdminAiAgents },
  { path: '/admin/ai/chats', component: AdminAiChats },
  { path: '/ai/chats', component: AdminAiChats }
  ,{ path: '/training', component: Training }
  ,{ path: '/admin/training', component: Training }
  ,{ path: '/training/commissions', component: TrainingCommissions }
  ,{ path: '/admin/training/commissions', component: TrainingCommissions }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Initialize authentication
initializeAuth()

// Add navigation guard for /my-wallets
router.beforeEach((to, from, next) => {
  console.log('Route navigation:', { to, from })
  if (to.path === '/my-wallets') {
    if (!isAuthenticated.value) {
      console.log('Not authenticated, redirecting to login')
      next('/login')
    } else {
      console.log('Authenticated, proceeding to MyWallets')
      next()
    }
  } else {
    next()
  }
})

// Create Vue app instance
console.log('Creating Vue app instance')
const app = createApp(App)

// Register global components
console.log('Registering AppLayout component')
app.component('AppLayout', AppLayout)

// Use router
console.log('Configuring router')
app.use(router)

// Mount app
console.log('Mounting app to #app element')
app.mount('#app')