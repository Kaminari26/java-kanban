package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> storyMap = new HashMap<>();

    private void linkLast(Task task) {
        final Node node = new Node(task, tail, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private void removeNode(int id) {
        final Node node = storyMap.remove(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
        }
    }
    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        removeNode(id);
        linkLast(task);
        storyMap.put(id, tail);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
    class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Task task, Node prev, Node next) {
            this.data = task;
            this.next = next;
            this.prev = prev;
        }
    }
}


