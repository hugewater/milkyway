<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Winnings Summary -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-yellow-100">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Winnings</p>
                <p class="text-2xl font-bold text-deep-ocean">$0.00</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-green-100">
                <svg class="w-6 h-6 text-forest-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Biggest Win</p>
                <p class="text-2xl font-bold text-deep-ocean">$0.00</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-100">
                <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Tickets Played</p>
                <p class="text-2xl font-bold text-deep-ocean">45</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-purple-100">
                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Win Rate</p>
                <p class="text-2xl font-bold text-deep-ocean">0%</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Prize Structure -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Prize Structure</h3>
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-gray-200">
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Match</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Prize</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Odds</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Your Wins</th>
                </tr>
              </thead>
              <tbody>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">5 + Powerball</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">Jackpot</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 292,201,338</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">5</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$1,000,000</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 11,688,054</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">4 + Powerball</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$50,000</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 913,129</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">4</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$100</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 36,525</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">3 + Powerball</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$100</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 14,494</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">3</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$7</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 580</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">2 + Powerball</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$7</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 701</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">1 + Powerball</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$4</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 92</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
                <tr>
                  <td class="py-3 px-4 text-sm text-gray-900">Powerball only</td>
                  <td class="py-3 px-4 text-sm font-bold text-green-600">$4</td>
                  <td class="py-3 px-4 text-sm text-gray-600">1 in 38</td>
                  <td class="py-3 px-4 text-sm text-gray-900">0</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Playing History -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Playing History</h3>
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-gray-200">
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Draw Date</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Your Numbers</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Winning Numbers</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Matches</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Prize</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="entry in playingHistory" :key="entry.id" class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">{{ entry.drawDate }}</td>
                  <td class="py-3 px-4">
                    <div class="flex space-x-1">
                      <div v-for="(number, index) in entry.yourNumbers.slice(0, 5)" :key="index" 
                           class="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center text-xs font-bold">
                        {{ number }}
                      </div>
                      <div class="w-8 h-8 bg-red-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
                        {{ entry.yourNumbers[5] }}
                      </div>
                    </div>
                  </td>
                  <td class="py-3 px-4">
                    <div class="flex space-x-1">
                      <div v-for="(number, index) in entry.winningNumbers.slice(0, 5)" :key="index" 
                           class="w-8 h-8 bg-blue-200 rounded-full flex items-center justify-center text-xs font-bold">
                        {{ number }}
                      </div>
                      <div class="w-8 h-8 bg-red-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
                        {{ entry.winningNumbers[5] }}
                      </div>
                    </div>
                  </td>
                  <td class="py-3 px-4 text-sm text-gray-900">{{ entry.matches }}</td>
                  <td class="py-3 px-4 text-sm font-medium text-gray-900">${{ entry.prize }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

const playingHistory = ref([
  {
    id: 1,
    drawDate: '2024-11-06',
    yourNumbers: [12, 25, 33, 41, 52, 18],
    winningNumbers: [8, 27, 34, 4, 19, 10],
    matches: '0',
    prize: '0.00'
  },
  {
    id: 2,
    drawDate: '2024-10-30',
    yourNumbers: [7, 15, 28, 39, 44, 22],
    winningNumbers: [15, 33, 42, 12, 6, 22],
    matches: '1 + PB',
    prize: '0.00'
  },
  {
    id: 3,
    drawDate: '2024-10-23',
    yourNumbers: [3, 21, 35, 47, 59, 11],
    winningNumbers: [21, 35, 51, 14, 33, 18],
    matches: '2',
    prize: '0.00'
  }
])
</script>