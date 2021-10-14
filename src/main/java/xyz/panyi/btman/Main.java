package xyz.panyi.btman;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import xyz.panyi.btman.model.Film;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        LogUtil.log("btman bootstrap...");
        //parse(Config.RES);
        parse("https://nm.nmcsym.net/pw/thread.php?fid=83&page=245");
    }

    public static void parse(String filmUrl){
        try {
            Document document = Jsoup.connect(filmUrl).userAgent(Config.UA).get();
            //LogUtil.log(document.title());

            Elements elements = document.getElementsByClass("tr3 t_one");
            //elements = document.body().getElementsByAttribute("text-align:left;line-height:25px; FONT-SIZE:11pt");
            for (Element element : elements) {
                String alignValue = element.attr("align");
                //LogUtil.log(alignValue);
                Elements h3Elements = element.getElementsByTag("h3");
                if(h3Elements == null || h3Elements.size() == 0){
                    continue;
                }

                String href = h3Elements.first().getElementsByTag("a").attr("href");
                if(!href.startsWith("html_data")){
                    continue;
                }

                Film film = new Film();
                final String name = h3Elements.text();
                film.setName(name);
                film.setHref(href);
                LogUtil.log(name + "  " + href);

                readFilmDetail(film , Config.RES_HOST+"/"+href);
            }//end for each
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFilmDetail(final Film film, String url){
        LogUtil.log(url);
        try {
            Document document = Jsoup.connect(url).userAgent(Config.UA).get();

            Element contentElement= document.getElementById("read_tpc");
            Elements imgs = contentElement.select("img");
            for(Element img : imgs){
                LogUtil.log(img.outerHtml());
            }
            //LogUtil.log(contentElement.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readOnePage(final String url){
        LogUtil.log("read page : " + url);
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");
            for (Element link : links) {
                LogUtil.log("link : " + link.attr("href"));
                LogUtil.log("text : " + link.text());
            }//end for each
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}// end class
