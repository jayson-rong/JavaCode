import java.util.*;
class Node {
    Task task;
    Node prev, next;
    public Node(Task task) { this.task = task; }
}
class Task implements Comparable <Task> {
    String taskName;
    String taskDescription;
    int priority;
    boolean status;
    Task(String taskName, String taskDescription, int priority, boolean status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.priority = priority;
        this.status = status;
    }
    public String getName() {
        return taskName;
    }
    public String getDescription() {
        return taskDescription;
    }
    public int getPriority() {
        return priority;
    }
    public boolean isCompleted() {
        return status;
    }
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}
class TaskList {
    LinkedList<Task> toDo;
    Node head, tail;
    int size;

    TaskList() {
        this.size = 0;
    }

    public boolean checkIfEmpty() {
        return head == null;
    }

    public void addTask(String taskName, String taskDescription, int priority, boolean status) {
        Task newTask = new Task(taskName, taskDescription, priority, status);
        Node newNode = new Node(newTask);

        if (head == null) {
            head = newNode;
            tail = newNode;
            return;

        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void removeCompletedTask() {
        if (head == null) {
            System.out.println("No tasks to remove");
            return;
        }
        Node current = head;
        while (current != null) {
            Node nextNode = current.next;
            if (current.task.isCompleted()) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }
                size--;
            }
            current = nextNode;
        }
        updatePrior();
    }
    public void sortPriority() {
        if (head == null)
            return;
        head = mergeSort(head);
    }
    protected Node mergeSort(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node middle = midPoint(head);
        if (middle == null || middle.next == null) {
            return head;
        }
        Node endOfFirstHalf = middle.next;
        middle.next = null;

        Node left = mergeSort(head);
        Node right = mergeSort(endOfFirstHalf);

        return merge(left, right);
    }
    protected Node merge(Node left, Node right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        Node head = (left.task.compareTo(right.task) < 0) ? left : right;
        Node pointer = head;

        if (head == left) {
            left = left.next;
        } else {
            right = right.next;
        }
        while (left != null && right != null) {
            if (left.task.compareTo(right.task) <= 0) {
                pointer.next = left;
                left.prev = pointer;
                pointer = left;
                left = left.next;
            } else {
                pointer.next = right;
                right.prev = pointer;
                pointer = right;
                right = right.next;
            }
        }
        pointer.next = (left != null) ? left : right;
        if (pointer.next != null)
            pointer.next.prev = pointer;
        return head;
    }
    protected Node midPoint(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node slowPointer = head;
        Node fastPointer = head.next;

        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
        }
        return slowPointer;
    }
    public void updatePrior() {
        Node current = head;
        int newPrior = 1;

        while (current != null) {
            current.task.priority = newPrior;
            newPrior++;
            current = current.next;
        }
    }
    public void addTaskAtPosition(int position, String taskName, String taskDescription, int priority, boolean status) {
        if (position < 1 || position > size + 1) {
            System.out.println("Invalid position!");
            return;
        }

        Task newTask = new Task(taskName, taskDescription, priority, status);
        Node newNode = new Node(newTask);

        if (position == 1) {
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            Node current = head;
            for (int i = 1; i < position - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            newNode.prev = current;
            if (current.next != null) {
                current.next.prev = newNode;
            }
            current.next = newNode;
            if (newNode.next == null) {
                tail = newNode;
            }
        }
        size++;
        sortPriority();
    }
    public void printTasks() {
        if (head == null) {
            System.out.println("\nThere are no tasks to complete");
        }
        Node current = head;
        int taskNum = 1;
        while (current != null) {
            Task task = current.task;
            System.out.println("\nTask #" + taskNum);
            System.out.println("Task name: " + task.getName());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Priority: " + task.getPriority());
            System.out.println("Completed: " + task.isCompleted());
            current = current.next;
            taskNum++;
        }
    }
    public static void main(String[] args) {
        TaskList listOfTasks = new TaskList();

        listOfTasks.addTask("Shopping", "Buy Clothes and pants", 6, true);
        listOfTasks.addTask("Grocery", "Buy meats and vegetables.", 4,true);

        listOfTasks.addTaskAtPosition(1,"Workout","Go to gym at 1 p.m.",2,false);

        listOfTasks.sortPriority();
        System.out.print("Sort tasks by priority:");
        listOfTasks.printTasks();

        listOfTasks.removeCompletedTask();
        System.out.print("\nAfter removing completed tasks:");
        listOfTasks.printTasks();

        boolean isEmptyOrNot = listOfTasks.checkIfEmpty();
        System.out.print("\nIs the list empty: " + isEmptyOrNot);
    }
}
