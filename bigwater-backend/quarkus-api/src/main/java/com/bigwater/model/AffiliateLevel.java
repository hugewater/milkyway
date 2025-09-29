package com.bigwater.model;

public enum AffiliateLevel {
    FAN(0, 0, 0, 0, 0, 0, 0, 0, 0),
    SUBSCRIBER(1, 10, 0, 0, 0, 0, 1, 0, 1680),
    READER(2, 12, 5, 0, 0, 0, 3, 0, 1680),
    PROMOTER(3, 15, 7, 3, 0, 0, 5, 10, 0),
    LEADER(4, 18, 10, 5, 2, 5, 10, 30, 0),
    INFLUENCER(5, 20, 12, 7, 3, 7, 20, 50, 0),
    PRESIDENT(6, 25, 15, 10, 5, 10, 50, 100, 0);

    private final int order;
    private final int directReferralPercent;
    private final int firstGenPercent;
    private final int secondGenPercent;
    private final int thirdGenPercent;
    private final int leadershipBonusPercent;
    private final int minDirectReferrals;
    private final int minTotalDownlines;
    private final int minConsumption;

    AffiliateLevel(int order, int directReferralPercent, int firstGenPercent, 
                   int secondGenPercent, int thirdGenPercent, int leadershipBonusPercent,
                   int minDirectReferrals, int minTotalDownlines, int minConsumption) {
        this.order = order;
        this.directReferralPercent = directReferralPercent;
        this.firstGenPercent = firstGenPercent;
        this.secondGenPercent = secondGenPercent;
        this.thirdGenPercent = thirdGenPercent;
        this.leadershipBonusPercent = leadershipBonusPercent;
        this.minDirectReferrals = minDirectReferrals;
        this.minTotalDownlines = minTotalDownlines;
        this.minConsumption = minConsumption;
    }

    // Getters
    public int getOrder() { return order; }
    public int getDirectReferralPercent() { return directReferralPercent; }
    public int getFirstGenPercent() { return firstGenPercent; }
    public int getSecondGenPercent() { return secondGenPercent; }
    public int getThirdGenPercent() { return thirdGenPercent; }
    public int getLeadershipBonusPercent() { return leadershipBonusPercent; }
    public int getMinDirectReferrals() { return minDirectReferrals; }
    public int getMinTotalDownlines() { return minTotalDownlines; }
    public int getMinConsumption() { return minConsumption; }

    public boolean isPresident() {
        return this == PRESIDENT;
    }

    public static AffiliateLevel getNextLevel(AffiliateLevel currentLevel) {
        if (currentLevel == PRESIDENT) return PRESIDENT;
        
        for (AffiliateLevel level : values()) {
            if (level.order == currentLevel.order + 1) {
                return level;
            }
        }
        return currentLevel;
    }
}