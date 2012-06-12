/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.v3.xyzz.executor;

import java.io.*;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 *
 * @author ksyz
 */
public class TaskList {
    private final File list;
    private final Stack<Task> tasks;
    private final Integer threads;

    
    public TaskList(File list, Integer threads) throws FileNotFoundException, IOException {
        this.list = list;
        this.threads = threads;
        tasks = new Stack<Task>();
        createTaskList();
    }

    private void createTaskList() throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(list);
        BufferedReader i = new BufferedReader(new InputStreamReader(fis));
        String line;
        Pattern pEmpty = Pattern.compile("^\\s*$");
        Pattern pComment = Pattern.compile("^\\s*#");
        while ((line = i.readLine()) != null) {
            if (pEmpty.matcher(line).matches()) {
                continue;
            }
            
            if (pComment.matcher(line).find()) {
                continue;
            }
            
            String cmd = line.trim();
            Task task = new Task();
            task.setCommand(cmd);
            tasks.add(task);
        }
    }

    public void printAll() {
        synchronized (tasks) {
            for (Task x : tasks) {
                System.out.println(x.getCommand());
            }
        }
    }

    public void executeAll() {
        synchronized (tasks) {
            ExecutorService pool = new ThreadPoolExecutor(
                threads, // Core Pool Size
                threads, // Max Pool Size
                5000, // Keep Alive Time
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
            );

            for (Runnable x : tasks) {
                pool.execute(x);
            }
            
            pool.shutdown();
        }
    }
}
