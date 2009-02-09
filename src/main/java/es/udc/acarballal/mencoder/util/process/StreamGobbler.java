package es.udc.acarballal.mencoder.util.process;

import java.io.*;

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStreamWriter writer;
    
    
    StreamGobbler(InputStream is, String type, OutputStreamWriter writer)
    {
        this.is = is;
        this.type = type;
        this.writer = writer;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            	writer.write(type + ">" + line + "\n");    
            } catch (IOException ioe){}
    }
}
