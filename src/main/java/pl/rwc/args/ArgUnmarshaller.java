package pl.rwc.args;

import java.util.ListIterator;

public interface ArgUnmarshaller {
    Values unmarshall(ListIterator<String> iterator);
}

class RequiredUnmarshaller implements ArgUnmarshaller {
    @Override
    public Values unmarshall(ListIterator<String> iterator) {
        return Values.of(iterator.next());
    }
}

class OptionalUnmarshaller implements ArgUnmarshaller {
    @Override
    public Values unmarshall(ListIterator<String> iterator) {
        if (iterator.hasNext()) {
            return Values.of(iterator.next());
        } else {
            return Values.empty();
        }
    }
}

class DefaultValueUnmarshaller implements ArgUnmarshaller {
    private final ArgUnmarshaller unmarshaller;
    private final String defaultValue;

    DefaultValueUnmarshaller(ArgUnmarshaller unmarshaller, String defaultValue) {
        this.unmarshaller = unmarshaller;
        this.defaultValue = defaultValue;
    }

    @Override
    public Values unmarshall(ListIterator<String> iterator) {
        Values values = unmarshaller.unmarshall(iterator);
        return values.isEmpty() ? Values.of(defaultValue) : values;
    }
}