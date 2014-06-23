package korobitsin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Main {

    private static Set<String> traversedUrls = Collections.synchronizedSet(new HashSet<String>());

    public static void main(String[] args) {
        String baseUrl = getBaseUtl(args);
        try {
            parsePage(baseUrl);
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void parsePage(final String baseUrl) throws IOException {
        Document document = Jsoup.connect(baseUrl).get();
        for (Element link : document.select("a[href]")) {
            String url = link.attr("abs:href");
            traversedUrls.add(url);
        }

        for (final String link : traversedUrls) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        parsePage(link);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

}

    private static String getBaseUtl(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        String url = args[0];

        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }

        return url;
    }
}
