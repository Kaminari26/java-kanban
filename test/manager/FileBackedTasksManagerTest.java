package manager;

import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    public FileBackedTasksManagerTest() {

        super(new FileBackedTasksManager(new File("./resources/historyManagerTest.csv")));

    }

    @Test
    void loadFromFileFileDoesnotExist() {

        assertThrows(
                ManagerSaveException.class,
                ()->FileBackedTasksManager.loadFromFile(
                        new File("./resources/qopiwdfhakzxjcvbhliasudyhfdp.csv")));
    }

    @Test
    void loadFromFileEmptyFile() {

        File file = new File("./resources/HistoryManagerTestFileEmptyFile.csv");
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(0,manager.getHistory().size());
        assertEquals(0,manager.getTaskList().size());
        assertEquals(0,manager.getEpicList().size());
        assertEquals(0,manager.getTaskList().size());
    } @Test
    void loadFromFileNormalTest() {

        File file = new File("./resources/HistoryManagerNormalTest.csv");
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(3,manager.getHistory().size());
        assertEquals(3,manager.getTaskList().size());
        assertEquals(1,manager.getEpicList().size());
        assertEquals(3,manager.getTaskList().size());
    }

}