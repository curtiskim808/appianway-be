package dev.appianway.dashboard.scheduled;

import org.springframework.stereotype.Component;

@Component
public class SchedulerController {
    private boolean readyToStart = false;

    public synchronized void setReadyToStart(boolean readyToStart) {
        this.readyToStart = readyToStart;
    }

    public synchronized boolean isReadyToStart() {
        return readyToStart;
    }
}
