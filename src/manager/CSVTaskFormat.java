package manager;

import helpers.LocalDateTimeHelper;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic,startTime,duration";
    }

    private static String getAllTasksInString(List<Task> taskList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : taskList) {
            stringBuilder.append(toString(task));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    protected static String toString(Task task) {
        String str = String.join(",", Integer.toString(task.getId()),
                task.getType().name(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                (task.getType().equals(TypeTask.SUBTASK) ? ((Subtask)task).getEpicId().toString() : ""),
                task.getStartTime().toString(),
                task.getDuration().toString());
        return str;
    }

    protected static Task fromString(String value){
        String[] splitString = value.split(",");
        TypeTask typeTask = TypeTask.valueOf(splitString[1]);

        switch (typeTask) {
            case TASK:
                return new Task(
                        Integer.parseInt(splitString[0]),
                        typeTask,
                        splitString[2],
                        TaskStatus.valueOf(splitString[3]),
                        splitString[4] != null ? splitString[4] : "",
                        splitString[5] != null ?
                        LocalDateTimeHelper.convertToLocalDateTime(splitString[6]) : null,
                        splitString[6] != null ?
                        Long.getLong(splitString[7]) : null);
            case SUBTASK:
                return new Subtask(Integer.parseInt(splitString[0]),
                        splitString[2],
                        typeTask,
                        splitString[4] != null ? splitString[4] : "",
                        TaskStatus.valueOf(splitString[3]),
                        Integer.parseInt(splitString[5]),
                        splitString[6] != null ?
                                LocalDateTimeHelper.convertToLocalDateTime(splitString[6]) : null,
                        splitString[7] != null ?
                                Long.getLong(splitString[7]) : null);
            case EPIC:
                return new Epic(Integer.parseInt(splitString[0]),
                        splitString[2],
                        typeTask,
                        splitString[4] != null ? splitString[4] : "",
                        TaskStatus.valueOf(splitString[3]),
                        splitString[6] != null ?
                                LocalDateTimeHelper.convertToLocalDateTime(splitString[6]) : null,
                        splitString[7] != null ?
                                Long.getLong(splitString[7]) : null);
        }

        return null;
    }

    public static String historyToString(HistoryManager historyManager) {
        final List<Task> history = historyManager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] splitStringHistory = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String story : splitStringHistory) {
            list.add((Integer.parseInt(story)));
        }
        return list;
    }
}
