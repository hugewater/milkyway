<template>
  <AppLayout>
    <div class="p-6 max-w-6xl mx-auto">
      <h1 class="text-2xl font-bold text-deep-ocean mb-2">Commission Structure</h1>
      <p class="text-gray-600 mb-6">Official affiliate rank hierarchy and multi-generation payout schedule. Leadership bonus and upgrade criteria included.</p>

      <!-- Commission Table -->
      <div class="overflow-x-auto mb-10">
        <table class="min-w-full border border-gray-200 bg-white rounded-lg overflow-hidden">
          <thead class="bg-ocean text-white">
            <tr class="text-sm">
              <th class="px-4 py-3 text-left font-medium">Rank</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">Direct Referral</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">1st Generation</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">2nd Generation</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">3rd Generation</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">Leadership Bonus</th>
              <th class="px-4 py-3 text-left font-medium whitespace-nowrap">Upgrade Conditions</th>
            </tr>
          </thead>
          <tbody class="text-sm divide-y divide-gray-100">
            <tr v-for="row in commissionRows" :key="row.rank" class="hover:bg-gray-50">
              <td class="px-4 py-3 font-semibold text-ocean">{{ row.rank }}</td>
              <td class="px-4 py-3">{{ row.direct || '-' }}</td>
              <td class="px-4 py-3">{{ row.gen1 || '-' }}</td>
              <td class="px-4 py-3">{{ row.gen2 || '-' }}</td>
              <td class="px-4 py-3">{{ row.gen3 || '-' }}</td>
              <td class="px-4 py-3">{{ row.leadership || '-' }}</td>
              <td class="px-4 py-3 w-[260px]">{{ row.upgrade }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Legend / Notes -->
      <div class="grid md:grid-cols-3 gap-6 mb-10">
        <div class="bg-white p-4 rounded-lg border border-gray-100 shadow-sm">
          <h3 class="font-semibold text-ocean mb-2">Generations Definition</h3>
            <ul class="list-disc list-inside text-xs text-gray-600 space-y-1">
              <li>Direct Referral = personally enrolled user.</li>
              <li>1st Gen = direct referrals (same as Direct override stream).</li>
              <li>2nd / 3rd Gen = depth levels determined by referral chaining.</li>
            </ul>
        </div>
        <div class="bg-white p-4 rounded-lg border border-gray-100 shadow-sm">
          <h3 class="font-semibold text-ocean mb-2">Leadership Bonus</h3>
          <p class="text-xs text-gray-600">An additional override applied on qualifying network volume for ranks Promoter and above where defined (— indicates none).</p>
        </div>
        <div class="bg-white p-4 rounded-lg border border-gray-100 shadow-sm">
          <h3 class="font-semibold text-ocean mb-2">Upgrade Logic</h3>
          <p class="text-xs text-gray-600">System should auto-evaluate upgrade when a user meets both direct referral count and total downline count (and consumption requirement where specified) at end of each settlement cycle.</p>
        </div>
      </div>

      <div class="bg-white border border-gray-100 rounded-lg shadow-sm p-5 mb-8">
        <h3 class="font-semibold mb-3 text-ocean">Operational Flow</h3>
        <ol class="list-decimal list-inside text-sm text-gray-600 space-y-1">
          <li>Qualifying purchase recorded (amount locked).</li>
          <li>Generation & rank logic resolves eligible payout percentages.</li>
          <li>Entries stored as Pending commissions.</li>
          <li>After verification window passes, commissions become Confirmed.</li>
          <li>Leadership bonus computed for ranks with entitlement.</li>
          <li>Batch payout process exports / distributes funds.</li>
          <li>Ledger status flips to Paid; available balance updated.</li>
        </ol>
      </div>

      <div class="text-center text-xs text-gray-400 mt-10">Commission schedule implemented from provided specification.</div>
    </div>
  </AppLayout>
</template>

<script setup>
import AppLayout from '../components/layouts/AppLayout.vue'

const commissionRows = [
  { rank: 'President',   direct: '25%', gen1: '15%', gen2: '10%', gen3: '5%', leadership: '10%', upgrade: 'Directly Refer 50 People, 100 Downlines' },
  { rank: 'Influencer',  direct: '20%', gen1: '12%', gen2: '7%',  gen3: '3%', leadership: '7%',  upgrade: 'Directly Refer 20 People, 50 Downlines' },
  { rank: 'Leader',      direct: '18%', gen1: '10%', gen2: '5%',  gen3: '2%', leadership: '5%',  upgrade: 'Directly Refer 10 People, 30 Downlines' },
  { rank: 'Promoter',    direct: '15%', gen1: '7%',  gen2: '3%',  gen3: '-', leadership: '-',     upgrade: 'Directly Refer 5 People, 10 Downlines' },
  { rank: 'Reader',      direct: '12%', gen1: '5%',  gen2: '-',   gen3: '-', leadership: '-',     upgrade: 'Directly Refer 3 People, Each Consuming 1680' },
  { rank: 'Subscriber',  direct: '10%', gen1: '-',   gen2: '-',   gen3: '-', leadership: '-',     upgrade: 'Directly Refer 1 Person Consuming 1680' },
  { rank: 'Fan',         direct: '-',   gen1: '-',   gen2: '-',   gen3: '-', leadership: '-',     upgrade: '-' }
]
</script>

<style scoped>
.text-ocean { color: #0d4d8c; }
</style>
