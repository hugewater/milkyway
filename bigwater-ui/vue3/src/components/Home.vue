<template>
  <div class="min-h-screen wave-pattern">
    <!-- Navigation Header -->
    <nav class="bg-white/90 backdrop-blur-sm shadow-lg sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <h1 class="text-2xl font-bold text-ocean">BigWater</h1>
              <p class="text-xs text-forest-green">Digital Weekly Journal</p>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <router-link to="/login" class="text-deep-ocean hover:text-ocean px-3 py-2 rounded-md text-sm font-medium">
              Login
            </router-link>
            <router-link to="/signup" class="btn-primary px-4 py-2 rounded-lg text-sm font-medium">
              Sign Up
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <!-- Hero Section -->
    <div class="relative overflow-hidden">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div class="text-center">
          <h1 class="text-4xl md:text-6xl font-bold text-deep-ocean mb-6">
            Welcome to BigWater
            <span class="block text-ocean">Digital Weekly Journal</span>
          </h1>
          <p class="text-xl text-gray-600 mb-8 max-w-3xl mx-auto">
            Join our exclusive weekly lottery journal. Subscribe, get your lucky numbers, and win with our unique reward system.
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link to="/signup" class="btn-primary px-8 py-3 rounded-xl text-lg font-semibold">
              Start Your Journey
            </router-link>
            <button @click="scrollToFeatures" class="border-2 border-ocean text-ocean px-8 py-3 rounded-xl text-lg font-semibold hover:bg-ocean hover:text-white transition-all">
              Learn More
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div ref="featuresSection" class="py-20 bg-white/50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-3xl md:text-4xl font-bold text-deep-ocean mb-4">
            Why Choose BigWater?
          </h2>
          <p class="text-xl text-gray-600">
            Experience the future of digital lottery with our unique features
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- Feature 1 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Weekly Drawings</h3>
            <p class="text-gray-600">Get 10 unique lottery number sets every week with Powerball rules. Customize your numbers for better chances.</p>
          </div>

          <!-- Feature 2 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Referral Rewards</h3>
            <p class="text-gray-600">Earn from multi-level marketing. Build your team and get rewarded for every new subscriber you bring.</p>
          </div>

          <!-- Feature 3 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">USDT Payments</h3>
            <p class="text-gray-600">Secure cryptocurrency payments with USDT (TRC). $30/week subscription or $60 for double win chances.</p>
          </div>

          <!-- Feature 4 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Digital Journal</h3>
            <p class="text-gray-600">Access exclusive weekly digital content with insights, tips, and analysis from lottery experts.</p>
          </div>

          <!-- Feature 5 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Dashboard</h3>
            <p class="text-gray-600">Comprehensive dashboard to track your numbers, winnings, team, and rewards all in one place.</p>
          </div>

          <!-- Feature 6 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-gradient rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Secure Platform</h3>
            <p class="text-gray-600">Enterprise-grade security with encrypted data and secure wallet management for all transactions.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- CTA Section -->
    <div class="ocean-gradient py-20">
      <div class="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-white mb-6">
          Ready to Start Winning?
        </h2>
        <p class="text-xl text-white/90 mb-8">
          Join thousands of subscribers who are already playing and winning with BigWater.
        </p>
        <router-link to="/signup" class="bg-white text-ocean px-8 py-4 rounded-xl text-lg font-semibold hover:bg-gray-100 transition-all inline-block">
          Subscribe Now - $30/week
        </router-link>
      </div>
    </div>

    <!-- Footer -->
    <footer class="bg-deep-ocean text-white py-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 class="text-xl font-bold mb-4">BigWater</h3>
            <p class="text-white/80">Digital Weekly Journal for lottery enthusiasts worldwide.</p>
          </div>
          <div>
            <h4 class="font-semibold mb-4">Quick Links</h4>
            <ul class="space-y-2 text-white/80">
              <li><a href="#" class="hover:text-white">How to Play</a></li>
              <li><a href="#" class="hover:text-white">Winning Odds</a></li>
              <li><a href="#" class="hover:text-white">Rules</a></li>
            </ul>
          </div>
          <div>
            <h4 class="font-semibold mb-4">Support</h4>
            <ul class="space-y-2 text-white/80">
              <li><a href="#" class="hover:text-white">Contact Us</a></li>
              <li><a href="#" class="hover:text-white">FAQ</a></li>
              <li><a href="#" class="hover:text-white">Terms of Service</a></li>
            </ul>
          </div>
          <div>
            <h4 class="font-semibold mb-4">Connect</h4>
            <ul class="space-y-2 text-white/80">
              <li><a href="#" class="hover:text-white">Twitter</a></li>
              <li><a href="#" class="hover:text-white">Telegram</a></li>
              <li><a href="#" class="hover:text-white">Discord</a></li>
            </ul>
          </div>
        </div>
        <div class="border-t border-white/20 mt-8 pt-8 text-center text-white/60">
          <p>&copy; 2024 BigWater Digital Weekly Journal. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const featuresSection = ref(null)

const scrollToFeatures = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>