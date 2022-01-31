package clan.midnight.kyrestia.persist;

import clan.midnight.kyrestia.infra.spi.ExtensionScanner;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.persist.model.ExecutionPO;
import clan.midnight.kyrestia.persist.model.ExecutionPointPO;

import java.util.stream.Collectors;

public class PersistenceHelper {
    static final String PERSISTENCE_EXTENSION_GROUP = "__storage";
    static ProcessRepository processRepository;
    static ExecutionRepository executionRepository;
    static {
        ExtensionScanner scanner = new ExtensionScanner(PersistenceHelper.class.getPackage().getName());
        scanner.scan();
        processRepository = scanner.getExtension(PERSISTENCE_EXTENSION_GROUP, ProcessRepository.class);
        executionRepository = scanner.getExtension(PERSISTENCE_EXTENSION_GROUP, ExecutionRepository.class);
    }

    private PersistenceHelper() {}

    public static ExecutionPO convert(Execution execution) {
        ExecutionPO po = new ExecutionPO();
        po.setId(execution.id());
        po.setProcessId(execution.process().id());
        po.setStatus(execution.status().ordinal());
        po.setMainExecutionPoint(convert(execution.snapshot()));
        return po;
    }

    public static ExecutionPointPO convert(ExecutionPoint executionPoint) {
        ExecutionPointPO po = new ExecutionPointPO();
        po.setCurrentNodeId(executionPoint.currentNode().id());
        po.setCurrentNodeStage(executionPoint.currentNodeStage().ordinal());
        po.setLocalContext(executionPoint.localContext());
        po.setWaitEvent(executionPoint.waitEvent());
        po.setStatus(executionPoint.status().ordinal());
        po.setWaitingBereaved(executionPoint.isWaitingBereaved());
        po.setSubExecutionPoints(executionPoint.subExecutionPoints().stream()
                .map(PersistenceHelper::convert).collect(Collectors.toList()));
        return po;
    }
}
