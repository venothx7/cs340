/**
 * Created by Venoth on 4/24/2017.
 */


public class threadj {
    public class Student extends Thread {

        String name;
        boolean teachAbsent = true;
        public Student (String name){
            this.name = name;
        }

        public void run (){
            System.out.println(name);

            while (teachAbsent){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
