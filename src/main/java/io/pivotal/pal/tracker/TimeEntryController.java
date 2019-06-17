package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class TimeEntryController {
    private final TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository repository, MeterRegistry meterRegistry) {

        this.repository = repository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("time-entries")
    public ResponseEntity create(@RequestBody TimeEntry entry) throws URISyntaxException {
        TimeEntry result = repository.create(entry);

        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());

        return ResponseEntity
                .created(new URI(String.format("/time-entries/%s", entry.getId())))
                .body(result);
    }

    @GetMapping("time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry = repository.find(id);

        if (entry == null) {
            return ResponseEntity.notFound().build();
        } else {
            actionCounter.increment();
            return ResponseEntity.ok(entry);
        }
    }

    @GetMapping("time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return ResponseEntity.ok(repository.list());
    }

    @PutMapping("time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry entry) {
        TimeEntry updated = repository.update(id, entry);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        } else {
            actionCounter.increment();
            return ResponseEntity.ok(updated);
        }
    }

    @DeleteMapping("time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        repository.delete(id);
        actionCounter.increment();
        return ResponseEntity.noContent().build();
    }
}
