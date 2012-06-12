/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.v3.xyzz.executor;

import java.io.*;

/**
 * @link http://www.javaworld.com/jw-12-2000/jw-1229-traps.html?page=4
 * @author Michael C. Daconta (JavaWorld.com)
 * @date 2000-12-29
 */
class StreamGobbler extends Thread {

    InputStream is;
    String type;
    OutputStream os;

    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }
            }
            if (pw != null) {
                pw.flush();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
