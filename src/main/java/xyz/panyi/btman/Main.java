package xyz.panyi.btman;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.panyi.btman.model.Film;
import xyz.panyi.btman.util.LogUtil;
import xyz.panyi.btman.util.Utils;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        LogUtil.log("btman bootstrap...");
        parse(Config.RES);
        //parse("https://nm.nmcsym.net/pw/thread.php?fid=83&page=245");
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

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//end for each
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFilmDetail(final Film film, String url){
        LogUtil.log("fetch url : " + url);
        try {
            Document document = Jsoup.connect(url).userAgent(Config.UA).get();

            Element contentElement= document.getElementById("read_tpc");
            readImages(film , contentElement);
            readMagnet(film , contentElement);
            readDetail(film , contentElement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readMagnet(final Film film , Element contentElement){
        Elements aElems = contentElement.select("a");
        if(aElems == null || aElems.size() == 0)
            return;

        String magnetWeburl = null;
        for(Element link : aElems){
            //www.bitsdts
            String url = link.attr("href");
            if(url.contains("www.bitsdts")){
                magnetWeburl = url;
                break;
            }
        }

        if(magnetWeburl == null)
            return;

        film.setMagnetWeburl(magnetWeburl);
        LogUtil.log("fetch magnet: " + magnetWeburl);
        try {
            Document doc = Jsoup.connect(magnetWeburl).userAgent(Config.UA).get();
            Elements btnElements = doc.getElementsByClass("uk-button");
            for(Element element : btnElements){
                String hrefValue = element.attr("href");
                if(hrefValue != null && hrefValue.startsWith("magnet:")){
                    LogUtil.log("magnet : " + hrefValue);
                    film.setMagnet(hrefValue);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDetail(final Film film , Element contentElement){
        Element contentE = contentElement.getElementById("read_tpc");

        Elements all = contentE.getAllElements();
        for(Element e : all){
            if(e.tagName().equals("img")){
                e.remove();
            }else if(e.tagName().equals("a")){
                e.remove();
            }
        }

        String content = all.html()
                .replaceAll("<br>","\n")
                .replaceAll("&nbsp;","")
                .trim();

        content = Utils.replaceLineBlanks(content);
        if(!StringUtil.isBlank(film.getMagnetWeburl()) && content.endsWith(film.getMagnetWeburl())){
            content = content.substring(0 , content.length() - film.getMagnetWeburl().length()).trim();
        }
        LogUtil.log(content);
    }

    private static void readImages(final Film film , Element contentElement){
        Elements imgs = contentElement.select("img");
        for(Element img : imgs){
            String imageUrl = img.attr("src");
            if(StringUtil.isBlank(imageUrl) || imageUrl.endsWith("42600001974.gif")){//过滤宣传二维码图片
                continue;
            }

            film.getImages().add(imageUrl);
            LogUtil.log("image : " + imageUrl);
        }
    }
}// end class
