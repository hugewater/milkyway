<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Latest Drawing -->
        <div class="card p-8 rounded-2xl mb-8">
          <div class="text-center">
            <h2 class="text-2xl font-bold text-deep-ocean mb-2">Latest Drawing</h2>
            <p class="text-gray-600 mb-6">November 6, 2024 - Draw #2024-45</p>
            
            <div class="flex justify-center space-x-3 mb-6">
              <div v-for="number in latestWinning.slice(0, 5)" :key="number" class="number-ball text-lg">
                {{ number }}
              </div>
              <div class="number-ball powerball text-lg">
                {{ latestWinning[5] }}
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
              <div class="text-center">
                <p class="text-sm text-gray-600">Jackpot</p>
                <p class="text-xl font-bold text-green-600">$425,000,000</p>
              </div>
              <div class="text-center">
                <p class="text-sm text-gray-600">Winners</p>
                <p class="text-xl font-bold text-deep-ocean">3</p>
              </div>
              <div class="text-center">
                <p class="text-sm text-gray-600">Next Jackpot</p>
                <p class="text-xl font-bold text-ocean">$20,000,000</p>
              </div>
            </div>

            <div class="bg-blue-50 p-4 rounded-lg">
              <p class="text-sm text-blue-800">
                <strong>Congratulations to our winners!</strong> Check your numbers to see if you won any prizes.
              </p>
            </div>
          </div>
        </div>

        <!-- Your Numbers vs Winning Numbers -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Your Numbers for Latest Draw</h3>
          <div class="space-y-4">
            <div v-for="(set, index) in yourLatestNumbers" :key="index" class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
              <div class="flex items-center space-x-4">
                <span class="text-sm text-gray-600 w-12">Set {{ index + 1 }}:</span>
                <div class="flex space-x-1">
                  <div v-for="(number, numIndex) in set.slice(0, 5)" :key="numIndex" 
                       :class="`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold ${
                         latestWinning.slice(0, 5).includes(number) ? 'bg-green-500 text-white' : 'bg-gray-200'
                       }`">
                    {{ number }}
                  </div>
                  <div :class="`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold ${
                    latestWinning[5] === set[5] ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
                  }`">
                    {{ set[5] }}
                  </div>
                </div>
              </div>
              <div class="text-right">
                <span :class="`px-3 py-1 rounded-full text-sm ${getMatchResult(set).color}`">
                  {{ getMatchResult(set).text }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Historical Drawings -->
        <div class="card p-6 rounded-2xl mb-8">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-bold text-deep-ocean">Historical Drawings</h3>
            <select class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="10">Last 10 drawings</option>
              <option value="25">Last 25 drawings</option>  
              <option value="50">Last 50 drawings</option>
            </select>
          </div>

          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-gray-200">
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Date</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Draw #</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Winning Numbers</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Jackpot</th>
                  <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Winners</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="draw in historicalDrawings" :key="draw.id" class="border-b border-gray-100">
                  <td class="py-3 px-4 text-sm text-gray-900">{{ draw.date }}</td>
                  <td class="py-3 px-4 text-sm text-gray-900">{{ draw.drawNumber }}</td>
                  <td class="py-3 px-4">
                    <div class="flex space-x-1">
                      <div v-for="(number, index) in draw.numbers.slice(0, 5)" :key="index" 
                           class="w-7 h-7 bg-gray-200 rounded-full flex items-center justify-center text-xs font-bold">
                        {{ number }}
                      </div>
                      <div class="w-7 h-7 bg-red-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
                        {{ draw.numbers[5] }}
                      </div>
                    </div>
                  </td>
                  <td class="py-3 px-4 text-sm font-medium text-green-600">${{ draw.jackpot }}</td>
                  <td class="py-3 px-4 text-sm text-gray-900">{{ draw.winners }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- How to Play -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">How to Play & Win</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <h4 class="font-semibold text-deep-ocean mb-4">How to Play</h4>
              <ul class="space-y-2 text-sm text-gray-600">
                <li class="flex items-start space-x-2">
                  <span class="w-5 h-5 bg-ocean rounded-full flex items-center justify-center text-white text-xs mt-0.5">1</span>
                  <span>Choose 5 different numbers from 1 to 69</span>
                </li>
                <li class="flex items-start space-x-2">
                  <span class="w-5 h-5 bg-ocean rounded-full flex items-center justify-center text-white text-xs mt-0.5">2</span>
                  <span>Choose 1 Powerball number from 1 to 26</span>
                </li>
                <li class="flex items-start space-x-2">
                  <span class="w-5 h-5 bg-ocean rounded-full flex items-center justify-center text-white text-xs mt-0.5">3</span>
                  <span>You get 10 number sets with your subscription</span>
                </li>
                <li class="flex items-start space-x-2">
                  <span class="w-5 h-5 bg-ocean rounded-full flex items-center justify-center text-white text-xs mt-0.5">4</span>
                  <span>Customize any numbers you want to change</span>
                </li>
                <li class="flex items-start space-x-2">
                  <span class="w-5 h-5 bg-ocean rounded-full flex items-center justify-center text-white text-xs mt-0.5">5</span>
                  <span>Wait for the weekly drawing results</span>
                </li>
              </ul>
            </div>
            <div>
              <h4 class="font-semibold text-deep-ocean mb-4">Winning Odds</h4>
              <div class="space-y-2 text-sm">
                <div class="flex justify-between">
                  <span class="text-gray-600">Match 5 + Powerball:</span>
                  <span class="font-medium">1 in 292,201,338</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Match 5:</span>
                  <span class="font-medium">1 in 11,688,054</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Match 4 + Powerball:</span>
                  <span class="font-medium">1 in 913,129</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Match 4:</span>
                  <span class="font-medium">1 in 36,525</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Match 3 + Powerball:</span>
                  <span class="font-medium">1 in 14,494</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Overall odds:</span>
                  <span class="font-medium text-green-600">1 in 24.9</span>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

const latestWinning = ref([8, 27, 34, 4, 19, 10])

const yourLatestNumbers = ref([
  [12, 25, 33, 41, 52, 18],
  [7, 15, 28, 39, 44, 22],
  [3, 21, 35, 47, 59, 11],
  [9, 16, 23, 38, 45, 14],
  [1, 11, 29, 42, 58, 25]
])

const historicalDrawings = ref([
  { id: 1, date: '2024-11-06', drawNumber: '2024-45', numbers: [8, 27, 34, 4, 19, 10], jackpot: '425,000,000', winners: 3 },
  { id: 2, date: '2024-10-30', drawNumber: '2024-44', numbers: [15, 33, 42, 12, 6, 22], jackpot: '400,000,000', winners: 0 },
  { id: 3, date: '2024-10-23', drawNumber: '2024-43', numbers: [21, 35, 51, 14, 33, 18], jackpot: '380,000,000', winners: 1 },
  { id: 4, date: '2024-10-16', drawNumber: '2024-42', numbers: [5, 28, 45, 17, 62, 9], jackpot: '360,000,000', winners: 0 },
  { id: 5, date: '2024-10-09', drawNumber: '2024-41', numbers: [11, 24, 37, 49, 55, 16], jackpot: '340,000,000', winners: 2 }
])

const getMatchResult = (yourNumbers) => {
  const whiteMatches = yourNumbers.slice(0, 5).filter(num => latestWinning.value.slice(0, 5).includes(num)).length
  const powerballMatch = yourNumbers[5] === latestWinning.value[5]
  
  if (whiteMatches === 5 && powerballMatch) {
    return { text: 'JACKPOT!', color: 'bg-yellow-100 text-yellow-800' }
  } else if (whiteMatches === 5) {
    return { text: '$1,000,000', color: 'bg-green-100 text-green-800' }
  } else if (whiteMatches === 4 && powerballMatch) {
    return { text: '$50,000', color: 'bg-green-100 text-green-800' }
  } else if (whiteMatches === 4) {
    return { text: '$100', color: 'bg-green-100 text-green-800' }
  } else if (whiteMatches === 3 && powerballMatch) {
    return { text: '$100', color: 'bg-green-100 text-green-800' }
  } else if (whiteMatches === 3) {
    return { text: '$7', color: 'bg-blue-100 text-blue-800' }
  } else if (whiteMatches === 2 && powerballMatch) {
    return { text: '$7', color: 'bg-blue-100 text-blue-800' }
  } else if (whiteMatches === 1 && powerballMatch) {
    return { text: '$4', color: 'bg-blue-100 text-blue-800' }
  } else if (powerballMatch) {
    return { text: '$4', color: 'bg-blue-100 text-blue-800' }
  } else {
    return { text: 'No Win', color: 'bg-gray-100 text-gray-800' }
  }
}
</script>