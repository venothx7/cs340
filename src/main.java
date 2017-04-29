import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Venoth on 4/22/2017.
 */

// randGen.nextInt((max-min) + 1 ) + min;
public class main {
    //Default values
    public static int maxCapacity = 5; //Q, one less than actual value
    public static int numStudents = 11; // Q2,-1 , 15

    //Threads
    public static studentThread[] student;
    public static TeacherThread teacher;
    public static long time = System.currentTimeMillis();

    //Starting timers for Exam1,2,3
    public static long exam1Time = 1500;
    public static long exam2Time = 2500;
    public static long exam3Time = 3000;
    public static long breakTime = 100;
    public static long examDuration = 200;








    public static void main(String[] args) {

        msg("Start Simulation");

        teacher = new TeacherThread();
        teacher.start();


        student = new studentThread[numStudents];
        //Create Students
        //System.out.println(teacher.getRoomClosed());
        for (int i = 1; i < numStudents; i++) {
            student[i] = new studentThread(i);
            student[i].start();
        }
        //System.out.println(teacher.getRoomClosed());
        //msg("Simulation Done");


    }
    public static void msg(String m) {
        long t = System.currentTimeMillis()-time;
        System.out.println("["+(t)+"] "+Thread.currentThread().getName()+": "+m);
    }

}


