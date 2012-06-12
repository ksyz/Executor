/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.v3.xyzz.executor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author ksyz
 */
public class Task implements Runnable {

    private String command;
    public long startTime;

    private File status;
    private FileOutputStream statusStream;
    private String name;
    private final long timestamp;
    
    public Task() {
        timestamp = System.currentTimeMillis() / 1000;
    }
/**
 * @link http://www.javaworld.com/jw-12-2000/jw-1229-traps.html?page=4
 * @author Michael C. Daconta (JavaWorld.com)
 * @date 2000-12-29
 */
private void execute() {
        try {
            System.out.println(name + " COMMAND: " + this.command);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(getCommand());
            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), name + " ERROR: ", statusStream);
            // any output?
            //StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), name + " OUTPUT: ", System.out);
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), name + " OUTPUT: ", statusStream);
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            // any error???
            int exitVal = proc.waitFor();
            System.out.println(name + " EXIT: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            name = Thread.currentThread().getName();
            status = new File("status-" + String.valueOf(timestamp) + String.valueOf(System.nanoTime()) + "-" + name);

            if (status.exists()) {
                status.delete();
            }
            status.createNewFile();
            statusStream = new FileOutputStream(status);

            execute();
            
            statusStream.close();
        } 
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
