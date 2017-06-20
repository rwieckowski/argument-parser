package pl.rwc.args;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArgsExceptionTest {
    @Test
    public void formatMessageWithValue() throws Exception {
        ArgsException exception = new ArgsException(ArgsException.Code.UNEXPECTED_ARGUMENT, "foo");

        assertEquals("Unexpected argument: 'foo'", exception.getMessage());
    }
}