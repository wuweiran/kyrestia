package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleThreadSyncExecutionTest {
    @Test
    void testSingleNodeProcess() {
        NonReentrantNode n0 = new NonReentrantNode(null);
        Process process = new TestProcess(n0);
        Execution execution = new SingleThreadSyncExecution("test_execution", process);

        execution.run();

        assertEquals(Execution.Status.TERMINATED, execution.status());
        assertTrue(n0.isTraversed());
    }

    @Test
    void testMultiNodesSequentialProcess() {
        NonReentrantNode n4 = new NonReentrantNode(null);
        NonReentrantNode n3 = new NonReentrantNode(n4);
        NonReentrantNode n2 = new NonReentrantNode(n3);
        NonReentrantNode n1 = new NonReentrantNode(n2);
        NonReentrantNode n0 = new NonReentrantNode(n1);
        Process process = new TestProcess(n0);
        Execution execution = new SingleThreadSyncExecution("test_execution", process);

        execution.run();

        assertEquals(Execution.Status.TERMINATED, execution.status());
        assertTrue(n0.isTraversed());
        assertTrue(n1.isTraversed());
        assertTrue(n2.isTraversed());
        assertTrue(n3.isTraversed());
        assertTrue(n4.isTraversed());
    }

    @Test
    void testSingleNodeBlockOnExecutingProcess() {
        NonReentrantNode n0 = new BlockOnExecutingNrNode("TEST_STR_0",null);
        Process process = new TestProcess(n0);
        Execution execution = new SingleThreadSyncExecution("test_execution", process);

        execution.run();

        assertEquals(Execution.Status.RUNNING, execution.status());
        assertTrue(n0.isEntered());
        assertTrue(n0.isExecuted());
        assertFalse(n0.isLeft());

        execution.signal("TEST_STR_0");

        assertEquals(Execution.Status.TERMINATED, execution.status());
        assertTrue(n0.isTraversed());
    }

    @Test
    void testMultiBranchesProcess() {
        NonReentrantNode n31 = new NonReentrantNode(null);
        NonReentrantNode n30 = new NonReentrantNode(n31);
        NonReentrantNode n2 = new NonReentrantNode(null);
        NonReentrantNode n1 = new NonReentrantNode(null);
        ShatterNode n0 = new ShatterNode(n1, n2, n30);
        Process process = new TestProcess(n0);
        Execution execution = new SingleThreadSyncExecution("test_execution", process);

        execution.run();

        assertEquals(Execution.Status.TERMINATED, execution.status());
        assertTrue(n1.isTraversed());
        assertTrue(n2.isTraversed());
        assertTrue(n30.isTraversed());
        assertTrue(n31.isTraversed());
    }

    @Test
    void testMultiBranchesWaitingSameEventProcess() {
        NonReentrantNode n31 = new BlockOnExecutingNrNode("TEST_STR_0", null);
        NonReentrantNode n30 = new NonReentrantNode(n31);
        NonReentrantNode n2 = new BlockOnExecutingNrNode("TEST_STR_0",null);
        NonReentrantNode n1 = new BlockOnExecutingNrNode("TEST_STR_0",null);
        ShatterNode n0 = new ShatterNode(n1, n2, n30);
        Process process = new TestProcess(n0);
        Execution execution = new SingleThreadSyncExecution("test_execution", process);

        execution.run();

        assertEquals(Execution.Status.RUNNING, execution.status());
        assertFalse(n1.isTraversed());
        assertFalse(n2.isTraversed());
        assertTrue(n30.isTraversed());
        assertFalse(n31.isTraversed());

        execution.signal("TEST_STR_0");

        assertEquals(Execution.Status.TERMINATED, execution.status());
        assertTrue(n1.isTraversed());
        assertTrue(n2.isTraversed());
        assertTrue(n31.isTraversed());
    }
}