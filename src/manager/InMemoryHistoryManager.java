package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private class StoryLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;
        private HashMap<Integer, Node> storyMap = new HashMap<>();
        private int size = 0;

        public void linkLast(Task task) {
            if(storyMap.containsKey(task.getId())){
                removeNode(storyMap.get(task));
            }


            storyMap.put(task.getId(), new Node<>(task));
            if (head  == null) {
                head = storyMap.get(task.getId());
                tail = storyMap.get(task.getId());
                storyMap.get(task.getId()).next = storyMap.get(task.getId());
            }else {
                head.prev = storyMap.get(task.getId());

                storyMap.get(task.getId()).next = head;
                head = storyMap.get(task.getId());
            }


        }

        public void removeNode(Node node) {
            if (node.prev != null){
               if(node.next != null){
                   node.prev = node.next;
                   node.next = node.prev;

               }else {
                   node.prev = null;
               }
                storyMap.remove(node);
            }
        }
        public ArrayList getTasks(){
            ArrayList<Node<T>> StoryTasks = new ArrayList<>();
            Node node = tail;
            for (int i = 0; i < storyMap.size(); i++){
                if (node.prev != null){
                    StoryTasks.add(node);
                    node = node.prev;
                }else {
                    StoryTasks.add(head);
                }
            }
            return StoryTasks;
        }
    }
      LinkedList<Task> history = new LinkedList<>();
    private final static int MaxHistoryCapacity = 10;

    @Override
    public void add(Task task) {

        history.add(task);
        if (history.size() > MaxHistoryCapacity) {
            history.removeFirst();
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}


