package pers.pudgebd.java.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kartty on 17-2-23.
 */
public class HttpUtils {

    public static String UTF8 = "utf8";

    public static String POST = "POST";
    public static String GET = "GET";
    public static String PUT = "PUT";

    public static String req(String urlStr, String method, byte[] content) {
        String respStr = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(method);
            httpConn.setConnectTimeout(1000);
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            OutputStream output = httpConn.getOutputStream();
            output.write(content);
            output.flush();

            InputStream input = null;
            try {
                input = httpConn.getInputStream();
            } catch (Exception e) {
                input = httpConn.getErrorStream();
            }
            respStr = IOUtils.toString(input, UTF8);

            output.close();
            input.close();
            httpConn.disconnect();
            httpConn = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respStr;
    }


//    public static String reqGet(String urlStr, String content) throws Exception {
//        if (content == null) content = "";
//        return req(urlStr, GET, StringUtils.getUtf8Bytes(content));
//    }
//
//    public static String reqPost(String urlStr, String content) throws Exception {
//        if (content == null) content = "";
//        return req(urlStr, POST, StringUtils.getUtf8Bytes(content));
//    }

}
