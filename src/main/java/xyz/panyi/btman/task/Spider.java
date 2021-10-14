package xyz.panyi.btman.task;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import xyz.panyi.btman.Config;
import xyz.panyi.btman.model.Film;
import xyz.panyi.btman.util.LogUtil;
import xyz.panyi.btman.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Spider {
    private Film mLastFilm = null;

    private String mBasicUrl = Config.PORN_URL;

    private List<Film> filmList = new ArrayList<Film>();

    public void execute(){
        LogUtil.log("btman spider start...");
        filmList.clear();

        setupLastFilm();
        List<Film> list = parseWeb(mBasicUrl , 0);
        filmList.addAll(list);

        LogUtil.log("spider new film count : " + filmList.size());
    }

    public void executePullAll(){
        LogUtil.log("btman spider start...");
        filmList.clear();

        setupLastFilm();
        final int maxPage = 247;//porn
        //final int maxPage = 245;
        for(int i = maxPage;i >= 1;i--){
            List<Film> list = parseWeb(mBasicUrl, i);
            filmList.addAll(list);
        }//end for each

        LogUtil.log("spider new film count : " + filmList.size());
    }


    private boolean haveThisFilm(Film film){
        if(mLastFilm == null || StringUtil.isBlank(mLastFilm.getName()))
            return false;

        return film.getName().equals(mLastFilm.getName());
    }

    public void setupLastFilm(){
        //todo read last film
    }

    public List<Film> parseWeb(String filmUrl , int pageNumber){
        List<Film> filmList = new ArrayList<Film>();
        try {
            if(pageNumber > 1){
                filmUrl += ("&page=" + pageNumber);
            }
            LogUtil.log("read page : " + filmUrl);
            Document document = Jsoup.connect(filmUrl).userAgent(Config.UA).get();
            //LogUtil.log(document.title());

            Elements elements = document.getElementsByClass("tr3 t_one");
            //elements = document.body().getElementsByAttribute("text-align:left;line-height:25px; FONT-SIZE:11pt");
            for (Element element : elements) {
                Elements h3Elements = element.getElementsByTag("h3");
                if(h3Elements == null || h3Elements.size() == 0){
                    continue;
                }

                String href = h3Elements.first().getElementsByTag("a").attr("href");
                if(!href.startsWith("html_data")){
                    continue;
                }
                final Film film = fillFilm(h3Elements , href);
                if(haveThisFilm(film)){
                    break;
                }

                filmList.add(film);
            }//end for each
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filmList;
    }

    public Film fillFilm(Elements elements , String href){
        Film film = new Film();
        final String name = elements.text();

        LogUtil.log(name + "  " + href);
        film.setName(name);
        film.setHref(href);
        readFilmDetail(film , Config.RES_HOST+"/"+href);
        return film;
    }

    public void readFilmDetail(final Film film, String url){
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

    private void readMagnet(final Film film , Element contentElement){
        Elements aElems = contentElement.select("a");
        if(aElems == null || aElems.size() == 0)
            return;

        String magnetWeburl = null;
        for(Element link : aElems){
            //www.bitsdts
            String url = link.attr("href");
            if(url.contains("bitsdts")){
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
            if(btnElements.size() <= 0){
                btnElements = doc.getElementsByClass("uk-button ");
            }

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

    private void readDetail(final Film film , Element contentElement){
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

    private void readImages(final Film film , Element contentElement){
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
}//end class
