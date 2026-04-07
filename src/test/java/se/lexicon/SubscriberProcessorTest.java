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
}