package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Process;

public abstract class AbstractExecution implements Execution {
    protected final String id;

    protected final Process process;

    protected final AbstractExecutionPoint mainEp;

    protected volatile Status status;

    protected volatile SnapshotExecutionPoint ssMainEp;
    protected volatile boolean atSafePoint;

    protected AbstractExecution(String id, Process process) {
        this.id = id;
        this.process = process;
        this.mainEp = newMainExecutionPoint();
        this.status = Status.NEW;
        this.atSafePoint = true;
    }

    protected abstract AbstractExecutionPoint newMainExecutionPoint();

    @Override
    public String id() {
        return id;
    }

    @Override
    public Process process() {
        return process;
    }

    @Override
    public ExecutionPoint snapshot() {
        if (!atSafePoint) return ssMainEp;
        ssMainEp = SnapshotExecutionPoint.snapshot(this);
        return ssMainEp;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public void run() {
        if (status != Status.NEW) {
            throw new IllegalStateException("[PVM] Trying to run execution of a wrong status: " + status);
        }

        synchronized (this) {
            if (status != Status.NEW) return;
            status = Status.RUNNING;
        }

        mainEp.drive();
    }

    @Override
    public void signal(String event) {
        if (status != Status.RUNNING) {
            throw new IllegalStateException("[PVM] Trying to signal execution of a wrong status: " + status);
        }

        ssMainEp = SnapshotExecutionPoint.snapshot(this);
        atSafePoint = false;
        mainEp.signal(event);
        atSafePoint = true;
    }

    @Override
    public void pause() {
        if (status != Status.RUNNING) {
            throw new IllegalStateException("[PVM] Trying to pause execution of a wrong status: " + status);
        }

        synchronized (this) {
            status = Status.SUSPEND;
        }
    }

    @Override
    public void proceed() {
        if (status != Status.SUSPEND) {
            throw new IllegalStateException("[PVM] Trying to resume execution of a wrong status: " + status);
        }

        synchronized (this) {
            status = Status.RUNNING;
        }

        mainEp.drive();
    }

    void markTerminated() {
        status = Status.TERMINATED;
    }
}
