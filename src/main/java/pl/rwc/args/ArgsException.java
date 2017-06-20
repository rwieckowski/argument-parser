package pl.rwc.args;

import java.util.List;

public class ArgsException extends RuntimeException {
    private final Code code;
    private final String[] values;

    public ArgsException(Code code, String... values) {
        super(code.formatMessage(values));
        this.code = code;
        this.values = values;
    }

    public ArgsException(Code code, List<String> values) {
        this(code, values.toArray(new String[]{}));
    }

    private String getErrorMessage() {
        return code.toString();
    }

    public Code getCode() {
        return code;
    }

    public String[] getValues() {
        return values;
    }

    public enum Code {
        OK("No error found"),
        UNEXPECTED_ARGUMENT("Unexpected argument: '%s'"),
        INVALID_FORMAT("Invalid format: '%s'"),
        UNSUPPORTED_TYPE("Unsupported type: '%s'");

        private final String format;

        Code(String format) {
            this.format = format;
        }

        public String formatMessage(String... values) {
            return String.format(format, values);
        }
    }
}
