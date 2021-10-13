package xyz.panyi.btman;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        LogUtil.log("btman bootstrap...");

        parse();
    }

    public static void parse(){
        try {
            Document document = Jsoup.connect(Config.RES_HOST).get();
            LogUtil.log(document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}// end class
