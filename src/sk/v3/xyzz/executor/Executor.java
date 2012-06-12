package sk.v3.xyzz.executor;

import java.io.File;

/**
 * 
 * @author ksyz
 */
public class Executor {

    private static int threads;
    private static File cmdList;
    private static TaskList tasks;

    public static void main(String[] argv) {
        if (argv.length < 2) {
            System.out.print("Usage: executor <number of threads> <taks list file>\n");
            System.exit(1);
        }

        try {
            threads = Integer.parseInt(argv[0]);
        } catch (NumberFormatException nfe) {
            System.err.print("Invalid number of threads\n");
            System.exit(1);
        }

        cmdList = new File(argv[1]);
        if (!cmdList.exists()) {
            System.err.printf("File '%s' not found.\n", argv[1]);
            System.exit(1);
        }

        try {
            tasks = new TaskList(cmdList, threads);
        } catch (Exception e) {
            System.err.printf("File not found or read error.\n");
            e.printStackTrace();
            System.exit(1);
        }
        tasks.printAll();
        tasks.executeAll();

    }

    public static String trimLeft(String s) {
        return s.replaceAll("^\\s+", "");
    }

    public static String trimRight(String s) {
        return s.replaceAll("\\s+$", "");
    }
}
