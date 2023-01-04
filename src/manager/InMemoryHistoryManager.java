package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager<T> implements HistoryManager {
        public Node<T> head;
        public Node<T> tail;
        private Map<Integer, Node> storyMap = new HashMap<>();

        private void linkLast(Task task) {
            final Node node = new Node(task);
            if (head == null) {
                head = node;
            } else {
                node.prev = tail;
            }
            tail = node;
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
            Node node = tail;
            for (int i = 0; i < storyMap.size(); i++){
                if (node.prev != null){
                    history.add(node);
                    node = node.prev;
                }else {
                    history.add(node);

                }
            }
            return history;
        }


    ArrayList<Node<T>> history = new ArrayList<>(); // надеюсь я понял про сбор в один лист
    private final static int Max_History_Capacity = 10;

    @Override
    public void add(Task task) {
          if (storyMap.containsKey(task.getId())) {
            removeNode(storyMap.get(task.getId()));
        }

        storyMap.put(task.getId(), new Node<>(task));
        linkLast(task);
          if (storyMap.size() > Max_History_Capacity) {
            removeNode(tail);
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public ArrayList getHistory() {
        getTasks();
        return history;
    }
}


