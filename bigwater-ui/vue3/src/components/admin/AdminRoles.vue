btn-primary">
              {{ editingUser ? 'Update User' : 'Add User' }}
            </button>
            <button type="button" @click="closeModal" class="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { calculateUserLevel, SUBSCRIBER_LEVELS } from '../../utils/userLevels.js'

const showAddModal = ref(false)
const editingUser = ref(null)
const roleFilter = ref('')
const levelFilter = ref('')

const userForm = ref({
  name: '',
  email: '',
  role: 'subscriber'
})

// Mock users data
const users = ref([
  {
    id: 1,
    name: 'John Doe',
    email: 'john@example.com',
    role: 'subscriber',
    referrals: 15,
    earnings: 125.50,
    status: 'active',
    initials: 'JD',
    level: calculateUserLevel(15)
  },
  {
    id: 2,
    name: 'Sarah Johnson',
    email: 'sarah@example.com',
    role: 'admin',
    referrals: 0,
    earnings: 0,
    status: 'active',
    initials: 'SJ',
    level: null
  },
  {
    id: 3,
    name: 'Mike Wilson',
    email: 'mike@example.com',
    role: 'subscriber',
    referrals: 35,
    earnings: 450.75,
    status: 'active',
    initials: 'MW',
    level: calculateUserLevel(35)
  },
  {
    id: 4,
    name: 'Emma Davis',
    email: 'emma@example.com',
    role: 'subscriber',
    referrals: 8,
    earnings: 89.25,
    status: 'suspended',
    initials: 'ED',
    level: calculateUserLevel(8)
  }
])

const subscribers = computed(() => users.value.filter(user => user.role === 'subscriber'))
const admins = computed(() => users.value.filter(user => user.role === 'admin'))
const activeUsersCount = computed(() => users.value.filter(user => user.status === 'active').length)

const filteredUsers = computed(() => {
  let filtered = users.value
  
  if (roleFilter.value) {
    filtered = filtered.filter(user => user.role === roleFilter.value)
  }
  
  if (levelFilter.value) {
    filtered = filtered.filter(user => user.role === 'subscriber' && user.level.level === parseInt(levelFilter.value))
  }
  
  return filtered
})

const getLevelCount = (level) => {
  return subscribers.value.filter(user => user.level.level === level).length
}

const editUser = (user) => {
  editingUser.value = user
  userForm.value = {
    name: user.name,
    email: user.email,
    role: user.role
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingUser.value = null
  userForm.value = {
    name: '',
    email: '',
    role: 'subscriber'
  }
}

const saveUser = () => {
  if (editingUser.value) {
    // Update existing user
    const index = users.value.findIndex(u => u.id === editingUser.value.id)
    if (index !== -1) {
      users.value[index] = {
        ...users.value[index],
        name: userForm.value.name,
        email: userForm.value.email,
        role: userForm.value.role,
        initials: userForm.value.name.split(' ').map(n => n[0]).join('').toUpperCase()
      }
    }
  } else {
    // Add new user
    const newUser = {
      id: Date.now(),
      name: userForm.value.name,
      email: userForm.value.email,
      role: userForm.value.role,
      referrals: 0,
      earnings: 0,
      status: 'active',
      initials: userForm.value.name.split(' ').map(n => n[0]).join('').toUpperCase(),
      level: userForm.value.role === 'subscriber' ? calculateUserLevel(0) : null
    }
    users.value.push(newUser)
  }
  
  closeModal()
}

const toggleUserStatus = (user) => {
  user.status = user.status === 'active' ? 'suspended' : 'active'
}

const deleteUser = (user) => {
  if (confirm(`Are you sure you want to delete ${user.name}?`)) {
    const index = users.value.findIndex(u => u.id === user.id)
    if (index !== -1) {
      users.value.splice(index, 1)
    }
  }
}
</script>
