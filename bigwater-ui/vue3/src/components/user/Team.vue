<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Team Overview -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-100">
                <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Direct Referrals</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ loading ? '...' : level1Count }}</p>
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
                <p class="text-sm text-gray-600">Total Network</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ loading ? '...' : totalNetworkCount }}</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-yellow-100">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Network Value</p>
                <p class="text-2xl font-bold text-deep-ocean">${{ loading ? '...' : networkValue }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Referral Link -->
        <div class="card p-6 rounded-2xl mb-8">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-4 flex-wrap">
              <div class="flex items-center space-x-3">
                <span class="text-2xl">{{ userLevelTeam.icon }}</span>
                <div>
                  <p class="font-semibold text-deep-ocean">{{ userLevelTeam.name }}</p>
                  <p class="text-xs text-gray-600">{{ userLevelTeam.stars }} Star Level</p>
                </div>
              </div>
              <div class="text-sm text-gray-600 flex items-center space-x-3">
                <span>Total: <span class="font-semibold text-deep-ocean">{{ totalNetworkCount }}</span></span>
                <span>•</span>
                <span>L1: <span class="font-semibold text-deep-ocean">{{ level1Count }}</span></span>
                <span>L2: <span class="font-semibold text-deep-ocean">{{ level2Count }}</span></span>
                <span>L3: <span class="font-semibold text-deep-ocean">{{ level3Count }}</span></span>
              </div>
            </div>
            <h3 class="text-lg font-bold text-deep-ocean">My Referral Link</h3>
          </div>
          <div class="flex items-center space-x-3">
            <input
              type="text"
              :value="referralLink"
              readonly
              class="flex-1 px-4 py-3 border border-gray-300 rounded-lg bg-gray-50"
            />
            <button
              @click="copyReferralLink"
              class="btn-primary px-6 py-3 rounded-lg font-medium"
            >
              Copy Link
            </button>
          </div>
          <p class="text-sm text-gray-600 mt-2">
            Share this link to earn commissions from new subscribers
          </p>
        </div>

        <!-- Team Structure -->
        <div class="card p-6 rounded-2xl mb-8">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-bold text-deep-ocean">Team Structure</h3>
            <div class="flex items-center space-x-2">
              <button
                @click="openAddMyDownline"
                class="btn-primary px-4 py-2 rounded-lg font-medium"
              >
                Add My Downline
              </button>
              <button
                @click="showGraphModal = true"
                class="btn-primary px-4 py-2 rounded-lg font-medium flex items-center space-x-2"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                </svg>
                <span>Graph</span>
              </button>
            </div>
          </div>
          
          <!-- Level 1 - Direct Referrals -->
          <div class="mb-6">
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-ocean text-white rounded-full flex items-center justify-center text-sm mr-2">1</span>
              Level 1 - Direct Referrals ({{ level1Count }} members)
            </h4>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Referral Code</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr v-for="member in level1Members" :key="member.id" class="hover:bg-gray-50 cursor-pointer" 
                      :title="`Title: ${member.userLevel} | Referral Code: ${member.referralCode || 'N/A'}`">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="flex items-center space-x-3">
                        <div class="w-10 h-10 bg-ocean rounded-full flex items-center justify-center text-white font-medium">
                          {{ member.initials }}
                        </div>
                        <div>
                          <p class="font-medium text-gray-900">{{ member.name }}</p>
                          <p v-if="member.referralCode" class="text-sm text-ocean cursor-pointer hover:text-deep-ocean hover:underline font-mono"
                             @click="addDirectDownline(member)"
                             :title="`Click to add direct downline under ${member.name}`">
                            {{ member.referralCode }}
                          </p>
                          <p v-else class="text-sm text-gray-400 font-mono">N/A</p>
                        </div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                        {{ member.userLevel }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium" 
                            :class="member.status === 'active' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                        {{ member.status }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ member.joinDate }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-mono">
                      {{ member.referralCode || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">
                      ${{ member.contribution }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Level 2 - Indirect Referrals -->
          <div class="mb-6">
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-forest-green text-white rounded-full flex items-center justify-center text-sm mr-2">2</span>
              Level 2 - Indirect Referrals ({{ level2Count }} members)
            </h4>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Referral Code</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr v-for="member in level2Members" :key="member.id" class="hover:bg-gray-50 cursor-pointer" 
                      :title="`Title: ${member.userLevel} | Referral Code: ${member.referralCode || 'N/A'}`">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="flex items-center space-x-3">
                        <div class="w-10 h-10 bg-forest-green rounded-full flex items-center justify-center text-white font-medium">
                          {{ member.initials }}
                        </div>
                        <div>
                          <p class="font-medium text-gray-900">{{ member.name }}</p>
                          <p v-if="member.referralCode" class="text-sm text-ocean cursor-pointer hover:text-deep-ocean hover:underline font-mono"
                             @click="addDirectDownline(member)"
                             :title="`Click to add direct downline under ${member.name}`">
                            {{ member.referralCode }}
                          </p>
                          <p v-else class="text-sm text-gray-400 font-mono">N/A</p>
                        </div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                        {{ member.userLevel }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium" 
                            :class="member.status === 'active' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                        {{ member.status }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ member.joinDate }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-mono">
                      {{ member.referralCode || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">
                      ${{ member.contribution }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Level 3 - Third Level -->
          <div>
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-yellow-500 text-white rounded-full flex items-center justify-center text-sm mr-2">3</span>
              Level 3 - Third Level ({{ level3Count }} members)
            </h4>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Referral Code</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr v-for="member in level3Members" :key="member.id" class="hover:bg-gray-50 cursor-pointer" 
                      :title="`Title: ${member.userLevel} | Referral Code: ${member.referralCode || 'N/A'}`">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="flex items-center space-x-3">
                        <div class="w-10 h-10 bg-yellow-500 rounded-full flex items-center justify-center text-white font-medium">
                          {{ member.initials }}
                        </div>
                        <div>
                          <p class="font-medium text-gray-900">{{ member.name }}</p>
                          <p v-if="member.referralCode" class="text-sm text-ocean cursor-pointer hover:text-deep-ocean hover:underline font-mono"
                             @click="addDirectDownline(member)"
                             :title="`Click to add direct downline under ${member.name}`">
                            {{ member.referralCode }}
                          </p>
                          <p v-else class="text-sm text-gray-400 font-mono">N/A</p>
                        </div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                        {{ member.userLevel }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium" 
                            :class="member.status === 'active' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                        {{ member.status }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ member.joinDate }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-mono">
                      {{ member.referralCode || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">
                      ${{ member.contribution }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- Team Performance -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Team Performance</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h4 class="font-semibold text-gray-900 mb-4">Monthly Growth</h4>
              <div class="space-y-3">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">November 2024</span>
                  <span class="text-sm font-medium text-green-600">+5 members</span>
                </div>
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">October 2024</span>
                  <span class="text-sm font-medium text-green-600">+8 members</span>
                </div>
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">September 2024</span>
                  <span class="text-sm font-medium text-green-600">+12 members</span>
                </div>
              </div>
            </div>
            <div>
              <h4 class="font-semibold text-gray-900 mb-4">Top Performers</h4>
              <div class="space-y-3">
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-gold rounded-full flex items-center justify-center text-white text-xs">1</div>
                    <span class="text-sm text-gray-900">Sarah Johnson</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$450</span>
                </div>
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-gray-400 rounded-full flex items-center justify-center text-white text-xs">2</div>
                    <span class="text-sm text-gray-900">Mike Chen</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$320</span>
                </div>
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-yellow-600 rounded-full flex items-center justify-center text-white text-xs">3</div>
                    <span class="text-sm text-gray-900">Lisa Brown</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$280</span>
                </div>
              </div>
            </div>
          </div>
                </div>

        <!-- ECharts Network Graph Modal -->
        <div
          v-if="showGraphModal"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
          @click="closeModal"
        >
          <UserNetworkGraph
            :selectedUser="currentUserForGraph"
            :downlines="allDownlineMembers"
            :loading="false"
            @close="showGraphModal = false"
          />
        </div>

        <!-- Add Direct Downline Modal -->
        <div
          v-if="showAddDownlineModal"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
          @click="closeAddDownlineModal"
        >
          <div
            class="bg-white rounded-lg p-6 max-w-md w-full mx-4"
            @click.stop
          >
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-bold text-deep-ocean">Add Direct Downline</h3>
              <button
                @click="closeAddDownlineModal"
                class="text-gray-400 hover:text-gray-600"
              >
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form class="mb-4" @submit.prevent="submitAddDownline" autocomplete="off">
              <!-- Anti-autofill dummies -->
              <input type="text" name="fake-username" autocomplete="username" style="display:none" />
              <input type="password" name="fake-password" autocomplete="current-password" style="display:none" />

              <p class="text-sm text-gray-600 mb-2">
                Adding direct downline under: <span class="font-medium text-deep-ocean">{{ selectedMember?.name }}</span>
              </p>
              <p class="text-sm text-gray-500">
                Referral Code: <span class="font-mono text-ocean">{{ selectedMember?.referralCode }}</span>
              </p>
            
            <div class="space-y-4 mt-3">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Email Address *</label>
                <input
                  v-model="newDownlineForm.email"
                  type="email"
                  placeholder="Enter email address"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                  autocomplete="email"
                  required
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">First Name</label>
                <input
                  v-model="newDownlineForm.firstName"
                  type="text"
                  placeholder="Enter first name (optional)"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                  autocomplete="given-name"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Last Name</label>
                <input
                  v-model="newDownlineForm.lastName"
                  type="text"
                  placeholder="Enter last name (optional)"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                  autocomplete="family-name"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Phone Number</label>
                <input
                  v-model="newDownlineForm.phone"
                  type="tel"
                  placeholder="Enter phone number (optional)"
                  name="new-phone"
                  autocomplete="tel"
                  inputmode="tel"
                  autocapitalize="off"
                  autocorrect="off"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Password</label>
                <input
                  v-model="newDownlineForm.password"
                  type="password"
                  placeholder="Enter password (optional)"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                  autocomplete="new-password"
                />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Member Level</label>
                <select
                  v-model="newDownlineForm.level"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-ocean focus:border-ocean"
                >
                  <option value="CUSTOMER">Customer</option>
                  <option value="CHIEF">Chief</option>
                  <option value="MAYOR">Mayor</option>
                  <option value="GOVERNOR">Governor</option>
                  <option value="MINISTER">Minister</option>
                  <option value="PRESIDENT">President</option>
                </select>
              </div>
              
              <div class="grid grid-cols-2 gap-3 mt-2">
                <button
                  type="button"
                  @click="closeAddDownlineModal"
                  class="w-full px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50"
                  :disabled="addingDownline"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  class="w-full px-4 py-2 btn-primary rounded-md disabled:opacity-50"
                  :disabled="addingDownline"
                >
                  <span v-if="addingDownline">Adding...</span>
                  <span v-else>Add Downline</span>
                </button>
              </div>
            </div>
            </form>

            <div class="mt-4 p-3 bg-blue-50 rounded-md">
              <p class="text-xs text-blue-800">
                <strong>Note:</strong> An invitation email will be sent to the specified email address. 
                The new member will be automatically placed under {{ selectedMember?.name }} in your team structure.
              </p>
            </div>
          </div>
        </div>
      </div>
    </AppLayout>
  </template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import UserNetworkGraph from './UserNetworkGraph.vue'
import { currentUser } from '../../utils/auth.js'
import apiService from '../../utils/api.js'
import { calculateUserLevel, SUBSCRIBER_LEVELS } from '../../utils/userLevels.js'

const referralLink = computed(() => {
  const referralCode = currentUser.value?.referralCode || 'BW12345678'
  return `https://6768688.com/signup?ref=${referralCode}`
})

const showGraphModal = ref(false)
const showAddDownlineModal = ref(false)
const selectedMember = ref(null)
const loading = ref(false)
const addingDownline = ref(false)
const newDownlineForm = ref({
  email: '',
  firstName: '',
  lastName: '',
  phone: '',
  password: '',
  level: 'CUSTOMER'
})

// Real team data from API
const level1Members = ref([])
const level2Members = ref([])
const level3Members = ref([])
const networkData = ref(null)

// Helper function to generate initials
const getInitials = (firstName, lastName) => {
  if (firstName && lastName) {
    return `${firstName[0]}${lastName[0]}`.toUpperCase()
  }
  return 'U'
}

// Helper function to format date
const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleDateString()
}

// Helper function to calculate contribution (placeholder)
const calculateContribution = (user) => {
  // This could be based on actual business logic
  return Math.floor(Math.random() * 200) + 50
}

// Derive display title; default new members to Customer until >=24 weeks
const getDisplayUserLevel = (member) => {
  const weeks = member?.weeksSubscribed ?? member?.subscriptionWeeks ?? 0
  if (weeks < 24) return 'Customer'
  return member?.level || 'Chief'
}

// Load team network data from API
const loadNetworkData = async () => {
  console.log('=== LOAD NETWORK DATA START ===')
  console.log('Current user:', currentUser.value)
  console.log('Current user ID:', currentUser.value?.id)
  
  if (!currentUser.value?.id) {
    console.error('No current user ID found!')
    return
  }
  
  loading.value = true
  try {
    console.log('Loading network data for user:', currentUser.value.id)
    const response = await apiService.getUserNetwork(currentUser.value.id)
    console.log('API response:', response)
    
    if (response.success && response.data) {
      networkData.value = response.data
      console.log('Network data received:', response.data)
      
      // Process downlines and organize by levels
      const downlines = response.data.downlines || []
      
      // Clear existing data
      level1Members.value = []
      level2Members.value = []
      level3Members.value = []
      
      // Group downlines by their relationship level
      const processedMembers = new Map()
      
      // First, identify direct referrals (Level 1)
      const directReferrals = downlines.filter(member => 
        member.referredByCode === currentUser.value.referralCode
      )
      
      directReferrals.forEach(member => {
        const processedMember = {
          id: member.id,
          name: `${member.firstName || ''} ${member.lastName || ''}`.trim() || member.email,
          email: member.email,
          initials: getInitials(member.firstName, member.lastName),
          level: 1,
          userLevel: getDisplayUserLevel(member),
          status: member.status?.toLowerCase() || 'active',
          joinDate: formatDate(member.joinDate),
          contribution: calculateContribution(member),
          referralCode: member.referralCode,
          referredByCode: member.referredByCode
        }
        level1Members.value.push(processedMember)
        processedMembers.set(member.id, { ...processedMember, referralCode: member.referralCode })
      })
      
      // Find Level 2 members (referred by Level 1 members)
      const level1ReferralCodes = directReferrals.map(m => m.referralCode)
      const level2Referrals = downlines.filter(member => 
        level1ReferralCodes.includes(member.referredByCode) && 
        !directReferrals.some(dr => dr.id === member.id)
      )
      
      level2Referrals.forEach(member => {
        const processedMember = {
          id: member.id,
          name: `${member.firstName || ''} ${member.lastName || ''}`.trim() || member.email,
          email: member.email,
          initials: getInitials(member.firstName, member.lastName),
          level: 2,
          userLevel: getDisplayUserLevel(member),
          status: member.status?.toLowerCase() || 'active',
          joinDate: formatDate(member.joinDate),
          contribution: calculateContribution(member),
          referralCode: member.referralCode,
          referredByCode: member.referredByCode
        }
        level2Members.value.push(processedMember)
        processedMembers.set(member.id, { ...processedMember, referralCode: member.referralCode })
      })
      
      // Find Level 3 members (referred by Level 2 members)
      const level2ReferralCodes = level2Referrals.map(m => m.referralCode)
      const level3Referrals = downlines.filter(member => 
        level2ReferralCodes.includes(member.referredByCode) && 
        !directReferrals.some(dr => dr.id === member.id) &&
        !level2Referrals.some(l2 => l2.id === member.id)
      )
      
      level3Referrals.forEach(member => {
        const processedMember = {
          id: member.id,
          name: `${member.firstName || ''} ${member.lastName || ''}`.trim() || member.email,
          email: member.email,
          initials: getInitials(member.firstName, member.lastName),
          level: 3,
          userLevel: getDisplayUserLevel(member),
          status: member.status?.toLowerCase() || 'active',
          joinDate: formatDate(member.joinDate),
          contribution: calculateContribution(member),
          referralCode: member.referralCode,
          referredByCode: member.referredByCode
        }
        level3Members.value.push(processedMember)
        processedMembers.set(member.id, { ...processedMember, referralCode: member.referralCode })
      })
      
      console.log('Processed network data:')
      console.log('Level 1:', level1Members.value.length, 'members')
      console.log('Level 2:', level2Members.value.length, 'members')
      console.log('Level 3:', level3Members.value.length, 'members')
      
    } else {
      console.error('Failed to load network data:', response)
    }
  } catch (error) {
    console.error('Error loading network data:', error)
  } finally {
    loading.value = false
  }
}

const copyReferralLink = () => {
  navigator.clipboard.writeText(referralLink.value)
  alert('Referral link copied to clipboard!')
}

const addDirectDownline = (member) => {
  selectedMember.value = member
  // Reset form
  newDownlineForm.value = {
    email: '',
    firstName: '',
    lastName: '',
    phone: '',
    password: ''
  }
  showAddDownlineModal.value = true
}

// Open add modal to add a direct downline under current user
const openAddMyDownline = () => {
  selectedMember.value = {
    id: currentUser.value?.id,
    name: currentUser.value?.firstName && currentUser.value?.lastName
      ? `${currentUser.value.firstName} ${currentUser.value.lastName}`
      : currentUser.value?.email || 'You',
    referralCode: currentUser.value?.referralCode
  }
  // Pre-populate referral code by selecting current user as parent
  // Reset inputs to ensure no autofill leftovers
  newDownlineForm.value.email = ''
  newDownlineForm.value.firstName = ''
  newDownlineForm.value.lastName = ''
  newDownlineForm.value.phone = ''
  newDownlineForm.value.password = ''
  showAddDownlineModal.value = true
}

const submitAddDownline = async () => {
  if (!newDownlineForm.value.email) {
    alert('Email is required')
    return
  }
  
  // Password is optional; only include when provided
  
  addingDownline.value = true
  try {
    // Include referralCode directly from the selected member to boost performance
    const downlineData = {
      email: newDownlineForm.value.email,
      firstName: newDownlineForm.value.firstName || '',
      lastName: newDownlineForm.value.lastName || '',
      phone: newDownlineForm.value.phone || '',
      referralCode: selectedMember.value.referralCode,
      level: newDownlineForm.value.level || 'CUSTOMER'
    }
    if (newDownlineForm.value.password && newDownlineForm.value.password.trim() !== '') {
      downlineData.password = newDownlineForm.value.password
    }
    
    const response = await apiService.addDownline(selectedMember.value.id, downlineData)
    
    if (response.success) {
      alert('Downline added successfully!')
      closeAddDownlineModal()
      // Reload network data to show the new member
      await loadNetworkData()
    } else {
      alert(`Failed to add downline: ${response.error}`)
    }
  } catch (error) {
    console.error('Error adding downline:', error)
    alert(`Error adding downline: ${error.message}`)
  } finally {
    addingDownline.value = false
  }
}

const closeModal = (event) => {
  if (event.target === event.currentTarget) {
    showGraphModal.value = false
  }
}

const closeAddDownlineModal = () => {
  showAddDownlineModal.value = false
  selectedMember.value = null
}

// Load data when component mounts
onMounted(() => {
  loadNetworkData()
})

// Summary counts
const level1Count = computed(() => level1Members.value.length)
const level2Count = computed(() => level2Members.value.length)
const level3Count = computed(() => level3Members.value.length)
const totalNetworkCount = computed(() => level1Count.value + level2Count.value + level3Count.value)

// Calculate network value based on contributions
const networkValue = computed(() => {
  const total = [...level1Members.value, ...level2Members.value, ...level3Members.value]
    .reduce((sum, member) => sum + (parseInt(member.contribution) || 0), 0)
  return total.toLocaleString()
})

// Star level for current user (same logic as dashboard)
const weeksSubscribed = computed(() => currentUser.value?.weeksSubscribed ?? currentUser.value?.subscriptionWeeks ?? 24)
const userLevelTeam = computed(() => calculateUserLevel(level1Members.value.length, weeksSubscribed.value))

// Prepare data for ECharts Network Graph
const currentUserForGraph = computed(() => {
  const result = {
    id: currentUser.value?.id, // Use actual numeric ID instead of 'current_user'
    name: currentUser.value?.firstName && currentUser.value?.lastName 
      ? `${currentUser.value.firstName} ${currentUser.value.lastName}`
      : currentUser.value?.email || 'You',
    fullName: currentUser.value?.firstName && currentUser.value?.lastName 
      ? `${currentUser.value.firstName} ${currentUser.value.lastName}`
      : currentUser.value?.email || 'You',
    email: currentUser.value?.email || '',
    level: currentUser.value?.level || 'Chief',
    referralCode: currentUser.value?.referralCode || ''
  }
  console.log('currentUserForGraph computed:', result)
  return result
})

const allDownlineMembers = computed(() => {
  console.log('=== ALL DOWNLINE MEMBERS COMPUTED ===')
  console.log('level1Members.value:', level1Members.value)
  console.log('level2Members.value:', level2Members.value)
  console.log('level3Members.value:', level3Members.value)
  console.log('level1Members.length:', level1Members.value.length)
  console.log('level2Members.length:', level2Members.value.length)
  console.log('level3Members.length:', level3Members.value.length)
  
  const result = [
    ...level1Members.value.map(member => ({
      ...member,
      name: member.name,
      fullName: member.name,
      level: member.userLevel,
      networkLevel: 1
    })),
    ...level2Members.value.map(member => ({
      ...member,
      name: member.name,
      fullName: member.name,
      level: member.userLevel,
      networkLevel: 2
    })),
    ...level3Members.value.map(member => ({
      ...member,
      name: member.name,
      fullName: member.name,
      level: member.userLevel,
      networkLevel: 3
    }))
  ]
  console.log('allDownlineMembers computed result:', result)
  console.log('allDownlineMembers result length:', result.length)
  return result
})
</script>