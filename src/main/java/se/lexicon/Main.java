package se.lexicon;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        SubscriberDAO dao = new SubscriberDAO();

        // Add sample subscribers
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "c@test.com", Plan.PRO, true, 5));
        dao.save(new Subscriber(4, "d@test.com", Plan.FREE, false, 0));

        SubscriberProcessor processor = new SubscriberProcessor();

        // Get active subscribers
        List<Subscriber> active = processor.findSubscribers(
                dao.findAll(),
                BusinessRules.activeSubscriber()
        );

        System.out.println("Active Subscribers:");
        active.forEach(s -> System.out.println(s.getEmail()));

        // Extend subscription for paying subscribers with expiring plans
        processor.applyToMatching(
                dao.findAll(),
                s -> BusinessRules.payingSubscriber().matches(s)
                        && BusinessRules.expiringSubscription().matches(s),
                BusinessRules.extendSubscription(2)
        );

        // Deactivate expired free subscribers
        processor.applyToMatching(
                dao.findAll(),
                s -> s.getPlan() == Plan.FREE && s.getMonthsRemaining() == 0,
                BusinessRules.deactivateSubscriber()
        );
    }
}