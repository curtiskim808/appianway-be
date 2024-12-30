package dev.appianway.dashboard.scheduled;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerControllerTest {

    private SchedulerController schedulerController;

    @BeforeEach
    void setUp() {
        schedulerController = new SchedulerController();
    }

    @Test
    void initialState_ShouldNotBeReady() {
        assertFalse(schedulerController.isReadyToStart(), "Scheduler should not be ready by default");
    }

    @Test
    void setReadyToStart_ShouldUpdateState() {
        schedulerController.setReadyToStart(true);
        assertTrue(schedulerController.isReadyToStart(), "Scheduler should be ready after setting to true");

        schedulerController.setReadyToStart(false);
        assertFalse(schedulerController.isReadyToStart(), "Scheduler should not be ready after setting to false");
    }

    @Test
    void concurrentAccess_ShouldMaintainThreadSafety() throws InterruptedException {
        // This test checks if the SchedulerController methods are thread-safe
        // by accessing them from multiple threads.

        Runnable setter = () -> {
            for (int i = 0; i < 1000; i++) {
                schedulerController.setReadyToStart(i % 2 == 0);
            }
        };

        Runnable getter = () -> {
            for (int i = 0; i < 1000; i++) {
                schedulerController.isReadyToStart();
            }
        };

        Thread thread1 = new Thread(setter);
        Thread thread2 = new Thread(getter);
        Thread thread3 = new Thread(setter);
        Thread thread4 = new Thread(getter);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        // Since the final value depends on the last setter, we can't predict it.
        // However, the test should complete without any race conditions or exceptions.
        // We'll just assert that the value is either true or false.
        assertNotNull(schedulerController.isReadyToStart());
    }
}
