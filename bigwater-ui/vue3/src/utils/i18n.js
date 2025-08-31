// Internationalization utility
import { ref } from 'vue'

// Language files
const en = {
  // Common
  'common.save': 'Save',
  'common.cancel': 'Cancel',
  'common.edit': 'Edit',
  'common.delete': 'Delete',
  'common.view': 'View',
  'common.add': 'Add',
  'common.search': 'Search',
  'common.loading': 'Loading...',
  'common.error': 'Error',
  'common.success': 'Success',
  'common.confirm': 'Confirm',
  'common.yes': 'Yes',
  'common.no': 'No',
  
  // Navigation
  'nav.dashboard': 'Dashboard',
  'nav.journals': 'Journals',
  'nav.members': 'Members',
  'nav.payments': 'Payments',
  'nav.rewards': 'Rewards',
  'nav.drawings': 'Drawings',
  'nav.certificates': 'Certificates',
  'nav.adminManager': 'Admin Manager',
  'nav.myAccount': 'My Account',
  'nav.settings': 'Settings',
  'nav.logout': 'Logout',
  
  // Settings
  'settings.title': 'Settings',
  'settings.language': 'Language Settings',
  'settings.language.preferred': 'Preferred Language',
  'settings.language.description': 'Choose your preferred language for the application interface.',
  'settings.notifications': 'Notification Settings',
  'settings.notifications.email': 'Email Notifications',
  'settings.notifications.email.desc': 'Receive notifications via email',
  'settings.notifications.sms': 'SMS Notifications',
  'settings.notifications.sms.desc': 'Receive notifications via SMS',
  'settings.notifications.push': 'Push Notifications',
  'settings.notifications.push.desc': 'Receive browser push notifications',
  'settings.privacy': 'Privacy Settings',
  'settings.privacy.profile': 'Profile Visibility',
  'settings.privacy.profile.desc': 'Allow other users to see your profile',
  'settings.privacy.activity': 'Activity Status',
  'settings.privacy.activity.desc': 'Show when you\'re online',
  'settings.security': 'Security Settings',
  'settings.security.2fa': 'Two-Factor Authentication',
  'settings.security.2fa.desc': 'Add an extra layer of security to your account',
  'settings.security.loginHistory': 'Login History',
  'settings.security.loginHistory.desc': 'View recent login activity',
  'settings.data': 'Data & Storage',
  'settings.data.autoSave': 'Auto-save Drafts',
  'settings.data.autoSave.desc': 'Automatically save your work as you type',
  'settings.data.clearCache': 'Clear Cache',
  'settings.data.clearCache.desc': 'Clear stored data to free up space',
  
  // Dashboard
  'dashboard.welcome': 'Welcome to BigWater Admin',
  'dashboard.description': 'Manage your platform, members, and content from this central dashboard.',
  'dashboard.stats.totalMembers': 'Total Members',
  'dashboard.stats.activeJournals': 'Active Journals',
  'dashboard.stats.totalRewards': 'Total Rewards',
  'dashboard.stats.pendingDrawings': 'Pending Drawings',
  'dashboard.quickActions': 'Quick Actions',
  'dashboard.manageMembers': 'Manage Members',
  'dashboard.manageMembers.desc': 'View and manage user accounts',
  'dashboard.manageJournals': 'Manage Journals',
  'dashboard.manageJournals.desc': 'Create and edit weekly journals',
  'dashboard.manageRewards': 'Manage Rewards',
  'dashboard.manageRewards.desc': 'Configure reward programs',
  'dashboard.recentActivity': 'Recent Activity',
  'dashboard.activity.newMember': 'New member registration',
  'dashboard.activity.journalPublished': 'Journal published',
  'dashboard.activity.rewardPayout': 'Reward payout processed',
  'dashboard.activity.drawingScheduled': 'Drawing scheduled',
  
  // Members
  'members.title': 'Members Management',
  'members.stats.total': 'Total Members',
  'members.stats.active': 'Active',
  'members.stats.pending': 'Pending',
  'members.stats.suspended': 'Suspended',
  'members.search.placeholder': 'Search members...',
  'members.filter.status': 'All Status',
  'members.filter.status.active': 'Active',
  'members.filter.status.inactive': 'Inactive',
  'members.filter.status.suspended': 'Suspended',
  'members.filter.role': 'All Roles',
  'members.filter.role.subscriber': 'Subscriber',
  'members.filter.role.admin': 'Admin',
  'members.filter.role.superAdmin': 'Super Admin',
  'members.add': 'Add Member',
  'members.table.member': 'Member',
  'members.table.role': 'Role',
  'members.table.status': 'Status',
  'members.table.joinDate': 'Join Date',
  'members.table.lastLogin': 'Last Login',
  'members.table.actions': 'Actions',
  
  // Messages
  'message.languageUpdated': 'Language updated! Please refresh the page to see changes.',
  'message.settingsSaved': 'Settings saved successfully.',
  'message.confirmClearCache': 'Are you sure you want to clear the cache? This will log you out.',
  'message.cacheCleared': 'Cache cleared successfully! Please refresh the page.',
  'message.confirmDelete': 'Are you sure you want to delete this item?',
  'message.deleteSuccess': 'Item deleted successfully.',
  'message.featureComingSoon': 'This feature will be implemented soon.',
  'message.viewingMember': 'Viewing member: {name}',
  'message.editingMember': 'Editing member: {name}',
  'message.memberDeleted': 'Member {name} deleted successfully.',
  'message.addMemberComingSoon': 'Add member functionality will be implemented soon.'
}

const zh = {
  // Common
  'common.save': '保存',
  'common.cancel': '取消',
  'common.edit': '编辑',
  'common.delete': '删除',
  'common.view': '查看',
  'common.add': '添加',
  'common.search': '搜索',
  'common.loading': '加载中...',
  'common.error': '错误',
  'common.success': '成功',
  'common.confirm': '确认',
  'common.yes': '是',
  'common.no': '否',
  
  // Navigation
  'nav.dashboard': '仪表板',
  'nav.journals': '期刊',
  'nav.members': '成员',
  'nav.payments': '支付',
  'nav.rewards': '奖励',
  'nav.drawings': '抽奖',
  'nav.certificates': '证书',
  'nav.adminManager': '管理员',
  'nav.myAccount': '我的账户',
  'nav.settings': '设置',
  'nav.logout': '退出登录',
  
  // Settings
  'settings.title': '设置',
  'settings.language': '语言设置',
  'settings.language.preferred': '首选语言',
  'settings.language.description': '选择您偏好的应用程序界面语言。',
  'settings.notifications': '通知设置',
  'settings.notifications.email': '邮件通知',
  'settings.notifications.email.desc': '通过邮件接收通知',
  'settings.notifications.sms': '短信通知',
  'settings.notifications.sms.desc': '通过短信接收通知',
  'settings.notifications.push': '推送通知',
  'settings.notifications.push.desc': '接收浏览器推送通知',
  'settings.privacy': '隐私设置',
  'settings.privacy.profile': '个人资料可见性',
  'settings.privacy.profile.desc': '允许其他用户查看您的个人资料',
  'settings.privacy.activity': '活动状态',
  'settings.privacy.activity.desc': '显示您在线状态',
  'settings.security': '安全设置',
  'settings.security.2fa': '双重身份验证',
  'settings.security.2fa.desc': '为您的账户添加额外的安全层',
  'settings.security.loginHistory': '登录历史',
  'settings.security.loginHistory.desc': '查看最近的登录活动',
  'settings.data': '数据和存储',
  'settings.data.autoSave': '自动保存草稿',
  'settings.data.autoSave.desc': '在您输入时自动保存您的工作',
  'settings.data.clearCache': '清除缓存',
  'settings.data.clearCache.desc': '清除存储的数据以释放空间',
  
  // Dashboard
  'dashboard.welcome': '欢迎使用 BigWater 管理后台',
  'dashboard.description': '从这个中央仪表板管理您的平台、成员和内容。',
  'dashboard.stats.totalMembers': '总成员数',
  'dashboard.stats.activeJournals': '活跃期刊',
  'dashboard.stats.totalRewards': '总奖励',
  'dashboard.stats.pendingDrawings': '待处理抽奖',
  'dashboard.quickActions': '快速操作',
  'dashboard.manageMembers': '管理成员',
  'dashboard.manageMembers.desc': '查看和管理用户账户',
  'dashboard.manageJournals': '管理期刊',
  'dashboard.manageJournals.desc': '创建和编辑每周期刊',
  'dashboard.manageRewards': '管理奖励',
  'dashboard.manageRewards.desc': '配置奖励计划',
  'dashboard.recentActivity': '最近活动',
  'dashboard.activity.newMember': '新成员注册',
  'dashboard.activity.journalPublished': '期刊已发布',
  'dashboard.activity.rewardPayout': '奖励支付已处理',
  'dashboard.activity.drawingScheduled': '抽奖已安排',
  
  // Members
  'members.title': '成员管理',
  'members.stats.total': '总成员数',
  'members.stats.active': '活跃',
  'members.stats.pending': '待处理',
  'members.stats.suspended': '已暂停',
  'members.search.placeholder': '搜索成员...',
  'members.filter.status': '所有状态',
  'members.filter.status.active': '活跃',
  'members.filter.status.inactive': '非活跃',
  'members.filter.status.suspended': '已暂停',
  'members.filter.role': '所有角色',
  'members.filter.role.subscriber': '订阅者',
  'members.filter.role.admin': '管理员',
  'members.filter.role.superAdmin': '超级管理员',
  'members.add': '添加成员',
  'members.table.member': '成员',
  'members.table.role': '角色',
  'members.table.status': '状态',
  'members.table.joinDate': '加入日期',
  'members.table.lastLogin': '最后登录',
  'members.table.actions': '操作',
  
  // Messages
  'message.languageUpdated': '语言已更新！请刷新页面以查看更改。',
  'message.settingsSaved': '设置已成功保存。',
  'message.confirmClearCache': '您确定要清除缓存吗？这将使您退出登录。',
  'message.cacheCleared': '缓存已成功清除！请刷新页面。',
  'message.confirmDelete': '您确定要删除此项目吗？',
  'message.deleteSuccess': '项目已成功删除。',
  'message.featureComingSoon': '此功能即将推出。',
  'message.viewingMember': '查看成员：{name}',
  'message.editingMember': '编辑成员：{name}',
  'message.memberDeleted': '成员 {name} 已成功删除。',
  'message.addMemberComingSoon': '添加成员功能即将推出。'
}

// Current language
const currentLanguage = ref('en')

// Language files mapping
const languages = {
  en,
  zh
}

// Translation function
export function t(key, params = {}) {
  const lang = languages[currentLanguage.value] || languages.en
  let text = lang[key] || key
  
  // Replace parameters
  Object.keys(params).forEach(param => {
    text = text.replace(`{${param}}`, params[param])
  })
  
  return text
}

// Set language
export function setLanguage(lang) {
  if (languages[lang]) {
    currentLanguage.value = lang
    localStorage.setItem('userLanguage', lang)
    
    // Update document language
    document.documentElement.lang = lang
    
    // Dispatch custom event for language change
    window.dispatchEvent(new CustomEvent('languageChanged', { detail: { language: lang } }))
  }
}

// Get current language
export function getCurrentLanguage() {
  return currentLanguage.value
}

// Initialize language from localStorage
export function initializeLanguage() {
  const savedLanguage = localStorage.getItem('userLanguage')
  if (savedLanguage && languages[savedLanguage]) {
    currentLanguage.value = savedLanguage
    document.documentElement.lang = savedLanguage
  }
}

// Available languages
export const availableLanguages = [
  { code: 'en', name: 'English', nativeName: 'English' },
  { code: 'zh', name: '中文', nativeName: '中文' }
]

// Initialize on module load
initializeLanguage()
