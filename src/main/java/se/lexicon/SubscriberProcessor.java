package se.lexicon;

import java.util.ArrayList;
import java.util.List;

public class SubscriberProcessor {

    public List<Subscriber> findSubscribers(List<Subscriber> list, SubscriberFilter filter) {
        List<Subscriber> result = new ArrayList<>();

        for (Subscriber s : list) {
            if (filter.matches(s)) {
                result.add(s);
            }
        }

        return result;
    }

    public void applyToMatching(List<Subscriber> list, SubscriberFilter filter, SubscriberAction action) {
        for (Subscriber s : list) {
            if (filter.matches(s)) {
                action.run(s);
            }
        }
    }
}