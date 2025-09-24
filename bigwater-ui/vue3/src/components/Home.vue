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
            <router-link to="/login" class="btn-primary px-4 py-2 rounded-lg text-sm font-medium">
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
          <h2 class="text-xl md:text-4xl font-bold text-deep-ocean mb-6">
            Welcome to BigWater
            <span class="block text-ocean">Digital Weekly Journal</span>
          </h2>
          <p class="text-base md:text-lg text-gray-600 mb-8 max-w-3xl mx-auto">
            Enrich yourself with weekly journals!
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link to="/signup" class="btn-primary px-8 py-3 rounded-xl text-lg font-semibold">
              Start Your Journey
            </router-link>
            <button @click="scrollToFeatures" class="btn-primary px-8 py-3 rounded-xl text-lg font-semibold">
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
            Experience the future life with ubiquitious AI!
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- Feature 1 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Health</h3>
            <p class="text-gray-600">Discover how AI-powered diagnostics, personalized medicine, and wearable technology are helping us stay younder and healthier.</p>
          </div>

          <!-- Feature 2 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Family</h3>
            <p class="text-gray-600">Learn how digital tools, smart homes, and virtual connections are reshaping the way families communicate, learn, and support each other in daily life.</p>
          </div>

          <!-- Feature 3 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Social Life</h3>
            <p class="text-gray-600">Explore the changing landscape of human interaction, from social media influence to AI-driven communities, and how they shape culture, values, and connection.</p>
          </div>

          <!-- Feature 4 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">New Tech</h3>
            <p class="text-gray-600">Get a front-row seat to the latest innovations in robotics, biotechnology, quantum computing, and beyond—technologies pushing the boundaries of possibility.</p>
          </div>

          <!-- Feature 5 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">AI</h3>
            <p class="text-gray-600">Understand the broader implications of artificial intelligence, from ethics and governance to creativity and global problem-solving.</p>
          </div>

          <!-- Feature 6 -->
          <div class="card p-8 rounded-2xl text-center">
            <div class="w-16 h-16 ocean-blue rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-deep-ocean mb-4">Jobs</h3>
            <p class="text-gray-600"> Stay ahead with insights into how automation, remote work, and emerging industries are redefining career paths and the skills needed to succeed.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- CTA Section -->
    <div class="ocean-blue py-20">
      <div class="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-white mb-6">
          Ready for a Better Life?
        </h2>
        <p class="text-xl text-white/90 mb-8">
          Join thousands of subscribers who are already playing and winning with BigWater.
        </p>
        <router-link to="/signup" class="px-8 py-4 rounded-xl text-lg font-semibold inline-block border-2 border-white text-white hover:bg-white/10 transition-colors">
          Subscribe Now!
        </router-link>
      </div>
    </div>

    <!-- Footer -->
    <footer class="ocean-blue text-white py-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 class="text-xl font-bold mb-4">BigWater</h3>
            <p class="text-white/80">Digital Weekly Journal for Better Life.</p>
          </div>
          <div>
            <h4 class="font-semibold mb-4">Quick Links</h4>
            <ul class="space-y-2 text-white/80">

              <li><a href="#" class="hover:text-white">AI</a></li>
              <li><a href="#" class="hover:text-white">AI for Anti-aging</a></li>
              <li><a href="#" class="hover:text-white">AI for Family</a></li>
              
            </ul>
          </div>
          <div>
            <h4 class="font-semibold mb-4">Support</h4>
            <ul class="space-y-2 text-white/80">   
              <li><a href="#" class="hover:text-white">AI for Social Life</a></li>
              <li><a href="#" class="hover:text-white">AI for New Tech</a></li>
              <li><a href="#" class="hover:text-white">AI for Jobs</a></li>
          </ul>
          </div>
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