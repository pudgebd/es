package pers.pudgebd.java.elasticSearch.bigDataTest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import pers.pudgebd.java.utils.HttpUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by kartty on 17-2-23.
 */
public class MgetJTest {

    public static void main(String[] args) {
        System.out.println("23");
//        String host = "192.168.2.225";
        String host = "127.0.0.1";
        double loopCounts = 10D;
        boolean isMget = true;
//        boolean isMget = false;

        //--- change param above

        double countsPer = 100000D;
        double totalQps = 0D;
        String urlStr = "http://" + host + ":9200/bd/employee/";
        if (isMget) urlStr += host + "_mget";

        double totalData = 1000000000D; //the same as 1s == xx nanos
        Random random = new Random();

        for (int i = 1; i <= loopCounts; i++) {
            try {
                JSONObject obj = new JSONObject();
                JSONArray idsArr = new JSONArray();
                if (isMget) {
                    for (int j = 0; j < countsPer; j++) {
                        idsArr.put(String.valueOf(random.nextInt((int) totalData)));
                    }
                    obj.put("ids", idsArr);
                }

                URL url = new URL(urlStr + (isMget ? "" : "510000000"));// + random.nextInt(1000000000)
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setConnectTimeout(1000);
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                OutputStream output = null;

                if (isMget) {
                    output = httpConn.getOutputStream();
                    output.write(obj.toString().getBytes(HttpUtils.UTF8));
                    output.flush();
                }

                double t1 = System.nanoTime();
                InputStream input = httpConn.getInputStream();
                double t2 = System.nanoTime();

                if (isMget) output.close();
                input.close();
                httpConn.disconnect();
                httpConn = null;

                double sec = ((t2 - t1) / totalData);
                double currQps = isMget ? countsPer / sec : 1D / sec;
                totalQps += currQps;
                System.out.println(EsJThread.joinStr("get counts:", countsPer,
                        ", QPS:", currQps));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("totalQps:" + totalQps / loopCounts);
    }

}
