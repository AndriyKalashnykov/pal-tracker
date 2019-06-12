package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> timeEntryArrayList = new ArrayList<>();
    private long count;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        count++;
        timeEntry.setId(count);
        timeEntryArrayList.add(timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return timeEntryArrayList.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return Collections.unmodifiableList(timeEntryArrayList);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry entry = find(id);

        if (entry == null) {
            return null;
        }

        entry.setProjectId(timeEntry.getProjectId());
        entry.setUserId(timeEntry.getUserId());
        entry.setDate(timeEntry.getDate());
        entry.setHours(timeEntry.getHours());
        entry.setDate(timeEntry.getDate());

        return entry;
    }

    @Override
    public void delete(long id) {
        TimeEntry entry = find(id);

        if (entry != null) {
            timeEntryArrayList.remove(entry);
        }
    }
}
