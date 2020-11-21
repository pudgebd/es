package pers.pudgebd.java.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kartty on 17-4-5.
 */
public class EsUtils {

    private static TransportClient client = null;
    private static Object LOCK = new Object();

    public static TransportClient getClient() {
        if (client == null) {
            synchronized (LOCK) {
                if (client == null) {
                    try {
                        Settings settings = Settings.builder()
                                .put("cluster.name", "my-application").build();
                        List<Plugin> plugins = new ArrayList<Plugin>();

                        client = new PreBuiltTransportClient(settings)
                                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return client;
    }


    public static void loop(List<Terms.Bucket> buckets) {
        for (Terms.Bucket ele : buckets) {
            System.out.println(ele.getKeyAsString());
        }
    }


    public static void printHits(SearchHits hits) {
        for (SearchHit sh : hits) {
            System.out.println(sh.getId());
        }
    }

}
