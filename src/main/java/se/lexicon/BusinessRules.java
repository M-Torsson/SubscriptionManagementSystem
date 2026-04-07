package se.lexicon;

public class BusinessRules {

    // 1. Active Subscriber
    public static SubscriberFilter activeSubscriber() {
        return s -> s.isActive();
    }

    // 2. Expiring Subscription
    public static SubscriberFilter expiringSubscription() {
        return s -> s.getMonthsRemaining() <= 1;
    }

    // 3. Active and Expiring
    public static SubscriberFilter activeAndExpiring() {
        return s -> s.isActive() && s.getMonthsRemaining() <= 1;
    }

    // 4. By Plan
    public static SubscriberFilter byPlan(Plan plan) {
        return s -> s.getPlan() == plan;
    }

    // 5. Paying Subscriber
    public static SubscriberFilter payingSubscriber() {
        return s -> s.getPlan() == Plan.BASIC || s.getPlan() == Plan.PRO;
    }

    // 6. Extend Subscription
    public static SubscriberAction extendSubscription(int months) {
        return s -> s.setMonthsRemaining(s.getMonthsRemaining() + months);
    }

    // 7. Deactivate Subscriber
    public static SubscriberAction deactivateSubscriber() {
        return s -> s.setActive(false);
    }
}