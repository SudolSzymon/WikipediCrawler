import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.net.URLDecoder.decode;

class Engine {
    private Interface anInterface;
    private String initialURL;
    private Set<String> linkSet=new HashSet<>();
    private Set<String> searchedLinkSet=new HashSet<>();
    private ArrayList<URLmanager> activeDownloads=new ArrayList<>();

    Engine(Interface anInterface){
        this.anInterface=anInterface;
    }
    void check(String url){
        linkSet.add(url);
        anInterface.addLink(url);
        extractBase(url);
        downloadLinks();
    }


    private void downloadLinks() {
        anInterface.statusUpdate(false,linkSet.size());
        for (String link:linkSet){
            if(!searchedLinkSet.contains(link)){
                searchedLinkSet.add(link);
                activeDownloads.add(new URLmanager().Download(link));
            }
        }
    }

    void displayResults() {
       extractResult();
        anInterface.clearOutput();
        for (String link:linkSet){
            anInterface.addLink(link);
        }
        anInterface.statusUpdate(isDownlaodingFinished(),linkSet.size());
        System.out.println(linkSet.size());
    }

    private void extractResult() {
        ArrayList<String> temp=new ArrayList<>(URLmanager.getWebsite());
        for(String line:temp){
            Boolean undecoded=false;
            try {
               line=decode(line,"UTF-8");
            } catch (Exception e) {
                undecoded=true;
            }
            if(line.contains("href") && !undecoded) {
               int index=line.indexOf("href=\"")+6;
               int index2=line.indexOf("\"",index);
               if(index>0&&index2>0) {
                   line = line.substring(index, index2);
                   if(!line.contains("https://")&&line.contains("/wiki/")) {
                       linkSet.add(initialURL+line);
                   }
               }
           }
        }
    }
    private  boolean isDownlaodingFinished(){
        for (URLmanager m:activeDownloads)
            if (m.isRunning()) return false;
        return true;
    }
    private void extractBase(String websiteURL) {
        try {
            initialURL=websiteURL.substring(0,websiteURL.indexOf("/wiki/"));
        } catch (Exception e){
            System.out.println("can not extract main page url");
        }
    }
    void openAll(){
        for(String link:linkSet){
            openLink(link);
        }
    }

    private void openLink(String URL){
        try {
            Desktop.getDesktop().browse(new URL(URL).toURI());
        } catch (IOException | URISyntaxException exeption) {
            exeption.printStackTrace();
            System.out.println("Fsiled to open "+URL);
        }
    }

}
