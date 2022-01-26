package clan.midnight.kyrestia;

import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.service.ProcessService;
import clan.midnight.kyrestia.infra.xml.XMLParseException;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;
import clan.midnight.kyrestia.pvm.ExecutionFactory;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KyrestiaEngine {
    private final ConcurrentMap<String, Process> processes = new ConcurrentHashMap<>(32);

    public void deployBPMN(InputStream processDefinitionInputStream) {
        List<BpmnProcess> processList;
        try {
            processList = ProcessService.loadProcesses(processDefinitionInputStream);
        } catch (XMLParseException e) {
            throw new EngineException("Kyrestia: Failed to deploy BPMN.", e);
        }

        for (BpmnProcess bpmnProcess : processList) {
            processes.putIfAbsent(bpmnProcess.getId(), bpmnProcess);
        }
    }

    public Execution execute(String processId) {
        Process process = processes.get(processId);
        if (process == null) {
            throw new EngineException("Kyrestia: Cannot find process.");
        }

        return ExecutionFactory.createExecution(process);
    }
}
