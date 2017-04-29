import java.util.Random;
import java.util.concurrent.ThreadFactory;

public class studentThread extends Thread implements Comparable<studentThread> {
    private static int Exam1 = 0;
    private static int Exam2 = 0;
    private static int Exam3 = 0;


    public static long time = System.currentTimeMillis();
    public static int timeArrived = 0;

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    //constructor
    studentThread(int id) {
        setName("Student-" + id);
    }

    // methods to execute
    public void run() {
        Random randGen = new Random();

        arrivesInSchool(); // sleep
        setPriority(10);

        /*
         *************************   EXAM 1 ****************************
         */

        if (TeacherThread.examNum == 1) {


            // ********* If roomClosed, then add student to Wait Que *********
            if (TeacherThread.getRoomClosed()) {
                //set P=9 if waiting for on waitQ
                setPriority(Thread.currentThread().getPriority() - 1);
                Que.addWaitQue((studentThread) Thread.currentThread());
                msg("arrived, and added to waitQue for Exam 1");
            }

            // ******************  Wait for Teacher to Open Door ******************
            waitRoomOpen();

            //Add students that come after room is Open(Teacher arrived)
            if (Thread.currentThread().getPriority() == 10) {

                //class room is not full, add to classroom
                if ( Que.classroomQue.size() < main.maxCapacity) {
                    // if student inside classroom, P=8
                    setPriority(Thread.currentThread().getPriority() - 2);
                    Que.addClassroomQue((studentThread) Thread.currentThread());
                    msg("added to classroom for Exam " + TeacherThread.examNum);

                }
                //if room open and class is full then add to waitQ
                else {
                    setPriority(Thread.currentThread().getPriority() - 3);
                    Que.addWaitQue((studentThread) Thread.currentThread());
                    msg("Missed Exam 1(Classroom Full), added to Q1 for Exam 2");
                }
            }

            // put student to sleep until exam 1 Starts
            try {
                Thread.sleep((main.exam1Time ) - (System.currentTimeMillis() - time));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // EXAM 1 Started
            // For students that are in the Class Room
            if (Que.classroomQue.contains(Thread.currentThread())) {


                // Students go to sleep During Exam, wait till they get Interrupted
                try {
                    msg("Sleeping during Exam");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Exam1 = randGen.nextInt(91) + 10;
                    msg("Interrupted by Teacher in EXAM 1, Grade: " + Exam1);
                }


                // allow students to take a break
                try {
                    Thread.sleep(main.breakTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            msg("done with exam1");
        }




        /*
         *************************   EXAM 2 ****************************
         */

        //Students coming after taking Exam 1, P= 8
        //Fresh Students tht missed Exam1, P=10
        //Students tht came for Exam1, but missed, P=7,alrdy in Q1



        if (TeacherThread.examNum ==2)
        {
            // Fresh students, students that came after exam1 started and Room is closed
            if (getPriority() ==10 && TeacherThread.getRoomClosed()){
                setPriority(7);
                Que.addWaitQue((studentThread) Thread.currentThread());
                msg("added to Q1 for Exam 2, missed Exam1");
            }

            //busy waiting,Wait for teacher to open room
            while (TeacherThread.getRoomClosed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            // For students that came after exam1 Started and Room is open, add to ClassroomQ
            if (getPriority() ==10 && Que.classroomQue.size() < main.maxCapacity){
                setPriority(Thread.currentThread().getPriority() - 4);
                Que.addClassroomQue((studentThread) Thread.currentThread());
                msg("added to Q2 for Exam 2, missed Exam1");
            }
            //if class is full add to waitQ for Exam 3
            else{
                setPriority(Thread.currentThread().getPriority() - 1);
                Que.addWaitQue((studentThread) Thread.currentThread());
            }

            //Wait until Exam 2 starts
            while ((main.exam2Time ) >= (System.currentTimeMillis() - time))
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (Que.classroomQue.contains(Thread.currentThread())) {
                // Students go to sleep During Exam, wait till they get Interrupted
                try {
                    msg("Sleeping during Exam 2");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Exam2 = randGen.nextInt(91) + 10;
                    msg("Interrupted by Teacher in EXAM 1, Grade: " + Exam2);
                }


                // allow students to take a break
                try {
                    msg("Breaktime ");
                    Thread.sleep(main.breakTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


            // add here
        }

        msg("Ended run");

    } // run method


    //Simulates students to arrive in different times to school.
    private void arrivesInSchool() {

        Random randGen = new Random();
        int random = randGen.nextInt(2400);
        timeArrived = random;
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //msg("arrived at: " + random + "ms");
    }

    //This method waits for Teacher to open room while closed
    public void waitRoomOpen() {
        while (TeacherThread.getRoomClosed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getExam1() {
        return Exam1;
    }

    public int getExam2() {
        return Exam2;
    }

    public int getExam3() {
        return Exam3;
    }

    public static void setExam1(int exam1) {
        Exam1 = exam1;
    }

    public static void setExam2(int exam2) {
        Exam1 = exam2;
    }

    public static void setExam3(int exam3) {
        Exam1 = exam3;
    }

    @Override
    public int compareTo(studentThread o) {

        if (timeArrived < o.timeArrived) {
            return -1;
        } else if (timeArrived > o.timeArrived) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {

        return getName() + " Priority: " + getPriority();
    }


}
