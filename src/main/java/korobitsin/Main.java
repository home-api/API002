package korobitsin;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        String baseUrl = getBaseUtl(args);

        ParseExecutor parseExecutor = new ParseExecutor(baseUrl);
        parseExecutor.runJob();

        System.out.println("done");
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
