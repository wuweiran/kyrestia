package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Process;

import java.util.*;

public abstract class PvmExecution implements Execution {
    protected final Process process;

    protected final PvmExecutionPoint mainEp;

    protected volatile PvmExecutionPointView ssMainEp;

    protected volatile Status status;

    protected PvmExecution(Process process) {
        this.process = process;
        this.mainEp = createMainExecutionPoint();
        this.ssMainEp = PvmExecutionPointView.snapshot(this);
        this.status = Status.NEW;
    }

    protected abstract PvmExecutionPoint createMainExecutionPoint();

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public Collection<ExecutionPoint> getExecutionPoints() {
        ssMainEp = PvmExecutionPointView.snapshot(this);
        if (ssMainEp.getSubExecutionPoints().isEmpty()) return Collections.singletonList(ssMainEp);

        ArrayList<ExecutionPoint> res = new ArrayList<>();
        Queue<ExecutionPoint> q = new LinkedList<>();
        q.offer(ssMainEp);
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

        ssMainEp = PvmExecutionPointView.snapshot(this);
        mainEp.signal(event);
        ssMainEp = PvmExecutionPointView.snapshot(this);
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
