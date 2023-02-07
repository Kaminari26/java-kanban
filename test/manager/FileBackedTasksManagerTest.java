package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    private File file;
    @BeforeEach
    public void setUp() {
        file = new File("./resources/historyManagerTest.csv");
    }
    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }
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

        assertEquals(0,manager.getHistory().size(),"Был загружен не пустой лист" );
        assertEquals(0,manager.getTaskList().size(),"Был загружен не пустой лист");
        assertEquals(0,manager.getEpicList().size(),"Был загружен не пустой лист");
        assertEquals(0,manager.getTaskList().size(),"Был загружен не пустой лист");
        file.delete();
    } @Test
    void loadFromFileNormalTest() {

        File file = new File("./resources/HistoryManagerNormalTest.csv");
        FileBackedTasksManager managerLoadFile = FileBackedTasksManager.loadFromFile(file);

        assertEquals(3,manager.getHistory().size(),"Неверное количество элементов в листе");
        assertEquals(3,manager.getTaskList().size(),"Неверное количество элементов в листе");
        assertEquals(1,manager.getEpicList().size(),"Неверное количество элементов в листе");
        assertEquals(3,manager.getTaskList().size(),"Неверное количество элементов в листе");
        assertEquals(manager.getHistory(),managerLoadFile.getHistory());
    }

}