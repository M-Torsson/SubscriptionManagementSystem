package se.lexicon;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriberProcessorTest {

    @Test
    void testActiveSubscribers() {

        SubscriberDAO dao = new SubscriberDAO();

        // Add test data
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "c@test.com", Plan.PRO, false, 5));

        SubscriberProcessor processor = new SubscriberProcessor();

        List<Subscriber> result = processor.findSubscribers(
                dao.findAll(),
                BusinessRules.activeSubscriber()
        );

        // Expect only 2 active subscribers
        assertEquals(2, result.size());
    }

    @Test
    void testExpiringSubscriptions() {

        SubscriberDAO dao = new SubscriberDAO();

        // Add test data
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "c@test.com", Plan.PRO, true, 5));

        SubscriberProcessor processor = new SubscriberProcessor();

        List<Subscriber> result = processor.findSubscribers(
                dao.findAll(),
                BusinessRules.expiringSubscription()
        );

        // Expect subscribers with 0 or 1 month remaining
        assertEquals(2, result.size());
    }

    @Test
    void testActiveAndExpiringSubscribers() {

        SubscriberDAO dao = new SubscriberDAO();

        // Add test data
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "c@test.com", Plan.PRO, false, 0));
        dao.save(new Subscriber(4, "d@test.com", Plan.PRO, true, 5));

        SubscriberProcessor processor = new SubscriberProcessor();

        List<Subscriber> result = processor.findSubscribers(
                dao.findAll(),
                s -> BusinessRules.activeSubscriber().matches(s)
                        && BusinessRules.expiringSubscription().matches(s)
        );

        // Expect only subscribers who are active AND expiring
        assertEquals(2, result.size());
    }

    @Test
    void testExtendSubscriptionForPayingSubscribers() {

        SubscriberDAO dao = new SubscriberDAO();

        // Add test data
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "c@test.com", Plan.PRO, true, 0));

        SubscriberProcessor processor = new SubscriberProcessor();

        processor.applyToMatching(
                dao.findAll(),
                s -> BusinessRules.payingSubscriber().matches(s)
                        && BusinessRules.expiringSubscription().matches(s),
                BusinessRules.extendSubscription(2)
        );

        // Check results
        List<Subscriber> all = dao.findAll();

        Subscriber b = all.get(1);
        Subscriber c = all.get(2);

        assertEquals(3, b.getMonthsRemaining());
        assertEquals(2, c.getMonthsRemaining());
    }

    @Test
    void testDeactivateExpiredFreeSubscribers() {

        SubscriberDAO dao = new SubscriberDAO();

        // Add test data
        dao.save(new Subscriber(1, "a@test.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "b@test.com", Plan.BASIC, true, 0));
        dao.save(new Subscriber(3, "c@test.com", Plan.FREE, true, 2));

        SubscriberProcessor processor = new SubscriberProcessor();

        processor.applyToMatching(
                dao.findAll(),
                s -> s.getPlan() == Plan.FREE && s.getMonthsRemaining() == 0,
                BusinessRules.deactivateSubscriber()
        );

        List<Subscriber> all = dao.findAll();

        assertFalse(all.get(0).isActive()); // should be deactivated
        assertTrue(all.get(1).isActive());  // should remain active
        assertTrue(all.get(2).isActive());  // should remain active
    }
}