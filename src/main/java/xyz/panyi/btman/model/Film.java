package xyz.panyi.btman.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 电影
 *
 */
public class Film {
    private String name;
    private String href;
    private String extra;
    private String detail;
    private String magnet;
    private List<String> images = new ArrayList<String>();
    private String magnetWeburl;

    public String getMagnetWeburl() {
        return magnetWeburl;
    }

    public void setMagnetWeburl(String magnetWeburl) {
        this.magnetWeburl = magnetWeburl;
    }

    public List<String> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }
}//end class
