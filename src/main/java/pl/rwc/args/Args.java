package pl.rwc.args;

import java.util.*;

public class Args {
    private static final ArgMarshaller UNSUPPORTED_MARSHALLER = new UnsupportedTypeMarshaller();
    private List<Arg> args = new LinkedList<>();
    private Map<String,String> parsed = new HashMap<>();
    private Map<Class,ArgMarshaller> marshallers = new HashMap<Class,ArgMarshaller>() {{
        put(String.class, new StringMarshaller());
        put(Integer.class, new IntegerMarshaller());
    }};



    public int parse(String[] values) {
        ListIterator<String> it = Arrays.asList(values).listIterator();

        for (Arg arg : args) {
            parsed.put(arg.name, it.next());
        }

        if (it.hasNext()) {
            throw new ArgsException(ArgsException.Code.UNEXPECTED_ARGUMENT, it.next());
        }

        return it.nextIndex();
    }

    public Arg required(String name) {
        Arg arg = new Arg(name);
        args.add(arg);
        return arg;
    }

    public String getString(String name) {
        return getValue(String.class, name);
    }

    public Integer getInteger(String name) {
        return getValue(Integer.class, name);
    }

    public <T> T getValue(ArgMarshaller marshaller, String name) {
        return marshaller.parse(parsed.get(name));
    }

    public <T> T getValue(Class<T> cls, String name) {
        return getValue(marshallers.getOrDefault(cls, UNSUPPORTED_MARSHALLER), name);
    }
}

class Arg {
    final String name;

    Arg(String name) {
        this.name = name;
    }
}
