package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;
import clan.midnight.kyrestia.persist.model.ExecutionPO;
import clan.midnight.kyrestia.persist.model.ExecutionPointPO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ExecutionFactory {
    private ExecutionFactory() {}

    public static Execution createExecution(Process process) {
        String id = Configuration.idGenerator.generate();
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(id, process, Configuration.executorService);
        }
        return new SingleThreadSyncExecution(id, process);
    }

    public static Execution createExecution(Process process, Map<String, Serializable> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return createExecution(process);
        }

        String id = Configuration.idGenerator.generate();
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(id, process, Configuration.executorService, parameters);
        }
        return new SingleThreadSyncExecution(id, process, parameters);
    }

    public static Execution restoreExecution(Process process, ExecutionPO executionPO) {
        Execution.Status status = Execution.Status.valueOf(executionPO.getStatus());
        ExecutionPointPO mainEpPO = executionPO.getMainExecutionPoint();
        boolean multiThread = Configuration.multiThreadExecution;
        if (mainEpPO == null) {
            throw new IllegalArgumentException("[PVM] Must have a main execution point.");
        }
        ExecutionPoint.Status mainEpStatus = ExecutionPoint.Status.valueOf(mainEpPO.getStatus());
        Node currentNode = process.node(mainEpPO.getCurrentNodeId());
        if (currentNode == null && mainEpStatus != ExecutionPoint.Status.TERMINATED) {
            throw new IllegalArgumentException("[PVM] Current node cannot be null");
        }

        ExecutionPoint.NodeStage currentNodeStage = ExecutionPoint.NodeStage.valueOf(mainEpPO.getCurrentNodeStage());
        Map<String, Serializable> localContext = mainEpPO.getLocalContext();
        String waitEvent = mainEpPO.getWaitEvent();
        boolean waitingBereaved = mainEpPO.getWaitingBereaved();
        ArrayList<AbstractExecutionPoint> subEps = new ArrayList<>();
        AbstractExecutionPoint.MetaContainer metaContainer = new AbstractExecutionPoint.MetaContainer(subEps,
                localContext, mainEpStatus, currentNode, currentNodeStage, waitEvent, waitingBereaved);
        AbstractExecution execution;
        if (multiThread) {
            execution = new MultiThreadAsyncExecution(executionPO.getId(), process, Configuration.executorService,
                    metaContainer, status);
        } else {
            execution = new SingleThreadSyncExecution(executionPO.getId(), process, metaContainer, status);
        }

        for (ExecutionPointPO subEpPO : mainEpPO.getSubExecutionPoints()) {
            subEps.add(restoreExecutionPoint(process, execution.mainEp(), subEpPO, multiThread));
        }

        return execution;
    }

    private static AbstractExecutionPoint restoreExecutionPoint(Process process, AbstractExecutionPoint supEp,
                                                 ExecutionPointPO po, boolean multiThread) {
        ExecutionPoint.Status status = ExecutionPoint.Status.valueOf(po.getStatus());
        Node currentNode = process.node(po.getCurrentNodeId());
        if (currentNode == null && status != ExecutionPoint.Status.TERMINATED) {
            throw new IllegalArgumentException("[Persist] Current node cannot be null");
        }

        ExecutionPoint.NodeStage currentNodeStage = ExecutionPoint.NodeStage.valueOf(po.getCurrentNodeStage());
        Map<String, Serializable> localContext = po.getLocalContext();
        String waitEvent = po.getWaitEvent();
        boolean waitingBereaved = po.getWaitingBereaved();
        ArrayList<AbstractExecutionPoint> subEps = new ArrayList<>();
        AbstractExecutionPoint ep;
        AbstractExecutionPoint.MetaContainer metaContainer = new AbstractExecutionPoint.MetaContainer(subEps,
                localContext, status, currentNode, currentNodeStage, waitEvent, waitingBereaved);
        if (multiThread) {
            ep = new MultiThreadAsyncExecution.MultiThreadASyncExecutionPoint(supEp,
                    Configuration.executorService, metaContainer);
        } else {
            ep = new SingleThreadSyncExecution.SingleThreadSyncExecutionPoint(supEp, metaContainer);
        }

        for (ExecutionPointPO subEpPO : po.getSubExecutionPoints()) {
            subEps.add(restoreExecutionPoint(process, ep, subEpPO, multiThread));
        }

        return ep;
    }
}
