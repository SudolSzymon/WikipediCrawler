import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

class URLmanager implements Runnable{
private static volatile ArrayList<String> website= new ArrayList<>();

    private String websiteURL;
    private Thread ownThread;



    URLmanager Download(String websiteURL){
        this.websiteURL=websiteURL;
        ownThread = new Thread(this);
        ownThread.start();
        System.out.println("started thread");
        return this;
    }

    boolean isRunning(){
        return ownThread.isAlive();
    }



    synchronized static ArrayList<String> getWebsite(){
        return website;
    }

    @Override
    public void run() {
        InputStream is = null;
        BufferedReader br;
        String line;
        URL url;
        try {
            url = new URL(websiteURL);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                getWebsite().add(line);

            }
        } catch (IOException ioe) {
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}
