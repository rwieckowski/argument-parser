package pl.rwc.args;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static pl.rwc.args.ArgsException.Code.*;
import static pl.rwc.args.ArgsExceptionMatchers.*;

public class ArgsTest {
    @Rule
    public ExpectedException error = ExpectedException.none();
    private Args args;

    @Before
    public void setUp() throws Exception {
        args = new Args();
    }

    @Test
    public void emptySchemaWithNoArguments() throws Exception {
        int parsed = parse();

        assertParsedArguments(0, parsed);
    }

    @Test
    public void emptySchemaWithOneArgument() throws Exception {
        expectError(UNEXPECTED_ARGUMENT, "lorem");
        parse("lorem");
    }

    @Test
    public void emptySchemaWithMultipleArguments() throws Exception {
        expectError(UNEXPECTED_ARGUMENT, "lorem");
        parse("lorem", "ipsum", "dolor", "sit", "amet");
    }

    @Test
    public void requiredArgumentPresent() throws Exception {
        args.required("foo");

        int parsed = parse("lorem");

        assertParsedArguments(1, parsed);
        assertArgumentValue("lorem", "foo");
    }

    @Test
    public void multipleRequiredArgumentsPresent() throws Exception {
        args.required("foo");
        args.required("bar");
        args.required("baz");

        int parsed = parse("lorem", "ipsum", "dolor");

        assertParsedArguments(3, parsed);
        assertArgumentValue("lorem", "foo");
        assertArgumentValue("ipsum", "bar");
        assertArgumentValue("dolor", "baz");
    }

    @Test
    public void optionalArgumentPresent() throws Exception {
        args.optional("foo");

        int parsed = parse("lorem");

        assertParsedArguments(1, parsed);
        assertArgumentValue("lorem", "foo");
    }

    @Test
    public void optionalArgumentNotPresent() throws Exception {
        args.optional("foo");

        int parsed = parse();

        assertParsedArguments(0, parsed);
        assertArgumentValue(null, "foo");
    }

    @Test
    public void optionalArgumentPresentWithDefaultValue() throws Exception {
        args.optional("foo").orDefaultValue("ipsum");

        int parsed = parse("lorem");

        assertParsedArguments(1, parsed);
        assertArgumentValue("lorem", "foo");

    }

    @Test
    public void optionalArgumentNotPresentWithDefaultValue() throws Exception {
        args.optional("foo").orDefaultValue("ipsum");

        int parsed = parse();

        assertParsedArguments(0, parsed);
        assertArgumentValue("ipsum", "foo");

    }

    @Test
    public void marshallerError() throws Exception {
        ArgMarshaller<Object> INVALID_FORMAT_MARSHALLER = value -> {
            throw new RuntimeException("Parse failed");
        };
        args.required("foo");
        parse("lorem");

        expectError(INVALID_FORMAT, "lorem");
        args.getValue(INVALID_FORMAT_MARSHALLER, "foo");
    }

    private void expectError(ArgsException.Code code, String value) {
        error.expect(ArgsException.class);
        error.expect(hasCode(code));
        error.expect(hasValue(value));
    }

    private int parse(String... values) {
        return args.parse(values);
    }

    private void assertParsedArguments(int expected, int parsed) {
        assertEquals("number of parsed arguments", expected, parsed);
    }

    private void assertArgumentValue(String expected, String name) {
        assertEquals("argument '" + name + "' single", expected, args.getString(name));
    }

}