package pl.nightdev701.util.stream;

import pl.nightdev701.logger.AbstractLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;

public class OpenPrintStream extends PrintStream {

    AbstractLogger logger;

    private OpenPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public OpenPrintStream(AbstractLogger logger) throws FileNotFoundException {
        super("alternative_log.txt");
        this.logger = logger;
    }

    @Override
    public void println(String x) {
        logger.log(Level.INFO, x);
        new File("alternative_log.txt").delete();
    }

    @Override
    public void println(int x) {
        println(objectToString(x));
    }

    @Override
    public void println(char x) {
        println(objectToString(x));
    }

    @Override
    public void println(boolean x) {
        println(objectToString(x));
    }

    @Override
    public void println(long x) {
        println(objectToString(x));
    }

    @Override
    public void println(float x) {
        println(objectToString(x));
    }

    @Override
    public void println(char[] x) {
        println(objectToString(x));
    }

    @Override
    public void println(double x) {
        println(objectToString(x));
    }

    @Override
    public void println(Object x) {
        println(objectToString(x));
    }

    private String objectToString(Object o) {
        return o.toString();
    }
}
