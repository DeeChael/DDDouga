package net.deechael.dddouga.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Channel;
import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.item.Episode;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class T80Utils {

    private final static WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);

    private final static String DESC_1 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 ']/a[@class='link-hover']/@href";
    private final static String DESC_2 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1']/a[@class='link-hover']/@href";
    private final static String DESC_3 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 mmr0 pmr0']/a[@class='link-hover']/@href";

    private final static String NAME_1 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 ']/a[@class='link-hover']/span[@class='lzbz']/p[@class='name']/text()";
    private final static String NAME_2 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1']/a[@class='link-hover']/span[@class='lzbz']/p[@class='name']/text()";
    private final static String NAME_3 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 mmr0 pmr0']/a[@class='link-hover']/span[@class='lzbz']/p[@class='name']/text()";

    private final static String COVER_1 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 ']/a[@class='link-hover']/img[@class='lazy']/@src";
    private final static String COVER_2 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1']/a[@class='link-hover']/img[@class='lazy']/@src";
    private final static String COVER_3 = "/html/body/div[@class='main']/div[@class='index-area clearfix']/ul/li[@class='p1 m1 mmr0 pmr0']/a[@class='link-hover']/img[@class='lazy']/@src";

    private final static String EPISODE_NAME = "/html/body/div[@class='main']/div[@id='stab1']/div[@class='playfrom tab8 clearfix']/ul/div[@class='stui-pannel stui-pannel-bg clearfix']/div[@id='vlink_%s']/div[@class='stui-pannel_bd col-pd clearfix']/ul[@class='stui-content__playlist clearfix']/li/a/text()";
    private final static String EPISODE_PLAY_LINK = "/html/body/div[@class='main']/div[@id='stab1']/div[@class='playfrom tab8 clearfix']/ul/div[@class='stui-pannel stui-pannel-bg clearfix']/div[@id='vlink_%s']/div[@class='stui-pannel_bd col-pd clearfix']/ul[@class='stui-content__playlist clearfix']/li/a/@href";

    private final static String VIDEO_LINK = "/html/body/div[5]/div[@class='player mb']/div[@class='main']/div[@class='MacPlayer']/table/tbody/tr/td[@id='playleft']/iframe/@src";

    private final static String STATIC_VIDEO_LINK = "/html/body/div[5]/div[@class='player mb']/div[@class='main']/script[1]";

    private final static String REAL_VIDEO_LIOK = "html/body/div[@id='mvideo']/div[@class='dplayer-video-wrap']/video[@class='dplayer-video dplayer-video-current']/@ppp-src";

    private final static String QQAKU_JS = "html/body/script[8]";

    static {
        WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
        WEB_CLIENT.getOptions().setCssEnabled(false);
        WEB_CLIENT.getOptions().setTimeout(3 * 1000);
    }

    public static List<Douga> getPage(int page) {
        List<Douga> items = new ArrayList<>();
        //try {
        //    HtmlPage htmlPage = WEB_CLIENT.getPage("http://www.cucuyy.com/type/4-" + page + ".html");
        //    //WEB_CLIENT.waitForBackgroundJavaScript(3 * 1000);
        //    WEB_CLIENT.waitForBackgroundJavaScript(500);
        //    Document document = Jsoup.parse(htmlPage.asXml());
        //    items.addAll(solve(document, DESC_1, NAME_1, COVER_1));
        //    items.addAll(solve(document, DESC_2, NAME_2, COVER_2));
        //    items.addAll(solve(document, DESC_3, NAME_3, COVER_3));
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        Call call = DDDouga.getHttpClient().newCall(new Request.Builder().url("http://www.cucuyy.com/type/4-" + page + ".html").get().build());
        try {
            Response response = call.execute();
            Document document = Jsoup.parse(response.body().string());
            items.addAll(solve(document, DESC_1, NAME_1, COVER_1));
            items.addAll(solve(document, DESC_2, NAME_2, COVER_2));
            items.addAll(solve(document, DESC_3, NAME_3, COVER_3));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public static List<Channel> getEpisodes(Douga douga) {
        List<Channel> channels = new ArrayList<>();
        //try {
        //    HtmlPage page = WEB_CLIENT.getPage("http://www.cucuyy.com/vod/" + douga.getId() + ".html");
        //    WEB_CLIENT.waitForBackgroundJavaScript(500);
        //    Document document = Jsoup.parse(page.asXml());
        //    int channel = 1;
        //    while (true) {
        //        List<Episode> episodes = new ArrayList<>();
        //        List<String> nameList = Xsoup.compile(String.format(EPISODE_NAME, channel)).evaluate(document).list();
        //        List<String> playLinkList = Xsoup.compile(String.format(EPISODE_PLAY_LINK, channel)).evaluate(document).list();
        //        if (nameList.size() == 0)
        //            break;
        //        if (nameList.size() != playLinkList.size())
        //            throw new RuntimeException("The length of each part of items is not same!");
        //        for (int i = 0; i < nameList.size(); i++) {
        //            episodes.add(new Episode(douga, nameList.get(i), playLinkList.get(i), i));
        //        }
        //        channels.add(new Channel("线路 " + channel, channel - 1, episodes));
        //        channel++;
        //    }
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        Call call = DDDouga.getHttpClient().newCall(new Request.Builder().url("http://www.cucuyy.com/vod/" + douga.getId() + ".html").get().build());
        try {
            Response response = call.execute();
            Document document = Jsoup.parse(response.body().string());
            int channel = 1;
            while (true) {
                List<Episode> episodes = new ArrayList<>();
                List<String> nameList = Xsoup.compile(String.format(EPISODE_NAME, channel)).evaluate(document).list();
                List<String> playLinkList = Xsoup.compile(String.format(EPISODE_PLAY_LINK, channel)).evaluate(document).list();
                if (nameList.size() == 0)
                    break;
                if (nameList.size() != playLinkList.size())
                    throw new RuntimeException("The length of each part of items is not same!");
                for (int i = 0; i < nameList.size(); i++) {
                    episodes.add(new Episode(douga, nameList.get(i), playLinkList.get(i), i));
                }
                channels.add(new Channel("线路 " + channel, channel - 1, episodes));
                channel++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channels;
    }

    public static String getPlayLink(Episode episode) {
        //
        //try {
        //    HtmlPage page = WEB_CLIENT.getPage("http://www.cucuyy.com" + episode.getPlayLink());
        //    WEB_CLIENT.waitForBackgroundJavaScript(1000);
        //    Document document = Jsoup.parse(page.asXml());
        //    String result = Xsoup.compile(VIDEO_LINK).evaluate(document).list().get(0);
        //    if (result.startsWith("https://play.eueuyy.com")) {
        //        return result.substring("https://play.eueuyy.com/dp/m3u8.php?v=".length());
        //    } else if (result.startsWith("https://new.qqaku.com")) {
        //        for (Element element : document.getElementsByTag("body").get(0).getElementsByTag("script")) {
        //            if (element.hasAttr("src"))
        //                continue;
        //            String js = element.wholeText().replace(" ", "").replace("\n", "").replace("\t", "");
        //            return js.split("varmain=\"")[1].split("\";varplayertype")[0];
        //        }
        //    }
        //    System.out.println(result);
        //    return result;
        //} catch (Throwable e) {
        //    //throw new RuntimeException(e);
        //    return null;
        //}

        Call call = DDDouga.getHttpClient().newCall(new Request.Builder().url("http://www.cucuyy.com" + episode.getPlayLink()).get().build());
        try {
            Response response = call.execute();
            Document document = Jsoup.parse(response.body().string());
            Elements elements = Xsoup.compile(STATIC_VIDEO_LINK).evaluate(document).getElements();
            Element element = elements.get(0);
            JsonObject object = JsonParser.parseString(element.html().replace("var player_aaaa=", "")).getAsJsonObject();
            String result = object.get("url").getAsString();
            if (result.startsWith("https://play.eueuyy.com")) {
                result = result.substring("https://play.eueuyy.com/dp/m3u8.php?v=".length());
            } else if (result.startsWith("https://new.qqaku.com")) {
                // The video whose link starts with "https://new.qqaku.com" cannot be played
                // FIXME
                FrameUtils.error("该源还不支持！");
                DDDouga.getLogger().error("Unsupported source of video", new RuntimeException("Unsupported source"));
                result = qqakuLink(result);
            }
            DDDouga.getLogger().debug("Fetched video link successfully: " + result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String qqakuLink(String link) {
        // The video whose link starts with "https://new.qqaku.com" cannot be played
        // FIXME
        Call call = DDDouga.getHttpClient().newCall(new Request.Builder().url(link).get().build());
        try {
            Response response = call.execute();
            Document document = Jsoup.parse(response.body().string());
            Elements elements = Xsoup.compile(QQAKU_JS).evaluate(document).getElements();
            Element element = elements.get(0);
            String fetched = element.html().replace("\t", "");
            return "https://new.qqaku.com" + fetched.split("var main = \"")[1].split("\";")[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Douga> solve(Document document, String desc, String name, String cover) {
        List<Douga> items = new ArrayList<>();
        List<String> descList = Xsoup.compile(desc).evaluate(document).list();
        List<String> nameList = Xsoup.compile(name).evaluate(document).list();
        List<String> coverList = Xsoup.compile(cover).evaluate(document).list();
        if (!(descList.size() == nameList.size() && nameList.size() == coverList.size()))
            throw new RuntimeException("The length of each part of items is not same!");
        for (int i = 0; i < descList.size(); i++) {
            String description = descList.get(i);
            items.add(new Douga(nameList.get(i), coverList.get(i), description.substring(5, description.length() - 5)));
        }
        return items;
    }

    private T80Utils() {}

}
