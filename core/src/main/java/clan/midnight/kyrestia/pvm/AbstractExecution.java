package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Process;

import java.util.*;

public abstract class AbstractExecution implements Execution {
    protected final Process process;

    protected final AbstractExecutionPoint mainEp;

    protected volatile Status status;

    protected volatile SnapshotExecutionPoint ssMainEp;
    protected volatile boolean atSafePoint;

    protected AbstractExecution(Process process) {
        this.process = process;
        this.mainEp = newMainExecutionPoint();
        this.status = Status.NEW;
        this.atSafePoint = true;
    }

    protected abstract AbstractExecutionPoint newMainExecutionPoint();

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public Collection<ExecutionPoint> getExecutionPoints() {
        if (!atSafePoint) return getExecutionPoints(ssMainEp);
        ssMainEp = SnapshotExecutionPoint.snapshot(this);
        return getExecutionPoints(ssMainEp);
    }

    private Collection<ExecutionPoint> getExecutionPoints(ExecutionPoint rootEp) {
        if (rootEp.getSubExecutionPoints().isEmpty()) return Collections.singletonList(rootEp);

        ArrayList<ExecutionPoint> res = new ArrayList<>();
        Queue<ExecutionPoint> q = new LinkedList<>();
        q.offer(rootEp);
        while (!q.isEmpty()) {
            ExecutionPoint cur = q.poll();
            for (ExecutionPoint ep : cur.getSubExecutionPoints()) q.offer(ep);
            res.add(cur);
        }
        return res;
    }

    @Override
    public Status getStatus() {
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
    public void resume() {
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
