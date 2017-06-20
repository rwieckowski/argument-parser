package pl.rwc.args;

import java.util.List;

import static pl.rwc.args.ArgsException.Code.*;

public interface ArgMarshaller<T> {
    T parse(Values values);
}

class UnsupportedTypeMarshaller implements ArgMarshaller<Void> {
    @Override
    public Void parse(Values values) {
        throw new ArgsException(UNSUPPORTED_TYPE, values.all());
    }
}

class StringMarshaller implements ArgMarshaller<String> {
    @Override
    public String parse(Values values) {
        return values.single().orElse(null);
    }
}

class IntegerMarshaller implements ArgMarshaller<Integer> {
    @Override
    public Integer parse(Values values) {
        return values.single().map(Integer::parseInt).orElse(null);
    }
}
