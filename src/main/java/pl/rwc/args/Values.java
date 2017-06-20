package pl.rwc.args;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Values {
        private final List<String> values;

    public static Values of(String value) {
        return new Values(Collections.singletonList(value));
    }

    public static Values of(List<String> values) {
        return new Values(values);
    }

    public static Values empty() {
        return new Values(Collections.emptyList());
    }

    private Values(List<String> values) {
        this.values = values;
    }

    public Optional<String> single() {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }

    public List<String> all() {
        return values;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}

