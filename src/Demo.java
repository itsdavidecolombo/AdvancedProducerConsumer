
import queue.IQueue;
import queue.IQueueListener;
import queue.QueueListenerException;
import runnable.logger.Formatter;
import runnable.logger.FormatterRepo;
import queue.LogQueue;
import runnable.logger.Logger;
import runnable.peer.Connection;
import pluggable.Dashboard;
import pluggable.IPluggable;
import runnable.peer.BasePeer;
import runnable.peer.Producer;
import runnable.RunnableException;

import java.util.Random;

public class Demo {

    public static void main(String[] args){

// ========================================================================================================

//                                          DASHBOARD SETUP

// ========================================================================================================
        IQueueListener listenerRef;      // create a reference variable of type QueueListener
        IQueue queueRef;                // create a reference variable of type IQueue

        queueRef = new LogQueue();

        IPluggable dashboard = Dashboard.getInstance();          // get the unique instance of the Dashboard
        listenerRef = (IQueueListener) dashboard;
        try {
            listenerRef.registerToQueue(queueRef);              // register the Dashboard to the queue
            listenerRef = Logger.getDefaultLogger();            // create the Logger for the Dashboard
            listenerRef.registerToQueue(queueRef);              // register the dashboard Logger to the queue
            queueRef = null;
        } catch(QueueListenerException e) {
            System.err.println("QueueListenerException caught in main(): " + e.getMessage());
        }

// ========================================================================================================

//                                          LOGGER SETUP

// ========================================================================================================

        Formatter schemeRef;    // create a reference variable for the Formatter scheme
        Logger loggerRef;        // create a reference variable for the Logger

        schemeRef = FormatterRepo.getInstance().                // define the formatter scheme
                newFormatter("mylogformatter",
                "OPENER: <<<; CLOSER: >>>");

        loggerRef = Logger.getLoggerWithFormatter(schemeRef);   // define the logger with the formatter scheme

        queueRef = new LogQueue();      // create a new Queue
        try {
            loggerRef.registerToQueue(queueRef);         // register the Logger to the Queue
        } catch(QueueListenerException e) {
            System.err.println("QueueListenerException caught in main(): " + e.getMessage());
        }

// ==========================================================================================
        final BasePeer p1 = new Producer("VermicelloPazzerello", queueRef);
        Connection conn1 = new Connection(dashboard, p1);
        p1.openConnection(conn1);
        p1.openConnection(new Connection(dashboard, p1));       // test if the exception is correctly caught

        new Thread(() -> {      // runner Thread for running the BasePeer 1
            try {
                p1.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();

// ==========================================================================================
        queueRef = new LogQueue();
        loggerRef = Logger.getDefaultLogger();
        try {
            loggerRef.registerToQueue(queueRef);
        } catch(QueueListenerException e) {
            System.err.println("QueueListenerException caught in main(): " + e.getMessage());
        }

        final BasePeer p2 = new Producer("LumachinaGentile", queueRef);
        new Thread(() -> {      // runner Thread for running the BasePeer 2
            try {
                p2.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        // p2.openConnection(conn1);       // test ConnException when a connection is already opened
        p2.openConnection(new Connection(dashboard, p2));       // open connection

// ==========================================================================================
        queueRef = new LogQueue();
        loggerRef = Logger.getDefaultLogger();
        try {
            loggerRef.registerToQueue(queueRef);
        } catch(QueueListenerException e) {
            System.err.println("QueueListenerException caught in main(): " + e.getMessage());
        }

        final BasePeer p3 = new Producer("LaMuccaMuu", queueRef);
        new Thread(() -> {      // runner Thread for running the BasePeer 3
            try {
                p3.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        p3.openConnection(new Connection(dashboard, p3));       // open connection

        Random rand = new Random();

        try {
            Thread.sleep(10000);
            p1.pause();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.pause();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.pause();
            Thread.sleep(5000);
            p1.resume();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.resume();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.resume();
            Thread.sleep(10000);
            p1.stop();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.stop();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.stop();
        } catch(InterruptedException | RunnableException e) {
            e.printStackTrace();
        }

    }
}



/* THIS IS A TEST FOR UNDERSTANDING IF THE FORMATTER AND FORMATTER REPOSITORY WORK PROPERLY
        try {
            FormatterRepo formatterRepository = FormatterRepo.getInstance();
            Formatter got;

            Thread.sleep(1000);
            got = formatterRepository.getFormatterByName("myformatter");    // exception test
            System.out.println(got.toString());
            Thread.sleep(1000);
            got = formatterRepository.getDefaultFormatter();    // default formatter test
            System.out.println(got.toString());

            formatterRepository.newFormatter("MyFormatter", "$", "#", "-"); // formatter creation test
            Thread.sleep(1000);
            got = formatterRepository.getFormatterByName("MyFormatter");    // formatter pull test
            System.out.println(got.toString());

        } catch(InterruptedException e) {
            e.printStackTrace();
        }

         */
