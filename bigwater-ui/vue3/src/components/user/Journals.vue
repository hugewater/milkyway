<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Current Week Journal -->
        <div class="card p-6 rounded-2xl mb-8">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-xl font-bold text-deep-ocean">Week 45 - Current Issue</h2>
            <span class="px-3 py-1 bg-green-100 text-green-800 text-sm rounded-full">Active</span>
          </div>
          
          <div class="grid md:grid-cols-2 gap-6">
            <div>
              <img src="https://via.placeholder.com/400x300/0077be/ffffff?text=Week+45+Journal" 
                   alt="Week 45 Journal Cover" 
                   class="w-full h-48 object-cover rounded-lg mb-4">
            </div>
            <div>
              <h3 class="text-lg font-semibold text-deep-ocean mb-3">November Special Edition</h3>
              <p class="text-gray-600 mb-4">
                This week's journal features exclusive insights into number patterns, statistical analysis, 
                and expert predictions. Learn about the latest trends in lottery strategies and discover 
                new ways to optimize your number selections.
              </p>
              <div class="flex items-center space-x-4 mb-4">
                <div class="flex items-center text-sm text-gray-500">
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                  </svg>
                  November 8, 2024
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path>
                  </svg>
                  15 min read
                </div>
              </div>
              <button class="btn-primary px-6 py-3 rounded-lg font-medium">
                Read Full Journal
              </button>
            </div>
          </div>
        </div>

        <!-- Journal Archive -->
        <div class="card p-6 rounded-2xl">
          <h2 class="text-xl font-bold text-deep-ocean mb-6">Journal Archive</h2>
          
          <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- Week 44 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/228b22/ffffff?text=Week+44" 
                   alt="Week 44 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 44 - Strategy Focus</h3>
              <p class="text-sm text-gray-600 mb-3">
                Advanced techniques for number selection and pattern recognition.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Oct 31, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>

            <!-- Week 43 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/40e0d0/ffffff?text=Week+43" 
                   alt="Week 43 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 43 - Statistical Analysis</h3>
              <p class="text-sm text-gray-600 mb-3">
                Deep dive into winning patterns and probability calculations.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Oct 24, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>

            <!-- Week 42 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/0077be/ffffff?text=Week+42" 
                   alt="Week 42 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 42 - Winners Circle</h3>
              <p class="text-sm text-gray-600 mb-3">
                Featuring stories from recent winners and their strategies.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Oct 17, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>

            <!-- Week 41 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/228b22/ffffff?text=Week+41" 
                   alt="Week 41 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 41 - Tech Innovation</h3>
              <p class="text-sm text-gray-600 mb-3">
                How AI and machine learning are changing lottery predictions.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Oct 10, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>

            <!-- Week 40 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/40e0d0/ffffff?text=Week+40" 
                   alt="Week 40 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 40 - Global Trends</h3>
              <p class="text-sm text-gray-600 mb-3">
                International lottery trends and cross-market analysis.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Oct 3, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>

            <!-- Week 39 -->
            <div class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
              <img src="https://via.placeholder.com/300x200/0077be/ffffff?text=Week+39" 
                   alt="Week 39 Journal" 
                   class="w-full h-32 object-cover rounded-lg mb-3">
              <h3 class="font-semibold text-deep-ocean mb-2">Week 39 - Psychology of Winning</h3>
              <p class="text-sm text-gray-600 mb-3">
                Understanding the mindset and habits of consistent winners.
              </p>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">Sep 26, 2024</span>
                <button class="text-ocean hover:text-deep-ocean text-sm font-medium">
                  Read Now
                </button>
              </div>
            </div>
          </div>

          <!-- Load more button -->
          <div class="text-center mt-8">
            <button class="border-2 border-ocean text-ocean px-6 py-3 rounded-lg font-medium hover:bg-ocean hover:text-white transition-colors">
              Load More Journals
            </button>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import AppLayout from '../layouts/AppLayout.vue'
// Journal functionality will be implemented here
</script>