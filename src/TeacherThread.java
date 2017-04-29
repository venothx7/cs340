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
        int temp = Que.waitQue.size() < main.maxCapacity ? Que.waitQue.size() : main.maxCapacity;
        // make sure temp is always less than 10 or size of waitQue
        studentThread stud;
        for (int i = 1; i <= temp; i++) {
            stud = Que.deWaitQue();
            if (stud != null) {
                stud.setPriority(stud.getPriority() - 1);
                Que.addClassroomQue(stud);
                msg("dumped to Q2 " + stud.toString());
            }
        }


        //busy wait, wait until class room is filled or exam1 starts
        //System.out.println(main.exam1Time);
        while ((System.currentTimeMillis() - time) <= main.exam1Time && Que.classroomQue.size() <= main.maxCapacity) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Que.classroomQue.size() <= main.maxCapacity) {
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
//        Iterator it = Que.classroomQue.iterator();
//        System.out.println("classroomQue:" + Que.classroomQue.size());
//        while (it.hasNext()) {
//            System.out.println(" " + it.next().toString());
//        }

        System.out.println("classroomQue:" + Que.classroomQue.size());
        for (studentThread s : Que.classroomQue) {
            System.out.println("  " + s.toString());
        }


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
        msg("Break time:" + main.breakTime + ", next Exam starts: " + main.exam2Time);


        System.out.println("waitQue Size : " + Que.waitQue.size());
        for (studentThread s : Que.waitQue) {

            System.out.println(" in waitQ " + s.toString());
        }
        try {

            // dump students from Classroom to waitQue
            for (studentThread s : Que.classroomQue) {
                s.setPriority(7);
                Que.addWaitQue(Que.deClassroomQue());
                msg("dumped to waitQ from Q2 " + s.toString());
            }

            Thread.sleep(main.breakTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if classroomQ is 0
        System.out.println("Class room Size : " + Que.classroomQue.size());
        System.out.println("waitQue Size : " + Que.waitQue.size());
        for (studentThread s : Que.waitQue) {

            System.out.println(" in waitQ " + s.toString());
        }

        //***************** EXAM 2 ***********************

        System.out.println();
        System.out.println();
        msg("Exam 2 Starting Soon");

        //dumps Q1 into Q2
        //if expression is true, than take the left value
        temp = Que.waitQue.size() < main.maxCapacity ? Que.waitQue.size() : main.maxCapacity;
        // make sure temp is always less than 10 or size of waitQue
        for (int i = 1; i <= temp; i++) {
            stud = Que.deWaitQue();
            if (stud != null) {
                stud.setPriority(stud.getPriority() - 1);
                Que.addClassroomQue(stud);
                msg(" dumped to Classroom FOR EXAM 2 " + stud.toString());
            }
        }



        roomClosed = false;// open room

        //Until Exam 2 time comes up
        while ((System.currentTimeMillis() - time) <= main.exam2Time) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        msg("--------------------Exam 2 Started -------------------------------------------------------");


        roomClosed = true;//close room

        //Printing Students currently taking Exam2
        System.out.println("classroomQue:" + Que.classroomQue.size());
        for (studentThread s : Que.classroomQue) {
            System.out.println("  " + s.toString());
        }

        System.out.println("waitmQue:" + Que.waitQue.size());
        for (studentThread s : Que.waitQue) {
            System.out.println("  " + s.toString());
        }

        //***************** EXAM 3 ***********************
        msg("--------------------Exam 3-------------------------------------------------------");
        examNum++;
        msg("Teacher Left");
    }

    //Simulates students to arrive in different times to school.
    private void arrivesInSchool() {
        Random randGen = new Random();
        int random = randGen.nextInt(1350); // teacher will arrive before exam1 starts for sure
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
