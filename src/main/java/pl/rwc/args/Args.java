package pl.rwc.args;

import java.util.*;

public class Args {
    private static final ArgMarshaller UNSUPPORTED_MARSHALLER = new UnsupportedTypeMarshaller();
    private List<Arg> args = new LinkedList<>();
    private Map<String,Values> parsed = new HashMap<>();
    private Map<Class,ArgMarshaller> marshallers = new HashMap<Class,ArgMarshaller>() {{
        put(String.class, new StringMarshaller());
        put(Integer.class, new IntegerMarshaller());
        put(List.class, new StringListMarshaller());
    }};



    public int parse(String[] values) {
        ListIterator<String> it = Arrays.asList(values).listIterator();

        for (Arg arg : args) {
            ArgUnmarshaller unmarshaller = arg.unmarshaller();
            Values vs = unmarshaller.unmarshall(it);
            parsed.put(arg.name, vs);
        }

        if (it.hasNext()) {
            throw new ArgsException(ArgsException.Code.UNEXPECTED_ARGUMENT, it.next());
        }

        return it.nextIndex();
    }

    public Arg required(String name) {
        Arg arg = new Arg(Arg.Type.REQUIRED, name);
        args.add(arg);
        return arg;
    }

    public Arg optional(String name) {
        Arg arg = new Arg(Arg.Type.OPTIONAL, name);
        args.add(arg);
        return arg;
    }

    public Arg collection(String name) {
        Arg arg = new Arg(Arg.Type.COLLECTION, name);
        args.add(arg);
        return arg;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStrings(String name) {
        return (List<String>)getValue(List.class, name);
    }

    public String getString(String name) {
        return getValue(String.class, name);
    }

    public Integer getInteger(String name) {
        return getValue(Integer.class, name);
    }

    public <T> T getValue(ArgMarshaller<T> marshaller, String name) {
        Values values = parsed.get(name);
        try {
            return marshaller.parse(values);
        } catch (Exception e) {
            throw new ArgsException(ArgsException.Code.INVALID_FORMAT, values.all());
        }
    }

    public <T> T getValue(Class<T> cls, String name) {
        @SuppressWarnings("unchecked")
        ArgMarshaller<T> marshaller = marshallers.getOrDefault(cls, UNSUPPORTED_MARSHALLER);
        return getValue(marshaller, name);
    }
}

class Arg {
    final Type type;
    final String name;
    Values defaultValue = Values.empty();

    Arg(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Arg orDefaultValue(String defaultValue) {
        this.defaultValue = Values.of(defaultValue);
        return this;
    }

    public ArgUnmarshaller unmarshaller() {
        ArgUnmarshaller unmarshaller;
        switch (type) {
            case REQUIRED:
                unmarshaller = new RequiredUnmarshaller();
                break;
            case OPTIONAL:
                unmarshaller = new OptionalUnmarshaller();
                break;
            case COLLECTION:
                unmarshaller = new CollectionUnmarshaller();
                break;
            default:
                throw new RuntimeException("Unsupported argument type");
        }

        if (!defaultValue.isEmpty()) {
            unmarshaller = new DefaultValueUnmarshaller(unmarshaller, defaultValue);
        }

        return unmarshaller;
    }

    enum Type {
        REQUIRED, OPTIONAL, COLLECTION
    }
}
