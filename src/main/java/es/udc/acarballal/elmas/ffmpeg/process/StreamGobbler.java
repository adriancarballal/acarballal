package es.udc.acarballal.elmas.ffmpeg.process;

import java.io.*;

public class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStreamWriter writer;
    
    
    public StreamGobbler(InputStream is, String type, OutputStreamWriter writer)
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
