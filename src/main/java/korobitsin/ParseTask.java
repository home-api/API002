package korobitsin;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Revision Info : $Author$ $Date$
 * Author  : e.korobitsin
 * Created : 23/06/2014 06:32
 *
 * @author e.korobitsin
 */
public class ParseTask implements Callable<Set<String>> {

    private String baseUrl;

    public ParseTask(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Set<String> call() throws Exception {
        Set<String> urls = new HashSet<String>();
        try {
            Document document = Jsoup.connect(baseUrl).timeout(5000).get();
            for (Element link : document.select("a[href]")) {
                String url = link.attr("abs:href");
                if (url.startsWith("http://allminsk.biz/")) {
                    urls.add(url);
                }
            }
        } catch (UnsupportedMimeTypeException e) {
        } catch (IOException e) {
            System.out.println();
            System.out.println();
            e.printStackTrace();
            System.out.println(baseUrl);
            System.out.println();
            System.out.println();
        }
        return urls;
    }

}
