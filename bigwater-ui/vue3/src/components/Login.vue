<template>
  <div class="min-h-screen wave-pattern flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="card p-8 rounded-2xl">
        <div class="text-center">
          <router-link to="/" class="inline-block">
            <h2 class="text-3xl font-bold text-ocean mb-2">BigWater</h2>
            <p class="text-sm text-forest-green">Digital Weekly Journal</p>
          </router-link>
          <h3 class="mt-6 text-2xl font-bold text-deep-ocean">Sign in to your account</h3>
        </div>

        <form @submit.prevent="handleLogin" class="mt-8 space-y-6">
          <!-- Error Message -->
          <div v-if="errorMessage" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
            {{ errorMessage }}
          </div>
          
          <div class="space-y-4">
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                Email Address
              </label>
              <input
                id="email"
                v-model="loginForm.email"
                type="email"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your email"
              />
            </div>

            <div>
              <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                Password
              </label>
              <input
                id="password"
                v-model="loginForm.password"
                type="password"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your password"
              />
            </div>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input
                id="remember-me"
                v-model="loginForm.rememberMe"
                type="checkbox"
                class="h-4 w-4 text-ocean focus:ring-ocean border-gray-300 rounded"
              />
              <label for="remember-me" class="ml-2 block text-sm text-gray-700">
                Remember me
              </label>
            </div>

            <div class="text-sm">
              <a href="#" class="font-medium text-ocean hover:text-deep-ocean">
                Forgot your password?
              </a>
            </div>
          </div>

          <button
            type="submit"
            :disabled="isLoading"
            class="w-full btn-primary py-3 px-4 rounded-lg font-medium disabled:opacity-50"
          >
            <span v-if="isLoading">Signing in...</span>
            <span v-else>Sign in</span>
          </button>

          <div class="text-center">
            <p class="text-sm text-gray-600">
              Don't have an account?
              <router-link to="/signup" class="font-medium text-ocean hover:text-deep-ocean">
                Sign up here
              </router-link>
            </p>
          </div>
        </form>

        <!-- Admin Login Link -->
        <div class="mt-6 pt-6 border-t border-gray-200">
          <div class="text-center">
            <router-link to="/admin" class="text-sm text-forest-green hover:text-ocean font-medium">
              Admin Login
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../utils/auth.js'

const router = useRouter()
const isLoading = ref(false)
const errorMessage = ref('')

const loginForm = ref({
  email: '',
  password: '',
  rememberMe: false
})

const handleLogin = async () => {
  if (!loginForm.value.email || !loginForm.value.password) {
    errorMessage.value = 'Please enter both email and password'
    return
  }

  isLoading.value = true
  errorMessage.value = ''
  
  try {
    const result = await login(loginForm.value.email, loginForm.value.password)
    
    if (result.success) {
      // Redirect based on role
      if (result.user.role === 'ADMIN' || result.user.role === 'SUPER_ADMIN') {
        router.push('/admin')
      } else {
        router.push('/dashboard')
      }
    } else {
      errorMessage.value = result.message || 'Login failed'
    }
  } catch (error) {
    console.error('Login error:', error)
    errorMessage.value = 'Network error. Please check your connection.'
  } finally {
    isLoading.value = false
  }
}
</script>