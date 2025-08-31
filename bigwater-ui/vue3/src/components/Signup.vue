<template>
  <div class="min-h-screen wave-pattern flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="card p-8 rounded-2xl">
        <div class="text-center">
          <router-link to="/" class="inline-block">
            <h2 class="text-3xl font-bold text-ocean mb-2">BigWater</h2>
            <p class="text-sm text-forest-green">Digital Weekly Journal</p>
          </router-link>
          <h3 class="mt-6 text-2xl font-bold text-deep-ocean">Create your account</h3>
        </div>

        <form @submit.prevent="handleSignup" class="mt-8 space-y-6">
          <!-- Error Message -->
          <div v-if="errorMessage" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
            {{ errorMessage }}
          </div>
          
          <!-- Success Message -->
          <div v-if="successMessage" class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg">
            {{ successMessage }}
          </div>
          
          <div class="space-y-4">
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                Email Address *
              </label>
              <input
                id="email"
                v-model="signupForm.email"
                type="email"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your email"
              />
            </div>

            <div>
              <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                Password *
              </label>
              <input
                id="password"
                v-model="signupForm.password"
                type="password"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Create a strong password"
              />
            </div>

            <div>
              <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
                Confirm Password *
              </label>
              <input
                id="confirmPassword"
                v-model="signupForm.confirmPassword"
                type="password"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Confirm your password"
              />
            </div>

            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700 mb-2">
                Phone Number
              </label>
              <input
                id="phone"
                v-model="signupForm.phone"
                type="tel"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your phone number"
              />
            </div>

            <div>
              <label for="referralCode" class="block text-sm font-medium text-gray-700 mb-2">
                Upline Referral Code
              </label>
              <input
                id="referralCode"
                v-model="signupForm.referralCode"
                type="text"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter referral code (optional)"
              />
              <p class="text-xs text-gray-500 mt-1">Enter your upline's referral code to join their team</p>
            </div>

            <div>
              <label for="firstName" class="block text-sm font-medium text-gray-700 mb-2">
                First Name *
              </label>
              <input
                id="firstName"
                v-model="signupForm.firstName"
                type="text"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your first name"
              />
            </div>

            <div>
              <label for="lastName" class="block text-sm font-medium text-gray-700 mb-2">
                Last Name *
              </label>
              <input
                id="lastName"
                v-model="signupForm.lastName"
                type="text"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                placeholder="Enter your last name"
              />
            </div>
          </div>

          <div class="bg-blue-50 p-4 rounded-lg">
            <h4 class="font-semibold text-deep-ocean mb-2">Subscription Options:</h4>
            <div class="space-y-2">
              <label class="flex items-center">
                <input
                  v-model="signupForm.subscriptionType"
                  type="radio"
                  value="standard"
                  class="mr-2"
                />
                <span class="text-sm">Standard - $30 USDT/week (10 number sets)</span>
              </label>
              <label class="flex items-center">
                <input
                  v-model="signupForm.subscriptionType"
                  type="radio"
                  value="double"
                  class="mr-2"
                />
                <span class="text-sm">Double Win - $60 USDT/week (20 number sets)</span>
              </label>
            </div>
          </div>

          <div class="flex items-center">
            <input
              id="terms"
              v-model="signupForm.acceptTerms"
              type="checkbox"
              required
              class="h-4 w-4 text-ocean focus:ring-ocean border-gray-300 rounded"
            />
            <label for="terms" class="ml-2 block text-sm text-gray-700">
              I agree to the <a href="#" class="text-ocean hover:text-deep-ocean">Terms of Service</a> and <a href="#" class="text-ocean hover:text-deep-ocean">Privacy Policy</a>
            </label>
          </div>

          <button
            type="submit"
            :disabled="isLoading || !signupForm.acceptTerms"
            class="w-full btn-primary py-3 px-4 rounded-lg font-medium disabled:opacity-50"
          >
            <span v-if="isLoading">Creating Account...</span>
            <span v-else>Create Account</span>
          </button>

          <div class="text-center">
            <p class="text-sm text-gray-600">
              Already have an account?
              <router-link to="/login" class="font-medium text-ocean hover:text-deep-ocean">
                Sign in here
              </router-link>
            </p>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../utils/auth.js'

const router = useRouter()
const isLoading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const signupForm = ref({
  email: '',
  password: '',
  confirmPassword: '',
  referralCode: '',
  firstName: '',
  lastName: '',
  phone: '',
  subscriptionType: 'standard',
  acceptTerms: false
})

const handleSignup = async () => {
  // Validation
  if (signupForm.value.password !== signupForm.value.confirmPassword) {
    errorMessage.value = 'Passwords do not match!'
    return
  }

  if (!signupForm.value.acceptTerms) {
    errorMessage.value = 'Please accept the terms and conditions'
    return
  }

  isLoading.value = true
  errorMessage.value = ''
  successMessage.value = ''
  
  try {
    // Prepare user data for API
    const userData = {
      email: signupForm.value.email,
      password: signupForm.value.password,
      firstName: signupForm.value.firstName,
      lastName: signupForm.value.lastName,
      phone: signupForm.value.phone,
      referralCode: signupForm.value.referralCode || 'COMPANY001'
    }
    
    const result = await register(userData)
    
    if (result.success) {
      successMessage.value = 'Account created successfully!'
      
      // Redirect based on role
      if (result.user.role === 'ADMIN' || result.user.role === 'SUPER_ADMIN') {
        router.push('/admin')
      } else {
        router.push('/dashboard')
      }
    } else {
      errorMessage.value = result.message || 'Failed to create account'
    }
    
  } catch (error) {
    console.error('Signup error:', error)
    errorMessage.value = 'Network error. Please check your connection.'
  } finally {
    isLoading.value = false
  }
}
</script>