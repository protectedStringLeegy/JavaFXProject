package exThread;

import java.util.ArrayList;

public class Workflow {

    public static void main(String[] args) {

        ArrayList<WorkflowThread> workflowThreads = new ArrayList<>();
        ArrayList<WorkflowThread> workflowThreads1 = new ArrayList<>();
        ArrayList<WorkflowThread> workflowThreads2 = new ArrayList<>();

        workflowThreads.add(new WorkflowThread(3, null));
        workflowThreads1.add(new WorkflowThread(15, workflowThreads));
        workflowThreads1.add(new WorkflowThread(5, workflowThreads));
        workflowThreads1.add(new WorkflowThread(10, workflowThreads));
        workflowThreads2.add(new WorkflowThread(4, workflowThreads1));
        for (int i = 0; i < 5; i++) {
            WorkflowThread wt = new WorkflowThread(3, workflowThreads2);
            workflowThreads2 = new ArrayList<>();
            workflowThreads2.add(wt);
        }

    }

    private static class WorkflowThread extends Thread {

        private int secondsWait;
        private ArrayList<WorkflowThread> workflowThreads;

        public WorkflowThread(int secondsWait, ArrayList<WorkflowThread> workflowThreads) {
            this.secondsWait = secondsWait;
            this.workflowThreads = workflowThreads;
            start();
        }

        @Override
        public void run() {
            if (workflowThreads != null) {
                for (WorkflowThread wt : workflowThreads) {
                    try {
                        wt.join();
                    } catch (InterruptedException e) {e.getMessage();}
                }
            }

            System.out.println(getName());
            for (int i = 0; i < secondsWait; i++) {
                try {
                    sleep(1000);
                    System.out.println(i);
                } catch (InterruptedException e) {e.getMessage();}
            }


        }
    }
}
