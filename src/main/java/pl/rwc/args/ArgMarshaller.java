package pl.rwc.args;

import static pl.rwc.args.ArgsException.Code.*;

public interface ArgMarshaller {
    <T> T parse(String value);
}

class UnsupportedTypeMarshaller implements ArgMarshaller {
    @Override
    public Void parse(String value) {
        throw new ArgsException(UNSUPPORTED_TYPE, value);
    }
}

class StringMarshaller implements ArgMarshaller {
    @Override
    public String parse(String value) {
        return value;
    }
}

class IntegerMarshaller implements ArgMarshaller {
    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }
}
