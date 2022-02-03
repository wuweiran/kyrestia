package clan.midnight.kyrestia;

import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.service.ProcessService;
import clan.midnight.kyrestia.infra.xml.XMLParseException;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;
import clan.midnight.kyrestia.persist.PersistenceHelper;
import clan.midnight.kyrestia.persist.model.ExecutionPO;
import clan.midnight.kyrestia.pvm.ExecutionFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KyrestiaEngine {
    public void deployBPMN(InputStream processDefinitionInputStream) {
        List<BpmnProcess> processList;
        try {
            processList = ProcessService.loadProcesses(processDefinitionInputStream);
        } catch (XMLParseException e) {
            throw new EngineException("Kyrestia: Failed to deploy BPMN.", e);
        }

        for (BpmnProcess bpmnProcess : processList) {
            PersistenceHelper.getProcessRepository().save(bpmnProcess);
        }
    }

    public Execution execute(String processId, Map<String, Serializable> parameters) {
        Process process = PersistenceHelper.getProcessRepository().getById(processId);
        if (process == null) {
            throw new EngineException("Kyrestia: Cannot find process.");
        }

        return ExecutionFactory.createExecution(process, parameters);
    }

    public ExecutionPO convertExecutionForPersistence(Execution execution) {
        return PersistenceHelper.convert(execution);
    }

    public Execution restoreExecutionFromPersistence(ExecutionPO executionPO) {
        try {
            return PersistenceHelper.restore(executionPO);
        } catch (Exception e) {
            throw new EngineException("Kyrestia: Cannot restore execution.", e);
        }
    }
}
