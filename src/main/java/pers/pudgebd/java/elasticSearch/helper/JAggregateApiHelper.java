package pers.pudgebd.java.elasticSearch.helper;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import pers.pudgebd.java.utils.constants.Key;

import java.util.HashMap;
import java.util.Map;

public class JAggregateApiHelper {

    private static Map<String, Object> template_params = new HashMap<>();

    static {
        template_params.put("param_gender", Key.RAITING());
    }

    protected static void searchTempFromFile(TransportClient client) {
        SearchResponse sr = new SearchTemplateRequestBuilder(client)
                .setScript("template_gender")
//                .setScriptType(ScriptType.FILE) //要把 script 放进集群每个节点并重启
                .setScriptParams(template_params)
                .setRequest(new SearchRequest())
                .get()
                .getResponse();

        for (SearchHit sh : sr.getHits()) {
            System.out.println(sh.getId());
        }
    }


    protected static void searchTempFromClusterState(TransportClient client) {
        client.admin().cluster().preparePutStoredScript()
//                .setLang("mustache")
                .setId("template_gender")
                .setContent(new BytesArray(
                        "{\n" +
                                "    \"query\" : {\n" +
                                "        \"match\" : {\n" +
                                "            \"gender\" : \"{{param_gender}}\"\n" +
                                "        }\n" +
                                "    }\n" +
                                "}"), XContentType.JSON).get();

        SearchResponse sr = new SearchTemplateRequestBuilder(client)
                .setScript("template_gender")
                .setScriptType(ScriptType.STORED)
                .setScriptParams(template_params)
                .setRequest(new SearchRequest())
                .get()
                .getResponse();

        for (SearchHit sh : sr.getHits()) {
            System.out.println(sh.getId());
        }
    }


}
