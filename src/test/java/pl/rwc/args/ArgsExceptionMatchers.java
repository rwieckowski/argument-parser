package pl.rwc.args;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

public class ArgsExceptionMatchers {
    public static ArgsExceptionCodeMatcher hasCode(ArgsException.Code code) {
        return new ArgsExceptionCodeMatcher(code);
    }

    public static ArgsExceptionValueMatcher hasValue(String value) {
        return new ArgsExceptionValueMatcher(value, -1);
    }

    public static ArgsExceptionValueMatcher hasValue(String value, int pos) {
        return new ArgsExceptionValueMatcher(value,pos);
    }
}

class ArgsExceptionCodeMatcher extends TypeSafeMatcher<ArgsException> {
    private final ArgsException.Code code;

    ArgsExceptionCodeMatcher(ArgsException.Code code) {
        this.code = code;
    }

    @Override
    protected boolean matchesSafely(ArgsException e) {
        return e.getCode() == code;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("code ")
                .appendValue(code);
    }

    @Override
    protected void describeMismatchSafely(ArgsException item, Description mismatchDescription) {
        mismatchDescription
                .appendText("was ")
                .appendValue(item.getCode());
    }
}

class ArgsExceptionValueMatcher extends TypeSafeMatcher<ArgsException> {
    private final String value;
    private final int pos;

    ArgsExceptionValueMatcher(String value, int pos) {
        this.value = value;
        this.pos = pos;
    }

    @Override
    protected boolean matchesSafely(ArgsException e) {
        String[] values = e.getValues();
        if (pos > -1) {
            return pos < values.length && value.equals(values[pos]);
        } else {
            for (String value : values) {
                if (this.value.equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("single ")
                .appendValue(value);
        if (pos > -1) {
            description.appendText(" at ");
            description.appendValue(pos);
            description.appendText(" index");
        }
    }

    @Override
    protected void describeMismatchSafely(ArgsException item, Description mismatchDescription) {
        String[] values = item.getValues();
        if (pos > -1 && pos < values.length) {
            mismatchDescription
                    .appendText("was ")
                    .appendValue(values[pos]);
        } else {
            mismatchDescription
                    .appendText("was ")
                    .appendValueList("[", "]", ",", Arrays.asList(values));
        }
    }
}