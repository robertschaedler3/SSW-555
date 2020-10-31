package gedcom.logging;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import gedcom.logging.Logger.LogEvent;

public class LogAppender extends Appender<LogEvent> {

    public void reset() {
        this.list.clear();
    }

    public int countEvents() {
        return list.size();
    }

    public boolean contains(String string, Level level) {
        return this.list.stream().anyMatch(event -> event.getMessage().contains(string) && event.getLevel().equals(level));
    }
    
    public boolean contains(String string, Level level, int code) {
        return this.list.stream().anyMatch(event -> event.getMessage().contains(string) && event.getLevel().equals(level) && event.getCode() == code);
    }

    public List<LogEvent> search(String string) {
        return this.list.stream().filter(event -> event.getMessage().contains(string)).collect(Collectors.toList());
    }

    public List<LogEvent> search(String string, Level level) {
        return this.list.stream().filter(event -> event.getMessage().contains(string) && event.getLevel().equals(level)).collect(Collectors.toList());
    }

    public int getSize() {
        return this.list.size();
    }

    public List<LogEvent> getLoggedEvents() {
        return Collections.unmodifiableList(this.list);
    }
}
