import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Que {

    public static Queue<studentThread> waitQue = new PriorityBlockingQueue<>(6); // size 14
    public static Queue<studentThread> classroomQue = new PriorityBlockingQueue<>(4); //size 10


    public static synchronized void addWaitQue(studentThread waitList) {
        waitQue.add(waitList);
    }

    public static synchronized studentThread deWaitQue() {
        return waitQue.poll();
    }

    public static synchronized void addClassroomQue(studentThread waitList) {
        classroomQue.add(waitList);
    }

    public static synchronized studentThread deClassroomQue(studentThread waitList) {
        return classroomQue.poll();
    }

}
