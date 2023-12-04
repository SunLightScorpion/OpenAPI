package pl.nightdev701.util;

public class StringHelper {

    String input;
    int sub;

    public StringHelper(String input) {
        this.input = input;
        this.sub = 1;
    }

    public StringHelper(String input, int len) {
        this.input = input;
        this.sub = len;
    }

    public String getSplitBase() {
        return sortData()[0].toString();
    }

    public String getSplitSubBase() {
        return sortData()[1].toString();
    }

    private Object[] sortData() {
        String format = input.substring(input.length() - sub);
        String du = input.substring(0, input.length() - sub);
        return new Object[]{format, du};
    }

}
