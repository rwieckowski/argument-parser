package pl.rwc.args;

import java.util.LinkedList;
import java.util.List;
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

class CollectionUnmarshaller implements ArgUnmarshaller {
    @Override
    public Values unmarshall(ListIterator<String> iterator) {
        List<String> values = new LinkedList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        return Values.of(values);
    }
}

class DefaultValueUnmarshaller implements ArgUnmarshaller {
    private final ArgUnmarshaller unmarshaller;
    private final Values defaultValues;

    DefaultValueUnmarshaller(ArgUnmarshaller unmarshaller, Values defaultValues) {
        this.unmarshaller = unmarshaller;
        this.defaultValues = defaultValues;
    }

    @Override
    public Values unmarshall(ListIterator<String> iterator) {
        Values values = unmarshaller.unmarshall(iterator);
        return values.isEmpty() ? defaultValues : values;
    }
}