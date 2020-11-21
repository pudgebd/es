package pers.pudgebd.java.elasticSearch.basic;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import pers.pudgebd.java.utils.constants.EsCnts;
import pers.pudgebd.java.utils.constants.Key;
import pers.pudgebd.java.utils.constants.PropName;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class JDocBasicApi {


    protected static void deleteByQuery(TransportClient client) throws Exception {
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery(Key.USER_ID_(), "6737"))
                .source(Key.GOODS())
                .execute(new ActionListener<BulkByScrollResponse>() { //execute 异步调用，get同步调用
                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        long deleted = response.getDeleted();
                        System.out.println(deleted);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle the exception
                    }
                });

    }


    protected static void updateByScript(TransportClient client) throws Exception {
        //报错
//        client.prepareUpdate(Key.GOODS(), Key.RECOMMEND(), "AV92w7yBqo1sqa1OzDYQ")
//                .setScript(new Script(
//                        ScriptType.INLINE, "ctx._source.gender = \"male\"", null, null
//                ))
//                .get();

        //正常
        client.prepareUpdate(Key.GOODS(), Key.RECOMMEND(), "AV92w7yBqo1sqa1OzDYQ")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("stay_ms", "124")
                        .endObject())
                .get();
    }


    protected static void bulkRequest(TransportClient client) throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.err.println(bulkResponse.buildFailureMessage());
        }
    }


    protected static void bulkProcessor(TransportClient client) throws Exception {
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {  }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {  }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {  }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        bulkProcessor.add(new IndexRequest("twitter", "tweet", "1").source(""));
        bulkProcessor.add(new DeleteRequest("twitter", "tweet", "2"));

        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
    }



}
