import java.util.Iterator;
import java.util.Random;

public class TeacherThread extends Thread {

    private static boolean roomClosed = true; // closed
    public static int examNum = 1;
    public static long time = System.currentTimeMillis();

    //creates teacher
    TeacherThread() {
        setName("Teacher");
    }


    // methods to execute
    public void run() {
        Random randGen = new Random();

        arrivesInSchool();
        roomClosed = false; // room is open
        //System.out.println("size of Q1 "+Que.waitQue.size());


        //********      EXAM 1          *********

        //dumps Q1 into Q2
        //if expression is true, than take the left value
        int temp = Que.waitQue.size() <= 10 ? Que.waitQue.size() : 10;
        // System.out.println("temp: "+temp);
        // make sure temp is always less than 10 or size of waitQue
        for (int i = 1; i <= temp; i++) {
            studentThread s = Que.deWaitQue();
            if (s != null) {
                s.setPriority(s.getPriority() - 1);
                Que.addClassroomQue(s);
                msg("dumped to Q2");
            }
        }


        //busy wait, wait until class room is filled or exam1 starts
        //System.out.println(main.exam1Time);
        while ((System.currentTimeMillis() - time) <= main.exam1Time && Que.classroomQue.size() <= 10) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Que.classroomQue.size() <= 10) {
            roomClosed = true; //close room
        }

        //Until Exam time comes up
        while ((System.currentTimeMillis() - time) <= main.exam1Time) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg("EXAM 1 STARTS, Duration: " + main.examDuration);

        roomClosed = true; // Room will be closed

        // for students that come after this time, are Qued for exam 2
        examNum++;


        // PRINTING classroomQue
        Iterator it = Que.classroomQue.iterator();
        System.out.println("classroomQue:" + Que.classroomQue.size());
        while (it.hasNext()) {
            System.out.println(" " + it.next().toString());
        }


        //System.out.println("  "+ Que.classroomQue.peek());
        //System.out.println("class room size: " + Que.classroomQue.size());
        //System.out.println("  " + main.student[4].toString());


        // Put Teacher to Sleep for duration ot Exam time 200ms
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Interrupt the students, when exam time is over
        for (studentThread s : Que.classroomQue) {
            s.interrupt();
        }


        // WAIT FOR EXAM TO FINISH
        try {
            Thread.sleep(main.examDuration);
            msg("Exam 1 is Finshed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // allow Teacher to take a break
        try {
            msg("Break time:" + main.breakTime);
            Thread.sleep(main.breakTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //***************** EXAM 2 ***********************
        // open room
        roomClosed=false;



        //dumps Q1 into Q2
        //if expression is true, than take the left value
        int tem = Que.waitQue.size() <= 10 ? Que.waitQue.size() : 10;
        // System.out.println("temp: "+temp);
        // make sure temp is always less than 10 or size of waitQue
        for (int i = 1; i <= temp; i++) {
            studentThread s = Que.deWaitQue();
            if (s != null) {
                s.setPriority(s.getPriority() - 1);
                Que.addClassroomQue(s);
                msg("dumped to Q22");
            }
        }


        msg("Teacher Left");
    }

    //Simulates students to arrive in different times to school.
    private void arrivesInSchool() {
        Random randGen = new Random();
        int random = randGen.nextInt(550); // teacher will arrive before exam1 starts for sure
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("arrived: " + random + "ms"); // Prints teacher arrived

    }
    // Assigns Grade from 10-100, inputt he student and the Exam#
    public static synchronized void assignGrade(int examNum, studentThread s) {
        Random randGen = new Random();
        switch (examNum) {
            case 1:
                s.setExam1(randGen.nextInt(91) + 10);
            case 2:
                s.setExam2(randGen.nextInt(91) + 10);
            case 3:
                s.setExam3(randGen.nextInt(91) + 10);
            default:
                System.out.println(" AssignGrade not working");
                break;

        }
    }
    public static synchronized int assignGrade1() {
        Random randGen = new Random();

        return (randGen.nextInt(91) + 10);


    }
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }
    public static boolean getRoomClosed() {
        return roomClosed;
    }
    public void setRoomClosed(Boolean roomClosed) {
        this.roomClosed = roomClosed;
    }
}
