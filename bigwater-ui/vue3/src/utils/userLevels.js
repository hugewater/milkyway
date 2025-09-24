// User level system for BigWater Digital Weekly Journal

export const USER_ROLES = {
  SUBSCRIBER: 'subscriber',
  ADMIN: 'admin'
}

export const SUBSCRIBER_LEVELS = {
  FAN: {
    level: 1,
    name: 'Fan',
    stars: 1,
    minReferrals: 0,
    bonusMultiplier: 0, // No extra bonus
    icon: '👨‍💼'
  },
  SUBSCRIBER: {
    level: 2,
    name: 'Subscriber',
    stars: 2,
    minReferrals: 1,
    bonusMultiplier: 0.005, // Level 1 extra 0.5%
    icon: '⭐'
  },
  READER: {
    level: 3,
    name: 'Reader', 
    stars: 3,
    minReferrals: 5,
    bonusMultiplier: 0.01, // Level 1 extra 1%
    icon: '⭐⭐'
  },
  PROMOTER: {
    level: 4,
    name: 'Promoter',
    stars: 4,
    minReferrals: 15,
    bonusMultiplier: 0.015, // Level 2 extra 1.5%
    icon: '⭐⭐⭐'
  },
  LEADER: {
    level: 5,
    name: 'Leader',
    stars: 5,
    minReferrals: 30,
    bonusMultiplier: 0.02, // Level 2 extra 2%
    icon: '⭐⭐⭐⭐'
  },
  INFLUENCER: {
    level: 6,
    name: 'Influencer',
    stars: 6,
    minReferrals: 50,
    bonusMultiplier: 0.025, // Level 3 extra 2.5%
    icon: '⭐⭐⭐⭐⭐'
  },
  PRESIDENT: {
    level: 7,
    name: 'President',
    stars: 7,
    minReferrals: 100,
    bonusMultiplier: 0.03, // All levels extra 3%
    icon: '⭐⭐⭐⭐⭐⭐'
  }
}

export const REWARD_STRUCTURE = {
  TIER_1: {
    name: 'Tier 1 (Direct)',
    baseBonus: 0.15, // 15% bonus
    description: 'Direct referrals'
  },
  TIER_2: {
    name: 'Tier 2 (Indirect)',
    baseBonus: 0.05, // 5% bonus
    description: 'Second level referrals'
  },
  TIER_3: {
    name: 'Tier 3 (Network)',
    baseBonus: 0.01, // 1% bonus
    description: 'Third level referrals'
  }
}

// Calculate user level based on direct referrals
export function calculateUserLevel(directReferrals, weeksSubscribed = 24) {
  // Default is FAN level
  if (!directReferrals || directReferrals === 0) {
    return SUBSCRIBER_LEVELS.FAN
  }

  const levels = Object.values(SUBSCRIBER_LEVELS)
    .filter(l => l.level > 0)
    .reverse()
  
  for (const level of levels) {
    if (directReferrals >= level.minReferrals) {
      return level
    }
  }
  
  return SUBSCRIBER_LEVELS.FAN
}

// Calculate bonus percentage for a tier based on user level
export function calculateBonusPercentage(userLevel, tier) {
  const baseTier = REWARD_STRUCTURE[tier]
  if (!baseTier) return 0
  
  let bonus = baseTier.baseBonus
  
  // Apply level multipliers
  switch (userLevel.level) {
    case 2: // Subscriber - Level 1 extra 0.5%
      if (tier === 'TIER_1') {
        bonus += userLevel.bonusMultiplier
      }
      break
    case 3: // Reader - Level 1 extra 1%
      if (tier === 'TIER_1') {
        bonus += userLevel.bonusMultiplier
      }
      break
    case 4: // Promoter - Level 2 extra 1.5%
      if (tier === 'TIER_2') {
        bonus += userLevel.bonusMultiplier
      }
      break
    case 5: // Leader - Level 2 extra 2%
      if (tier === 'TIER_2') {
        bonus += userLevel.bonusMultiplier
      }
      break
    case 6: // Influencer - Level 3 extra 2.5%
      if (tier === 'TIER_3') {
        bonus += userLevel.bonusMultiplier
      }
      break
    case 7: // President - All levels extra 3%
      bonus += userLevel.bonusMultiplier
      break
  }
  
  return bonus
}

// Calculate total earnings from referrals
export function calculateTotalEarnings(userLevel, tier1Count, tier2Count, tier3Count, subscriptionPrice = 30) {
  const tier1Bonus = calculateBonusPercentage(userLevel, 'TIER_1')
  const tier2Bonus = calculateBonusPercentage(userLevel, 'TIER_2')
  const tier3Bonus = calculateBonusPercentage(userLevel, 'TIER_3')
  
  const tier1Earnings = tier1Count * subscriptionPrice * tier1Bonus
  const tier2Earnings = tier2Count * subscriptionPrice * tier2Bonus
  const tier3Earnings = tier3Count * subscriptionPrice * tier3Bonus
  
  return {
    tier1Earnings,
    tier2Earnings,
    tier3Earnings,
    totalEarnings: tier1Earnings + tier2Earnings + tier3Earnings
  }
}