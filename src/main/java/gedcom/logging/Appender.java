package gedcom.logging;

import java.util.ArrayList;
import java.util.List;

public class Appender<T> {

    protected final List<T> list = new ArrayList<T>();

    private boolean started = false;

    protected void append(final T item) {
        if (started) {
            list.add(item);
        }
    }

    public List<T> getLog() {
        return new ArrayList<T>(list);
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }

}
