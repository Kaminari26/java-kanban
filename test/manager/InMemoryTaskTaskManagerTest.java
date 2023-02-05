package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskTaskManagerTest extends TaskManagerTest<InMemoryTaskTaskManager> {

    public InMemoryTaskTaskManagerTest() {
        super(new InMemoryTaskTaskManager());
    }
}