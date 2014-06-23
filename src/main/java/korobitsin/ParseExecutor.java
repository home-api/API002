package korobitsin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Revision Info : $Author$ $Date$
 * Author  : e.korobitsin
 * Created : 23/06/2014 06:43
 *
 * @author e.korobitsin
 */
public class ParseExecutor {

    private String baseUrl;
    private ConcurrentMap<String, String> traversedUrls;
    private ScheduledExecutorService threadPool;

    public ParseExecutor(String baseUrl) {
        this.baseUrl = baseUrl;
        this.traversedUrls = new ConcurrentHashMap<String, String>();
        this.threadPool = Executors.newScheduledThreadPool(3);
    }

    public void runJob() {
        Future<Set<String>> result = threadPool.schedule(new ParseTask(baseUrl), 2, TimeUnit.SECONDS);
        try {
            for (String url : result.get()) {
                submitNewTasks(url);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void submitNewTasks(String url) {
        if (traversedUrls.get(url) == null) {
            if (traversedUrls.putIfAbsent(url, "") == null) {
                System.out.print(traversedUrls.size() + ";");
                System.out.println(url);
                Future<Set<String>> result = threadPool.schedule(new ParseTask(url), 2, TimeUnit.SECONDS);
                try {
                    for (String newUrl : result.get()) {
                        submitNewTasks(newUrl);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        threadPool.shutdown();
    }

}
